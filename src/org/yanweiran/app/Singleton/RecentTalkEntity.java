package org.yanweiran.app.Singleton;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by lenov on 14-3-27.
 */
public class RecentTalkEntity  implements Serializable {

     private int fid;
     private String msgHead;
     private  String  msgName;
     private  String lastTalk;
     private String  msgTime;
     private int identity;
     private  int status;
    public RecentTalkEntity(){

    }
    public  RecentTalkEntity(  int fid,String msgHead, String  msgName, String lastTalk, String  msgTime,int tag){
        this.fid =fid;
        this.msgHead = msgHead;
        this.msgName = msgName;
        this.lastTalk = lastTalk;
        this.msgTime = msgTime;
        this.identity = tag;

    }
    public void setFid(int fid)
    {
        this.fid=fid;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public void setMsgHead(String msgHead)
    {
        this.msgHead=msgHead;
    }
    public void setMsgName(String msgName)
    {
        this.msgName=msgName;
    }
    public void setMsgTime(String msgTime)
    {
        this.msgTime=msgTime;
    }
    public void setLastTalk(String lastTalk)
    {
        this.lastTalk=lastTalk;
    }
    public void setIdentity(int tag){
        this.identity = tag;
    }

    public int getFid()
    {
        return  this.fid;
    }
    public  String getMsgHead()
    {
        return  this.msgHead;
    }
    public String getMsgName()
    {
        return  this.msgName;
    }
    public String getLastTalk()
    {
        return  this.lastTalk;
    }
    public String getMsgTime()
    {
        return  this.msgTime;
    }
    public int getIdentity(){
        return  this.identity;
    }
    public int getStatus(){
        return  this.status;
    }
}
