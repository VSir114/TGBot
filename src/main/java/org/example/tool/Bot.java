package org.example.tool;

import org.example.Message.MessageSend;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        //返回Bot名称
        return "0721Bot";
    }
    //Bot令牌
    @Override
    public String getBotToken() {

        return "";
    }

    @Override
    public void onUpdateReceived(Update update) {
        /**
         * 重要方法。每当有新的更新可用时，它都会自动调用。
         * */
        Message message = update.getMessage();
        User from = message.getFrom();
        String text = message.getText();

        try {
            MessageSend.SentTextByOne("you send message : "+ text , String.valueOf(from.getId()));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        System.out.println(update);
    }
}
