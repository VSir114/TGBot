package org.example;

import org.example.MQ.MQMessageUtil;
import org.example.tool.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    //全局Bot实例
    public static Bot bot = new Bot();
    public static void main(String[] args) throws TelegramApiException, IOException, TimeoutException {
        TelegramBotsApi botapi = new TelegramBotsApi(DefaultBotSession.class);
        botapi.registerBot(bot);
        MQMessageUtil.MessageGet("bot.queue");
    }
}