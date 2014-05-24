package org.yanweiran.app.Singleton;

import android.content.Context;
import android.content.SharedPreferences;

import org.yanweiran.app.activity.Tweet;

import java.util.ArrayList;

/**
 * Created by lenov on 14-4-27.
 */
public class TweetMemory {
    private Context context;
    public TweetMemory(Context context){
        this.context = context;
    }

    public String getUserInfo() {
        SharedPreferences pref =context.getSharedPreferences(
                "TweetEntityList", 0);
        return pref.getString("user_info", "");
    }
    public  boolean saveUserInfo(String tweetEntityList) {
        SharedPreferences pref = context.getSharedPreferences(
                "TweetEntityList", 0);
        return pref.edit().putString("TweetEntityList", tweetEntityList).commit();
    }
}
