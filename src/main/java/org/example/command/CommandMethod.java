package org.example.command;

import org.example.MQ.MQMessageUtil;
import org.example.Pojo.MessageImageText;
import org.example.Message.MessageSend;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * 命令方法处理
 * */
public class CommandMethod {
    /**
     * 判断命令
     * */
    public static void CommandCheck(Update update) throws TelegramApiException, IOException, TimeoutException {
        String text = update.getMessage().getText();
        text = text.trim();
        User from = update.getMessage().getFrom();

        //校验是否为命令
        if(text.startsWith("/image")){
            MessageSend.SendIamgeByOne("",String.valueOf(from.getId()));
        }
        if(text.startsWith("/table")){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",from.getId());
            MQMessageUtil.MessageSend(jsonObject.toString(),"bot.queue");
        }
    };
}
