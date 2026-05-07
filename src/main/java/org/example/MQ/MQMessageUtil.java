package org.example.MQ;

import com.rabbitmq.client.*;
import org.example.Message.MessageSend;
import org.example.Pojo.MessageImageText;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

//封装消息队列方法
public class MQMessageUtil {

    private static final String HOST = "";
    private static final int PORT = 5672;
    private static final String USERNAME = "";
    private static final String PASSWORD = "";
    private static final String VIRTUAL_HOST = "/";

    //连接对象
    private static Connection connection;

    //全局连接对象唯一

    public static synchronized Connection getConnection() throws IOException, TimeoutException {
        if (connection != null && connection.isOpen()) {
            return connection;
        }
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(HOST);
        connectionFactory.setPort(PORT);
        connectionFactory.setUsername(USERNAME);
        connectionFactory.setPassword(PASSWORD);
        connectionFactory.setVirtualHost(VIRTUAL_HOST);

        // 自动恢复连接
        connectionFactory.setAutomaticRecoveryEnabled(true);

        // 网络恢复间隔，单位毫秒
        connectionFactory.setNetworkRecoveryInterval(5000);

        connection = connectionFactory.newConnection();
        return connection;
    }

    public static Map<String,Object> deadMap (){
        Map<String, Object> retryArgs = new HashMap<>();
        retryArgs.put("x-message-ttl", 10000);
        retryArgs.put("x-dead-letter-exchange", "");
        retryArgs.put("x-dead-letter-routing-key", "bot.queue.deedMessage");
        return retryArgs;
    }

    public static void queueInit(String queueName,Map<String,Object> map) throws IOException, TimeoutException {
        Connection conn = getConnection();
        try(Channel channel = conn.createChannel()) {
            channel.queueDeclare(
                    queueName,
                    true,   // durable：队列持久化
                    false,  // exclusive：不排他，允许多个连接访问
                    false,  // autoDelete：不自动删除
                    map == null ? null : map
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //发送消息到队列
    /**
     * @param message 消息内容
     * @param queeName 队列名，传入队列不存在会新建
     * */
    public static void MessageSend(String message , String queeName) throws IOException, TimeoutException {
        Connection conn = getConnection();

        try(Channel channel = conn.createChannel()) {
            queueInit(queeName,null);

            AMQP.BasicProperties build = new AMQP.BasicProperties().builder()
                    .deliveryMode(2)//消息持久化
                    .contentType("application/json")
                    .contentEncoding("UTF-8")
                    .build();

            channel.basicPublish("",queeName,build,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息发送");
        }
    }

    public static void MessageGet(String queeName) throws IOException, TimeoutException {
        Connection conn = getConnection();
        Channel channel = conn.createChannel();

        //一次只处理一条
        channel.basicQos(1);
        DeliverCallback deliverCallback = (tag , messagebody) -> {
            //获取消息体
            String body = new String(messagebody.getBody(),StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(body);

            //业务逻辑执行
            System.out.println("message get :" + body);
            try{
                long id = jsonObject.getLong("id");
                List<MessageImageText> list = new ArrayList<>();
                list.add(new MessageImageText("测试文本","https://api.yujn.cn/api/gzl_ACG.php?type=image&form=pc"));
                list.add(new MessageImageText("测试文本2","https://api.yujn.cn/api/gzl_ACG.php?type=image&form=pc"));
                MessageSend.buildQuotedUpdateMessageTextImage(String.valueOf(id),list);

                //消息确认
                channel.basicAck(messagebody.getEnvelope().getDeliveryTag(),false);
            }catch (Exception e){
                e.printStackTrace();
                //业务逻辑处理过程中如果报错则放到重试队列
                try {
                    Map<String, Object> retryArgs = new HashMap<>();
                    retryArgs.put("x-message-ttl", 10000);
                    retryArgs.put("x-dead-letter-exchange", "");
                    retryArgs.put("x-dead-letter-routing-key", "message.fail.queue");
                    //初始化失败队列
                    queueInit("message.fail.queue",retryArgs);

                } catch (TimeoutException ex) {
                    throw new RuntimeException(ex);
                }
                //channel.basicAck(messagebody.getEnvelope().getDeliveryTag(),false);
                channel.basicNack(messagebody.getEnvelope().getDeliveryTag(),false,true);
            }
        };
        //当消费端被取消时会触发这个回调函数
        CancelCallback cancelCallback = (tag) -> {
            System.out.println("消费端被取消");
        };

        //开启监听队列
        channel.basicConsume(queeName,
                false,//false收到消息时不自动确认
                deliverCallback,
                cancelCallback
        );
    }
}
