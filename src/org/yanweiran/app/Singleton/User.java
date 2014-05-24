package org.yanweiran.app.Singleton;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lenov on 14-1-18.
 */
public class User {
    public String username;
    public  String tag;
    public int flag;
    public   String email;
    public String bbname;
    public String token;
    public JSONArray teacherClass;
    public JSONObject jsonObject;
    public int msgNum;
    public String headUrl;
    public String school_num;
    public int news=0;
    public int notifi=0;
    public  int IsRegister;
    public  String tagname;
    private volatile static User user;




    private User (){

    }
    public static User getUser() {
        if (user == null) {
            synchronized (User.class) {
                if (user == null) {
                    user = new User();
                }
            }
        }
        return user;
    }
}