package org.example.command;

import org.example.Message.MessageSend;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * 命令方法处理
 * */
public class CommandMethod {

    /**
     * 判断命令
     * */
    public static void CommandCheck(Update update) throws TelegramApiException, IOException {
        String text = update.getMessage().getText();
        text = text.trim();

        User from = update.getMessage().getFrom();

        //校验是否为命令
        if(text.startsWith("/image")){
            MessageSend.SendIamgeByOne("",String.valueOf(from.getId()));
        }
    };
}
