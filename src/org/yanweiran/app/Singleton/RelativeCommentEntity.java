package org.yanweiran.app.Singleton;

import android.content.Context;

/**
 * Created by lenov on 14-3-29.
 */
public class RelativeCommentEntity  {
    private String    imgUrl;
    private String    name;
    private String    time;
    private String    commContent;
    private String    commWhat;
    private String    tid;

    public void setTid(String tid){
        this.tid = tid;
    }
    public  void  setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }
    public  void setName(String name)
    {
        this.name = name;
    }
    public  void setTime(String time)
    {
        this.time = time;
    }

    public  void  setCommContent(String commContent)
    {
        this.commContent = commContent;
    }
    public  void setCommWhat(String commWhat)
    {
        this.commWhat= commWhat;
    }

    public String getTid(){
        return tid;
    }
    public  String getImgUrl()
    {
        return  imgUrl;
    }
    public  String getName()
    {
        return  name;
    }
    public String getTime()
    {
        return  time;
    }
    public  String getCommContent()
    {
        return  commContent;
    }
    public   String getCommWhat()
    {
        return  commWhat;
    }
}
