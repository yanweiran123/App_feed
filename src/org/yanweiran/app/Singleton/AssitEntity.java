package org.yanweiran.app.Singleton;

import org.json.JSONArray;
import org.yanweiran.app.activity.Assit;

import java.io.Serializable;

/**
 * Created by lenov on 14-3-4.
 */
public class AssitEntity  implements Serializable {

    private   String title;
    private String pulisher;
    private String imgUrl;
    private String target;
    private String  getReady;
    private String operate;
    private String notice;
    public AssitEntity()
    {

    }

    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setPulisher(String pulisher){
        this.pulisher = pulisher;
    }
    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }
    public void setTarget(String target)
    {
        this.target = target;
    }
    public  void setGetReady(String getReady)
    {
        this.getReady = getReady;
    }
    public void setOperate(String operate)
    {
        this.operate = operate;
    }

    public  void setNotice(String notice)
    {
        this.notice = notice;
    }

    public  String getTitle()
    {
        return  title;
    }
    public String getImgUrl()
    {
        return  imgUrl;
    }
    public String getTarget()
    {
        return  target;
    }
    public  String getGetReady()
    {
        return  getReady;
    }
    public  String getOperate()
    {
        return operate;
    }
    public String getNotice()
    {
        return  notice;
    }
    public String getPulisher(){
        return  pulisher;
    }
}
