package org.yanweiran.app.Singleton;



/**
 * Created by lenov on 14-4-8.
 */
public class PublicType  {
    public   String type ;
    public   int Detail_TYPE ;
    public  int  IMG_INDEX;
    public int POSITION;
    public int TweetIsZan;
    public String TweetZan;
    public int IMG_NUM=0;
    public String TweetComm;
    public int classPosition;
    private volatile static PublicType publicType;


    private PublicType(){

    }
    public static PublicType getPublicType() {
        if (publicType == null) {
            synchronized (PublicType.class) {
                if (publicType == null) {
                    publicType = new PublicType();
                }
            }
        }
        return publicType;
    }
}