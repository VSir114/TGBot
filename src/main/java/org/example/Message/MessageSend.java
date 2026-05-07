package org.example.Message;

import org.example.Main;
import org.example.Pojo.MessageImageText;
import org.example.tool.HttpSend;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 消息发送
 * */
public class MessageSend {

    /**
     * @Prame text 发送的文本内容 ， from 发送给对方的ID
     *
     */
    public static void SentTextByOne(String text , String from) throws TelegramApiException {
        SendMessage build = SendMessage.builder()
                .text(text)
                .chatId(from)
                .build();
        Main.bot.execute(build);
    }

    public static void  SendIamgeByOne(String text , String from) throws TelegramApiException, IOException {
        byte[] bytes = HttpSend.HttpGetImage("https://api.yujn.cn/api/gzl_ACG.php?type=image&form=pc", new HashMap<>());
        InputFile inputFile = FileInit(bytes);
        SentTextByOne(new SimpleDateFormat("YYYY-MM-DD HH:mm").format(new Date()),from);
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(from)
                .photo(inputFile)
                .build();
        Main.bot.execute(sendPhoto);
    }

    public static InputFile FileInit(byte[] bytes){
        InputFile inputFile = new InputFile().setMedia(new ByteArrayInputStream(bytes),String.valueOf(System.currentTimeMillis()));
        return inputFile;
    }
    /**
     * 构造并发送类似“更新通知 + 引用块”样式的消息。
     * Telegram 客户端需要配合 HTML parse mode 才会渲染引用块。
     */
    public static void sendQuotedUpdateMessage(
            String from,
            List<Map<String,Object>> messageList
    ) throws TelegramApiException {
        SendMessage.SendMessageBuilder sendMessage = SendMessage.builder();
        try {
            String message = buildQuotedUpdateMessageOnlyText(messageList);

            SendMediaGroup mediaGroup = SendMediaGroup.builder().build();


            SendMessage build = sendMessage.chatId(from)
                    .text(message)
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .build();
            Main.bot.execute(build);
        }catch (Exception e){
            SendMessage build = sendMessage.chatId(from)
                    .text(e.getMessage())
                    .build();
            Main.bot.execute(build);
        }

    }
    /**
     * 消息链构造器--文本
     * @param messageList 自定义消息链，键支持Text文本，Link链接、Quote二级标题
     * */
    public static String buildQuotedUpdateMessageOnlyText(List<Map<String, Object>> messageList) throws RuntimeException, IOException {
        StringBuilder builder = new StringBuilder();
        for(Map<String,Object> messageMaps : messageList){
            for(String type : messageMaps.keySet()){
                if(type.equals("Text")){
                    builder.append("<b>");
                    builder.append(messageMaps.get(type));
                    builder.append("</b>");
                    builder.append("\n");
                }
                else if(type.equals("Link")){
                    builder.append("<a href=\"");
                    builder.append(messageMaps.get(type));
                    builder.append("\">");
                    builder.append(messageMaps.get(type));
                    builder.append("\n");
                    builder.append("链接地址");
                    builder.append("</a>");
                    builder.append("\n");
                }
                else if(type.equals("Quote")){
                    builder.append("<blockquote>");
                    builder.append(messageMaps.get(type));
                    builder.append("</blockquote>\n");
                }
                else {
                    throw new RuntimeException("UnknowType:"+type+"Need Type Text Link Quote");
                }
            }
        }
        return builder.toString();
    }

    /**
     * 图文混发
     * */
    public static void buildQuotedUpdateMessageTextImage(String fromId , List<MessageImageText> list) throws RuntimeException, IOException, TelegramApiException {
        SendMediaGroup mediaGroup = new SendMediaGroup();
        mediaGroup.setChatId(fromId);
        List<InputMedia> inputMediaList = new ArrayList<>();

        for(MessageImageText messageImageText : list){
            byte[] bytes = HttpSend.HttpGetImage(messageImageText.getImgUrl(), new HashMap<>());
            InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
            //及时关流
            try(InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);){
                inputMediaPhoto.setMedia(byteArrayInputStream,String.valueOf(System.currentTimeMillis()));
            }catch (IOException ioException){

            }
            inputMediaPhoto.setParseMode(ParseMode.HTML);
            inputMediaList.add(inputMediaPhoto);
        }
        //只有第一个Caption会生效，多了文字发不出去
        StringBuilder builder = new StringBuilder();

        builder.append("<b>");
        builder.append("随机美图");
        builder.append("</b>");
        builder.append("\n");

        builder.append("<a href=\"");
        builder.append("https://api.yujn.cn/api/gzl_ACG.php?type=image&form=pc");
        builder.append("\">");
        builder.append("\n");
        builder.append("API地址");
        builder.append("</a>");
        builder.append("\n");

        inputMediaList.get(0).setCaption(builder.toString());
        mediaGroup.setMedias(inputMediaList);

        try {
            Main.bot.execute(mediaGroup);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    private static String escapeHtml(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private static String escapeHtmlAttribute(String text) {
        return escapeHtml(text).replace("'", "&#39;");
    }
}
