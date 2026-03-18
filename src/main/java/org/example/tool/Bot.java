package org.example.tool;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        //返回Bot名称
        return "0721Bot";
    }

    @Override
    public String getBotToken() {
        //Bot令牌
        return "";
    }

    @Override
    public void onUpdateReceived(Update update) {
        /**
         * 这是最重要的方法。每当有新的更新可用时，它都会自动调用。
         * */
        System.out.println(update);
    }
}
