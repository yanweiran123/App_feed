package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.HttpPackage.HttpUtil;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.dialog.DialogUtil;

/**
 * Created by lenov on 14-1-19.
 */
public class FreshNews extends Activity {

    @Override
    public  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout mainLinearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.freshnews,null);
        setContentView(mainLinearLayout);
        LinearLayout containerLinearLayout = (LinearLayout)findViewById(R.id.newsContainer);
        LinearLayout singleContent = (LinearLayout)getLayoutInflater().inflate(R.layout.freshnewssingle,null);
        containerLinearLayout.addView(singleContent);

        /**
         * 获取token
         * */

        String token = User.getUser().token;
        DialogUtil.showDialog(this,token);
        String JSONDataUrl= HttpUtil.BASE_URL+"feed.php?";
        JSONDataUrl = JSONDataUrl +"&token="+token;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.GET,JSONDataUrl,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        try
                        {
                            String ss= jsonObject.getString("token");
                            DialogUtil.showDialog(FreshNews.this,ss);
                        }
                        catch (JSONException ex)
                        {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        DialogUtil.showDialog(FreshNews.this,"无法连接");
                    }
                });


    }
}
