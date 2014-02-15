package org.yanweiran.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.HttpPackage.HttpUtil;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.clicklistener.BottomMenuListener;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.myadpter.RelativeCommentAdapter;

/**
 * Created by lenov on 13-12-28.
 */
public class RelativeComment extends Activity{
    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        LinearLayout mainLinearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.relativecomment,null);
        setContentView(mainLinearLayout);
        final ListView mListView = (ListView)findViewById(R.id.commContainer);



        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String jsongDataUrl = HttpUtil.BASE_URL+"mycommentlists.php?";
        String parameters = "token="    + User.getUser().token;
        jsongDataUrl=jsongDataUrl+parameters;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsongDataUrl,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try{
                    DialogUtil.showDialog(RelativeComment.this,jsonObject.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("lists");
                        Integer num = jsonArray.length();
                        String[]    name = new  String[num];
                        String[]    time = new String[num];
                        String[]    commContent =   new String[num];
                        String[]    headImgUrl  =   new String[num];
                        String[]    commWhat    =   new String[num];
                    for(int i=0;i<num;i++)
                    {
                        name[i] = jsonArray.getJSONObject(i).getString("name");
                        time[i] =   jsonArray.getJSONObject(i).getString("time");
                        commContent[i] =   jsonArray.getJSONObject(i).getString("c");
                        headImgUrl[i]   = jsonArray.getJSONObject(i).getString("headimg");
                        commWhat[i] =   jsonArray.getJSONObject(i).getString("rc");
                    }
                    RelativeCommentAdapter mAdapter = new RelativeCommentAdapter(num,headImgUrl,name,time,commContent,commWhat,RelativeComment.this);
                    mListView.setAdapter(mAdapter);
                }
                catch (JSONException ex)
                {

                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(jsonObjectRequest);


                    /*底部按钮的触发事件*/
        new BottomMenuListener().clickTurn(this);
    }
}
