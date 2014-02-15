package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.LruCache;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.yanweiran.app.Singleton.ChatObject;
import org.yanweiran.app.Singleton.SchoolClass;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.chatadapter.MessageBoxAdapter;
import org.yanweiran.app.dialog.DialogUtil;

import java.util.List;

/**
 * Created by lenov on 13-12-28.
 */
public class MessageBox extends Activity
    {

        int ScreenWidth;
        int ScreenHeight;


        @Override
        public void onCreate(Bundle savedInstance)
        {
                    super.onCreate(savedInstance);
                    LinearLayout mainLinearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.messagebox,null);
                    setContentView(mainLinearLayout);
                    final ListView mListView = (ListView)findViewById(R.id.msgContainer);



                   /*===========================
                    * 得到屏幕的大小
                    * ===========================*/
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    ScreenWidth = size.x;
                    ScreenHeight= size.y;

                    /**
                     * 请求数据格式
                     * */

                String jsonDataUrl = HttpUtil.BASE_URL+"inbox.php?"+"token="+User.getUser().token;
                RequestQueue  requestQueue = Volley.newRequestQueue(MessageBox.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {

                                try
                                {
                                    JSONArray talkMessList = jsonObject.getJSONArray("talkmess");
                                    int num=talkMessList.length();
                                    String[] name = new String[num];
                                    String[] headUrl = new String[num];
                                    String[] lastTalk = new String[num];
                                    String[] time = new String[num];
                                    Integer[] fid = new Integer[num];

                                    for(int i=0;i<num;i++)
                                    {
                                        name[i] = talkMessList.getJSONObject(i).getString("name");
                                        headUrl[i] = talkMessList.getJSONObject(i).getString("headimg");
                                        lastTalk[i] = talkMessList.getJSONObject(i).getString("mess");
                                        time[i] =   talkMessList.getJSONObject(i).getString("time");
                                        fid[i] = talkMessList.getJSONObject(i).getInt("uid");
                                    }
                                    MessageBoxAdapter   mAdapter = new MessageBoxAdapter(num,fid,headUrl,name,lastTalk,time,MessageBox.this);
                                    mListView.setAdapter(mAdapter);


                                }
                                catch (JSONException ex)
                                {
                                           DialogUtil.showDialog(MessageBox.this,ex.toString());
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError arg0) {
                            }

                        });
                 requestQueue.add(jsonObjectRequest);
        }
//        public  void addContent(String name, String msg,String time,String url,String uid)
//        {
////
////
////            singleContent.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    Intent intent = new Intent();
////                    ChatObject.getchatObject().fid=String.valueOf(view.getId());
////                    intent.setClass(MessageBox.this,MessageSingle.class);
////                    startActivity(intent);
////                    finish();
//                }
//            });
//        }


        /*加载图片的函数*/
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



