package org.example.Message;

import org.example.Main;
import org.example.tool.Bot;
import org.example.tool.HttpSend;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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
    ) throws TelegramApiException, IOException {
        String message = buildQuotedUpdateMessage(messageList);
        try {

        }catch (Exception e){

        }
        SendMessage sendMessage = SendMessage.builder()
                .chatId(from)
                .text(message)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .build();

        Main.bot.execute(sendMessage);
    }
    /**
     * 自定义消息链构造器
     * @param messageList 自定义消息链，键支持Text文本，Link链接、File文件、Quote二级标题
     * */
    //TODO 添加File支持
    public static String buildQuotedUpdateMessage(List<Map<String, Object>> messageList) throws RuntimeException, IOException {
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
                else if(type.equals("Image")){
                    byte[] bytes = HttpSend.HttpGetImage("https://api.yujn.cn/api/gzl_ACG.php?type=image&form=pc", new HashMap<>());
                    InputFile inputFile = FileInit(bytes);
                    builder.append("<b>Image:</b> ")
                            .append(inputFile);
                }
                else {
                    throw new RuntimeException("UnknowType:"+type+"Need Type Text Link Quote");
                }
            }
        }
        return builder.toString();
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
