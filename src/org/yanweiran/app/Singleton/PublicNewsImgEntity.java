package org.yanweiran.app.Singleton;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

/**
 * Created by lenov on 14-5-3.
 */
public class PublicNewsImgEntity implements Serializable {

    private byte[] smallBitmap;
    private byte[] bigBitmap;
    private String url;

    public void setUrl(String url){
        this.url = url;
    }
    public void setSmallBitmap(byte[] smallBitmap){
        this.smallBitmap = smallBitmap;
    }
    public void setBigBitmap(byte[] bigBitmap){
        this.bigBitmap = bigBitmap;
    }
    public byte[] getSmallBitmap(){
        return  smallBitmap;
    }
    public byte[] getBigUri(){
        return bigBitmap;
    }
    public String getUrl(){
        return  url;
    }
}
