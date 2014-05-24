package org.yanweiran.app.Singleton;

import java.io.Serializable;

/**
 * Created by lenov on 14-3-29.
 */
public class PhotoListEntity implements Serializable {
    private String sPhotoUrl;
    private  String tid;
    private  String headImgUrl;
    private  String name;
    private String bPhotoUrl;
    private String time;
    private String zanNum;
    private String content;
    private String commentNum;
    private int IsZan;
    private int tag;
    private  String s_photo1;
    private  String s_photo2;
    private  String s_photo3;
    private String b_photo1;
    private String b_photo2;
    private String b_photo3;

    public void setTid(String tid){
        this.tid = tid;
    }
    public void  setHeadImgUrl(String headImgUrl){
        this.headImgUrl = headImgUrl;
    }
    public void setContent(String content){
        this.content = content;
    }
    public  void setZanNum(String zanNum){
        this.zanNum = zanNum;
    }
    public  void setCommentNum(String commentNum){
        this.commentNum = commentNum;
    }
    public  void  setTag(int tag){
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
    public  void setsPhotoUrl(String sPhotoUrl)
    {
        this.sPhotoUrl = sPhotoUrl;
    }
    public void setbPhotoUrl(String bPhotoUrl)
    {
        this.bPhotoUrl = bPhotoUrl;
    }
    public  void setTime(String time)
    {
        this.time = time;
    }
    public  void setName(String name)
    {
        this.name = name;
    }
    public  void setIsZan(int IsZan){
        this.IsZan = IsZan;
    }
    public  String getsPhotoUrl()
    {
        return  sPhotoUrl;
    }
    public  String getbPhotoUrl()
    {
        return  bPhotoUrl;
    }
    public  String getTime()
    {
        return  time;
    }
    public  String getName()
    {
        return  name;
    }
    public String getTid(){
        return tid;
    }
    public String getHeadImgUrl(){ return headImgUrl;}
    public String getContent(){
        return  content;
    }
    public String getCommentNum(){
        return  commentNum;
    }
    public String getZanNum(){
        return  zanNum;
    }
    public int getIsZan(){
        return  IsZan;
    }
    public  int getTag(){
        return  tag;
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
    public String getB_photo1(){
        return b_photo1;
    }
    public  String getB_photo2(){
        return  b_photo2;
    }
    public  String getB_photo3(){
        return  b_photo3;
    }

}
