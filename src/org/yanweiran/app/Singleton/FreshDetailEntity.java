package org.yanweiran.app.Singleton;

import java.io.Serializable;

/**
 * Created by lenov on 14-2-16.
 */
public class FreshDetailEntity implements Serializable {

    private String msgId;
    private String msgTime;
    private String headUrl;
    private String sendName;
    private String msgContent;
    private String commNum;
    private String zanNum;
    public FreshDetailEntity()
    {

    }
    public  void setSendName(String sendName)
    {
        this.sendName = sendName;
    }
    public void setMsgTime(String msgTime)
    {
        this.msgTime = msgTime;
    }
    public  void setHeadUrl(String headUrl)
    {
        this.headUrl = headUrl;
    }
    public void setMsgContent(String msgContent)
    {
        this.msgContent = msgContent;
    }
    public void setCommNum(String commNum)
    {
        this.commNum = commNum;
    }
    public void setZanNum(String zanNum)
    {
        this.zanNum = zanNum;
    }
    public  void setMsgId(String msgId)
    {
        this.msgId = msgId;
    }
    public String getMsgId()
    {
        return  msgId;
    }
    public String getSendName()
    {
        return  sendName;
    }
    public String getMsgTime()
    {
        return  msgTime;
    }
    public String getHeadUrl()
    {
        return headUrl;
    }
    public  String getMsgContent()
    {
        return msgContent;
    }
    public String getCommNum()
    {
        return commNum;
    }
    public  String getZanNum()
    {
        return  zanNum;
    }
}
