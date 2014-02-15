package org.yanweiran.app.Singleton;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by lenov on 14-1-28.
 */
public class ChatObject implements Serializable {

    private Integer fid;
    private String name;

    public ChatObject()
    {

    }

    public ChatObject(Integer fid,String name)
    {
        this.fid = fid;
        this.name = name;
    }

    public void setName(String name)
    {
        this.name= name;
    }

    public  void setFid(Integer fid)
    {
        this.fid=fid;
    }

    public Integer getFid()
    {
        return  fid;
    }

    public String getName()
    {
        return  name;
    }

}

