package org.example.Pojo;

import lombok.Data;

/**
 * 图文混发
 * */
@Data
public class MessageImageText {

    private String caption;
    private String imgUrl;
    public MessageImageText(String caption , String imgUrl){
        this.caption = caption; this.imgUrl = imgUrl;
    }
}
