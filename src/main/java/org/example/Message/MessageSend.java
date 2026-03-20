package org.example.Message;

import org.example.Main;
import org.example.tool.Bot;
import org.example.tool.HttpSend;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.HashMap;

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
        SendPhoto sendPhoto = SendPhoto.builder()
                .photo(inputFile)
                .chatId(from)
                .build();
        Main.bot.execute(sendPhoto);
    }

    public static InputFile FileInit(byte[] bytes){
        InputFile inputFile = new InputFile().setMedia(new ByteArrayInputStream(bytes),String.valueOf(System.currentTimeMillis()));
        return inputFile;
    }
}
