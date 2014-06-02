package org.yanweiran.app.Singleton;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by lenov on 14-3-29.
 */
public class NoticeCommentEntity implements Serializable  {

    private String commName;
    private  String tid;
    private String headUrl;
    private String commContent;
    private String  commTime;
    private String rid;
    private int tag;
    private  int ismy;


    public void setRid(String rid)
    {
        this.rid = rid;
    }
    public  void setTag(int tag){
        this.tag = tag;
    }
    public  void setCommName(String commName)
    {
        this.commName = commName;
    }
    public void  setTid(String tid)
    {
        this.tid = tid;
    }
    public void setHeadUrl(String headUrl)
    {
        this.headUrl = headUrl;
    }
    public  void setCommContent(String commContent)
    {
        this.commContent = commContent;
    }
    public  void setCommTime(String commTime)
    {
        this.commTime =commTime;
    }
    public  void setIsmy(int ismy){
        this.ismy = ismy;
    }

    public  String getCommName()
    {
        return commName;
    }
    public  String getHeadUrl()
    {
        return  headUrl;
    }
    public  String getTid()
    {
        return  tid;
    }
    public  String getCommContent()
    {
        return  commContent;
    }
    public  String getCommTime()
    {
        return  commTime;
    }
    public  String getRid()
    {
        return  rid;
    }
    public  int getTag(){
        return tag;
    }
    public  int getIsmy(){
        return  ismy;
    }
}
