package org.yanweiran.app.Singleton;

import java.io.Serializable;

/**
 * Created by lenov on 14-2-26.
 */
public class NoticeEntity implements Serializable {

    private String tid;
    private String name;
    private  String sendTime;
    private  String msgContent;
    private  String s_photo1;
    private  String s_photo2;
    private  String s_photo3;
    private String b_photo1;
    private String b_photo2;
    private String b_photo3;
    private  String headImgUrl;
    private int isZan;
    private String replyNum;
    private  String appre;
    private int tag;
    private  int imgNum;

    public void setTid(String tid)
    {
        this.tid = tid;
    }
    public  void setImgNum(int imgNum){
        this.imgNum = imgNum;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public  void setSendTime(String sendTime)
    {
        this.sendTime = sendTime;
    }
    public void setMsgContent(String msgContent)
    {
        this.msgContent = msgContent;
    }
    public void setHeadImgUrl(String headImgUrl)
    {
        this.headImgUrl = headImgUrl;
    }
    public  void setS_photo1(String s_photo1)
    {
        this.s_photo1 =s_photo1;
    }
    public void setS_photo2(String s_photo2)
    {
        this.s_photo2=s_photo2;
    }
    public void  setS_photo3(String s_photo3)
    {
        this.s_photo3 =s_photo3;
    }
    public void setIsZan(int isZan)
    {
        this.isZan =isZan;
    }
    public  void setReplyNum(String replyNum)
    {
        this.replyNum = replyNum;
    }
    public  void setAppre(String appre)
    {
        this.appre = appre;
    }
    public void setTag(int tag)
    {
        this.tag = tag;
    }
    public void setB_photo1(String b_photo1){
        this.b_photo1 = b_photo1;
    }
    public  void setB_photo2(String b_photo2){
        this.b_photo2 = b_photo2;
    }
    public  void  setB_photo3(String b_photo3){
        this.b_photo3 = b_photo3;
    }

    public String getTid()
    {
        return  tid;
    }
    public String getName()
    {
        return  name;
    }
    public  String getSendTime()
    {
        return sendTime;
    }
    public  String getMsgContent()
    {
        return  msgContent;
    }
    public  String getS_photo1()
    {
        return  s_photo1;
    }
    public  String getS_photo2()
    {
        return  s_photo2;
    }
    public  String getS_photo3()
    {
        return  s_photo3;
    }
    public  String getHeadImgUrl()
    {
        return  headImgUrl;
    }
    public  String getReplyNum()
    {
        return  replyNum;
    }
    public  int getIsZan()
    {
        return  isZan;
    }
    public  int getTag()
    {
        return  tag;
    }
    public String getAppre()
    {
        return appre;
    }
    public String getB_photo1(){
        return b_photo1;
    }
    public  String getB_photo2(){
        return  b_photo2;
    }
    public  String getB_photo3(){
        return  b_photo3;
    }
    public  int getImgNum(){
        return  imgNum;
    }
}
