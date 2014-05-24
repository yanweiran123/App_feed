package org.yanweiran.app.Singleton;

import java.io.Serializable;

/**
 * Created by lenov on 14-2-20.
 */
public class ArrangeEntity implements Serializable {
    private String content;
    private String time;
    private String style;
    private int styleImgId;
    private int styleColorId;


    public  int getStyleImgId()
    {
        return  this.styleImgId;
    }
    public  int getStyleColorId()
    {
        return  this.styleColorId;
    }
    public  String getContent()
    {
        return  this.content;
    }
    public String getTime()
    {
        return  this.time;
    }
    public String getStyle()
    {  return  this.style;
    }
    public  void setContent(String content)
    {
        this.content = content;
    }
    public void setTime(String time)
    {
        this.time = time;
    }
    public  void setStyle(String style){this.style = style;}
    public  void setStyleImgId(int styleImgId){
        this.styleImgId = styleImgId;
    }
    public  void setStyleColorId(int styleColorId)
    {
        this.styleColorId = styleColorId;
    }
}
