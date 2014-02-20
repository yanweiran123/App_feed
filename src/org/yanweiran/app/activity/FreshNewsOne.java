package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.HttpPackage.HttpUtil;
import org.yanweiran.app.Singleton.FreshOneEntity;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.clicklistener.BottomMenuListener;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.myadpter.FreshOneNewsComment;
import org.yanweiran.app.myadpter.RoundImageView;

/**
 * Created by lenov on 14-2-16.
 */
public class FreshNewsOne extends Activity {

    @Override
    public  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fresh_detail);
        Intent intent = getIntent();
        final FreshOneEntity   msgEntity= (FreshOneEntity)intent.getSerializableExtra("singleMsg");
        final TextView  txtName = (TextView)findViewById(R.id.name);
        final RoundImageView headImg = (RoundImageView)findViewById(R.id.headImg);
        final TextView txtTime = (TextView)findViewById(R.id.newsTime);
        final TextView txtContent = (TextView)findViewById(R.id.textContent);
        final ListView mListView = (ListView)findViewById(R.id.commentList);
        final TextView btn_Comm = (TextView)findViewById(R.id.btn_comment);
        final TextView btn_Zan = (TextView)findViewById(R.id.btn_zan);
        final    RequestQueue requestQueue = Volley.newRequestQueue(this);

        btn_Comm.setText(msgEntity.getCommNum());
        btn_Zan.setText(msgEntity.getZanNum());
        txtName.setText(msgEntity.getSendName());
        txtTime.setText(msgEntity.getMsgTime());
        txtContent.setText(msgEntity.getMsgContent());
        loadImageByVolley(headImg,msgEntity.getHeadUrl());


        String jsonDataUrl1 = HttpUtil.BASE_URL+"commentlists.php?"+"token="+ User.getUser().token+"&tid="+msgEntity.getMsgId();
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET,jsonDataUrl1,null
                ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try{
                    JSONArray jsonArray =  jsonObject.getJSONArray("lists");
                    int num = jsonArray.length();
                    String[] name =new String[num];
                    String[] content = new String[num];
                    String[] time = new String[num];
                    String[] url = new String[num];
                    for(int i = 0;i<num;i++)
                    {
                        name[i] = jsonArray.getJSONObject(i).getString("name");
                        content[i] = jsonArray.getJSONObject(i).getString("c");
                        time[i] = jsonArray.getJSONObject(i).getString("time");
                        url[i] = jsonArray.getJSONObject(i).getString("headimg");
                    }
                    FreshOneNewsComment mAdapter = new  FreshOneNewsComment( num, name,url,content,time,FreshNewsOne.this);
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
        requestQueue.add(jsonObjectRequest1);
        new BottomMenuListener().clickTurn(this);
    }




    /*加载图片函数*/
    public void loadImageByVolley(ImageView imageView,String imgUrl){
        String imageUrl=imgUrl;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, R.drawable.head_photo,R.drawable.head_photo);
        imageLoader.get(imageUrl, listener);
    }

}
