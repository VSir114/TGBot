package org.example.Message;

import org.example.tool.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * 消息处理
 * */
public class MessageSend {
    public static Bot bot = new Bot();

    /**
     * @Prame text 发送的文本内容 ， from 发送给对方的ID
     *
     */
    public static void SentTextByOne(String text , String from) throws TelegramApiException {
        SendMessage build = SendMessage.builder()
                .text(text)
                .chatId(from)
                .build();
        bot.execute(build);
    }
}
