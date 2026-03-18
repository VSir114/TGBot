package org.example;

import org.example.tool.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botapi = new TelegramBotsApi(DefaultBotSession.class);
        botapi.registerBot(new Bot());
    }
}