package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pixate.Pixate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.HttpPackage.HttpUtil;
import org.yanweiran.app.Singleton.SchoolClass;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.clicklistener.BottomMenuListener;


import java.lang.ref.SoftReference;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;


/**
 * Created by lenov on 13-12-20.
 */
public class TeacherNotice extends Activity {

          int width;
          int height;







        @Override
        public void onCreate(Bundle savedInstance)
        {
            super.onCreate(savedInstance);
            // Initialize pixate for the Activity
            Pixate.setLicenseKey("MP7VF-CAGHI-2NE0T-UG6B0-75HQM-8SVI6-D23SG-AQLHH-MERVK-51GAB-NPG96-GPMUT-CR8VB-UEALM-H6JI7-L0\n","shijiezhang@ieee.org"
            );
            Pixate.init(this);
            /*===========================
            * 得到屏幕的大小
            * ===========================*/
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
             width = size.x;
             height = size.y;




            /**
             * 请求Http响应
             * */
            String BASE_URL = HttpUtil.BASE_URL;

            String JSONDataUrl = BASE_URL +"feed.php?"+"token="+ User.getUser().token+"&page=1"+"&tag="+User.getUser().tag+"&classid="+ SchoolClass.getSchoolClass().classId;
            //  String JSONDataUrl = "http://115.28.46.167:83/app_feed/feed.php?token=dcf34856705df9db50aad220c1b301cd&page=1&tag=1&classid=108";
            // String JSONDataUrl =" http://115.28.46.167:83/app_feed/login.php?email=sunyibin&pwd=4184&tag=1";
            RequestQueue requestQueue = Volley.newRequestQueue(TeacherNotice.this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,JSONDataUrl,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                            try {
                                //DialogUtil.showDialog(TeacherNotice.this,jsonObject.toString());
                                SchoolClass.getSchoolClass().messageArray = jsonObject.getJSONArray("lists");
                                JSONObject jsonObjectList;
                                JSONArray arrayClass = SchoolClass.getSchoolClass().messageArray;
                                for(int count=0;count<arrayClass.length();count++)
                                {
                                    try
                                    {
                                        jsonObjectList = arrayClass.getJSONObject(count);
                                        noiticeAddContent(jsonObjectList.getString("headimg"),jsonObjectList.getString("message"),jsonObjectList.getString("name"),jsonObjectList.getString("time"),jsonObjectList.getString("reply_num"),jsonObjectList.getString("zan"));
                                    }
                                    catch (JSONException ex)
                                    {

                                    }
                                }

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


             /*加载主题框架*/
            LinearLayout mainLinearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.teachernotice,null);
            setContentView(mainLinearLayout);

             /*底部按钮的触发事件*/
            new BottomMenuListener().clickTurn(this);
        }




                /*对单个消息进行渲染*/
            public void  noiticeAddContent(String imgSource, String msg,String name,String time,String commentNum,String appreNum)
            {
                LinearLayout main_container = (LinearLayout)findViewById(R.id.singleContent);
                /*单个内容*/
                LinearLayout single_content = new LinearLayout(this);
                Pixate.setStyleClass(single_content,"singleContent");
                single_content.setOrientation(LinearLayout.VERTICAL);

                /*设置周围边距*/
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width*96/100,LinearLayout.LayoutParams.WRAP_CONTENT);
                int sin_width = width*96/100;
                lp.setMargins(0,height/200,0,0);
                single_content.setLayoutParams(lp);
                single_content.setPadding(0,5,0,0);
                /* 单个内容的第一行第一列*/
                LinearLayout firstR = new LinearLayout(this);
                firstR.setOrientation(LinearLayout.HORIZONTAL);
                /*单个内容的头像*/
                ImageView head_photo = new ImageView(this);
                loadImageByVolley(head_photo,imgSource);
                firstR.addView(head_photo);
                head_photo.setLayoutParams(new LinearLayout.LayoutParams(width/10, height/20));
                head_photo.setScaleType(ImageView.ScaleType.CENTER);
                /*第一行第二列*/
                LinearLayout firstR_2c = new LinearLayout(this);
                firstR_2c.setOrientation(LinearLayout.VERTICAL);
                TextView timeLine = new TextView(this);
                TextView teacher_name = new TextView(this);
                Pixate.setStyleClass(teacher_name,"teacherName");
                teacher_name.setText(name);;
                timeLine.setText(time);
                firstR_2c.addView(teacher_name);
                firstR_2c.addView(timeLine);
                firstR.addView(firstR_2c);

                /*第二行内容*/
                TextView get_text =new TextView(this);
                Pixate.setStyleClass(get_text,"getText");
                TextView  category = new TextView(this);
                View line =new View(this);
                LinearLayout.LayoutParams draw_line = new LinearLayout.LayoutParams(sin_width*96/100,1);
                draw_line.setMargins(sin_width*2/100,2,0,2);
                line.setLayoutParams(draw_line);
                Pixate.setStyleClass(line,"line");

                 Pixate.setStyleClass(category,"category");
                LinearLayout.LayoutParams cate=new LinearLayout.LayoutParams(sin_width*20/100,LinearLayout.LayoutParams.WRAP_CONTENT);
                category.setText("一周食谱");
                category.setLayoutParams(cate);
                get_text.setText(msg);
                LinearLayout secondR = new LinearLayout(this);
                LinearLayout.LayoutParams text=new LinearLayout.LayoutParams(sin_width*97/100,LinearLayout.LayoutParams.WRAP_CONTENT);
                text.setMargins(sin_width*2/100,0,0,0);
                secondR.setLayoutParams(text);
                secondR.setOrientation(LinearLayout.VERTICAL);

                secondR.addView(line);
                secondR.addView(get_text);
                secondR.addView(category);


                /*第三行内容*/
                LinearLayout thirdR= new LinearLayout(this);
                thirdR.setOrientation(LinearLayout.HORIZONTAL);
                final TextView comment = new TextView(this);
                final TextView appreciate = new TextView(this);
                LinearLayout.LayoutParams lay_com=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1);
                LinearLayout.LayoutParams lay_appr=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1);
                comment.setText("评论"+commentNum);
                appreciate.setText("赞"+appreNum);
                comment.setGravity(Gravity.CENTER);
                appreciate.setGravity(Gravity.CENTER);
                Pixate.setStyleClass(comment, "thirdR");
                Pixate.setStyleClass(appreciate, "thirdR");
                comment.setLayoutParams(lay_com);
                appreciate.setLayoutParams(lay_appr);
                thirdR.addView(comment);
                thirdR.addView(appreciate);

                /*将每行内容添加到单个内容框架中*/
                single_content.addView(firstR);
                single_content.addView(secondR);
                single_content.addView(thirdR);

                /*将每条内容加到中间层*/
                main_container.addView(single_content);
            }
            /*加载函数*/
        public void loadImageByVolley(ImageView imageView,String imgUrl){
            String imageUrl=imgUrl;
           RequestQueue requestQueue = Volley.newRequestQueue(this);
            final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
            ImageLoader.ImageCache imageCache = new ImageCache() {
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
            ImageListener listener = ImageLoader.getImageListener(imageView, R.drawable.head_photo,R.drawable.head_photo);
            imageLoader.get(imageUrl, listener);
        }


}



