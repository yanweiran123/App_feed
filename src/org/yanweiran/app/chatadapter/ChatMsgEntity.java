package org.yanweiran.app.chatadapter;

/**
 * Created by lenov on 14-1-30.
 */


/**
 * 一个聊天消息的JavaBean
 *
 * @author way
 *
 */
public class ChatMsgEntity {
    private String name;// 消息来自
    private String date;// 消息日期
    private String message;// 消息内容
    private String imgUrl;
    private boolean isComMeg = true;// 是否为收到的消息

    public ChatMsgEntity() {

    }

    public ChatMsgEntity(String name, String date, String text, String imgUrl,boolean isComMsg) {
        super();
        this.name = name;
        this.date = date;
        this.message = text;
        this.imgUrl = imgUrl;
        this.isComMeg = isComMsg;
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

    public boolean getMsgType() {
        return isComMeg;
    }

    public void setMsgType(boolean isComMsg) {
        isComMeg = isComMsg;
    }

    public String getImg() {
        return imgUrl;
    }

    public void setImg(String  imgUrl) {
        this.imgUrl = imgUrl;
    }
}
