package org.yanweiran.app.Singleton;

/**
 * Created by lenov on 14-1-30.
 */


import java.io.Serializable;

/**
 * 一个聊天消息的JavaBean
 *
 * @author way
 *
 */
public class ChatMsgEntity implements Serializable {
    private String name;// 消息来自
    private String date;// 消息日期
    private String message;// 消息内容
    private String imgUrl;
    private int isComMsg ;// 是否为收到的消息
    private String msgTag;
    private String sPhotoUrl;
    private String bPhotoUrl;
    public ChatMsgEntity() {
    }

    public ChatMsgEntity(String name, String date, String text, String imgUrl,int isComMsg) {
        super();
        this.name = name;
        this.date = date;
        this.message = text;
        this.imgUrl = imgUrl;
        this.isComMsg = isComMsg;
    }

    public String getMsgTag()
    {
        return  msgTag;
    }
    public void setMsgTag(String msgTag)
    {
        this.msgTag = msgTag;
    }
    public  void setsPhotoUrl(String sPhotoUrl){
        this.sPhotoUrl = sPhotoUrl;
    }
    public String getsPhotoUrl(){
            return  sPhotoUrl;
    }
    public  void setbPhotoUrl(String bPhotoUrl){
        this.bPhotoUrl = bPhotoUrl;
    }
    public  String getbPhotoUrl(){
        return  bPhotoUrl;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMsgType() {
        return isComMsg;
    }

    public void setMsgType(int isComMsg) {
       this.isComMsg = isComMsg;
    }

    public String getHeadImgUrl() {
        return imgUrl;
    }

    public void setHeadImgUrl(String  imgUrl) {
        this.imgUrl = imgUrl;
    }


}
