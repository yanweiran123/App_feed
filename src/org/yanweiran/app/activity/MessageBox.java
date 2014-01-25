package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.LruCache;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import org.yanweiran.app.Singleton.SchoolClass;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.dialog.DialogUtil;

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

                String jsonDataUrl = HttpUtil.BASE_URL+"inbox.php?"+"token="+User.getUser().token+"&school_class_id=" + SchoolClass.getSchoolClass().classId.toString();
                RequestQueue  requestQueue = Volley.newRequestQueue(MessageBox.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                DialogUtil.showDialog(MessageBox.this,jsonObject.toString());
                                try
                                {

                                    JSONArray talkNameList = jsonObject.getJSONArray("talk_names");
                                    JSONArray talkMessList = jsonObject.getJSONArray("talkmess");

                                  for(int count=0;count<talkNameList.length();count++)
                                  {
                                      JSONObject talkName = talkNameList.getJSONObject(count);
                                      String url=talkName.getString("headimg");
                                      String msg="";
                                      String uid;
                                      String time;
                                      uid =talkName.getString("uid");


                                      for(int talkMessNum=0;talkMessNum<talkMessList.length();talkMessNum++)
                                      {
                                          JSONObject talkMess = talkMessList.getJSONObject(talkMessNum);
                                          if(talkName.getString("uid")==talkMess.getString("uid"))
                                          {
                                                 msg=talkMess.getString("mess");
                                                 break;
                                          }
                                      }
                                      //addContent(talkName.getString("name"),msg,talkMessList.getJSONObject(count).getString("time"),);


                                  }


                                }
                                catch (JSONException ex)
                                {

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
        public  void addContent(String name, String msg,String time,String url,String uid)
        {
            LinearLayout mainContainer = (LinearLayout)findViewById(R.id.messageContainer);
            LinearLayout singleContent =new LinearLayout(this);
            singleContent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ScreenHeight/10));
            singleContent.setOrientation(LinearLayout.HORIZONTAL);



            LinearLayout.LayoutParams headImgLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ImageView headImg = new ImageView(this);
            loadImageByVolley(headImg,url);
            headImg.setLayoutParams(headImgLayout);

            LinearLayout textContain = new LinearLayout(this);
            LinearLayout.LayoutParams  textLayout = new LinearLayout.LayoutParams(ScreenWidth*8/10,ScreenHeight/20);
            textContain.setOrientation(LinearLayout.VERTICAL);
            textContain.setLayoutParams(textLayout);

            TextView  talkName = new TextView(this);
            talkName.setText(name);
            TextView talkMess = new TextView(this);
            textContain.addView(talkName);
            textContain.addView(talkMess);
            talkMess.setText(msg);


            TextView timeItem = new TextView(this);
            timeItem.setText("sssss");

            singleContent.addView(headImg);
            singleContent.addView(textContain);
            singleContent.addView(timeItem);

            mainContainer.addView(singleContent);
        }


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



