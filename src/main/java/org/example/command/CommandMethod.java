package org.example.command;

import org.example.Message.MessageSend;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

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
        if(text.startsWith("/table")){
            List<Map<String, Object>> messagelist = new ArrayList<>();
            HashMap<String, Object> stringObjectsHashMap = new HashMap<>();
            stringObjectsHashMap.put("Text","测试文本1");
            stringObjectsHashMap.put("Link","google.com");
            stringObjectsHashMap.put("Quote","二级标题");
            stringObjectsHashMap.put("Image","https://api.yujn.cn/api/gzl_ACG.php?type=image&form=pc");
            messagelist.add(stringObjectsHashMap);

            MessageSend.sendQuotedUpdateMessage(String.valueOf(from.getId()),messagelist);
        }
    };
}
