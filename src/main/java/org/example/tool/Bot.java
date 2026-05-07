package org.example.tool;

import org.example.Config.BotConfig;
import org.example.Message.MessageSend;
import org.example.command.CommandMethod;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        //返回Bot名称
        return "0721Bot";
    }
    //Bot令牌
    @Override
    public String getBotToken() {

        return BotConfig.BotToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        /**
         * 重要方法。每当有新的更新可用时，它都会自动调用。
         * */

        //MessageSend.SentTextByOne("you send message : "+ text , String.valueOf(from.getId()));
        try {
            CommandMethod.CommandCheck(update);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        System.out.println(update);
    }
}
