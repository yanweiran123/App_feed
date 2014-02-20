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
import org.yanweiran.app.Singleton.SchoolClass;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.clicklistener.BottomMenuListener;

import org.yanweiran.app.myadpter.NoticeAdapter;

public class  TeacherNotice extends Activity
    {

        JSONArray arrayClass=null;

        @Override
        public void  onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            LinearLayout mainLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.teachernotice,null);
            setContentView(mainLayout);
            final ListView mListView = (ListView)findViewById(R.id.noticeContainer);

            /**
             * 请求Http响应
             * */
            String BASE_URL = HttpUtil.BASE_URL;
            String JSONDataUrl = BASE_URL +"feed.php?"+"token="+ User.getUser().token+"&page=1"+"&tag="+User.getUser().tag+"&classid="+ SchoolClass.getSchoolClass().classId;

            // String JSONDataUrl ="http://115.28.46.167:83/app_feed/feed.php?token=ef1f51c023a1919360b9b0474608e3be";
            RequestQueue requestQueue = Volley.newRequestQueue(TeacherNotice.this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,JSONDataUrl,null,
                    new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {

                                    SchoolClass.getSchoolClass().messageArray = jsonObject.getJSONArray("lists");
                                    arrayClass = SchoolClass.getSchoolClass().messageArray;
                                    User.getUser().msgNum = arrayClass.length();
                                    int msgNum =arrayClass.length();
                                    String[] msgId = new String[msgNum];
                                    String[] name=new String[msgNum];
                                    String[] time=new String[msgNum];
                                    String[] content=new String[msgNum];
                                    String[] headImgUrl=new String[msgNum];
                                    String[] sPhotoUrl_1 = new String[msgNum];
                                    String[] sPhotoUrl_2 = new String[msgNum];
                                    String[] sPhotoUrl_3 = new String[msgNum];
                                    String[] comment = new String[msgNum];
                                    String[] appre = new String[msgNum];

                                    for(int i=0;i< msgNum;i++)
                                    {
                                        msgId[i] = arrayClass.getJSONObject(i).getString("tid");
                                        name[i] = arrayClass.getJSONObject(i).getString("name");
                                        time[i] = arrayClass.getJSONObject(i).getString("time");
                                        content[i]=arrayClass.getJSONObject(i).getString("message");
                                        headImgUrl[i] = arrayClass.getJSONObject(i).getString("headimg");
                                        comment[i] = arrayClass.getJSONObject(i).getString("reply_num");
                                        appre[i] =arrayClass.getJSONObject(i).getString("zan");
                                        sPhotoUrl_1[i]=arrayClass.getJSONObject(i).getString("s_photo1");
                                        sPhotoUrl_2[i]=arrayClass.getJSONObject(i).getString("s_photo2");
                                        sPhotoUrl_3[i]=arrayClass.getJSONObject(i).getString("s_photo3");
                                    }

                                    //mHandler.sendMessage(msg);
                                    NoticeAdapter mAdapter = new NoticeAdapter(msgId,msgNum,headImgUrl,name,time,content,sPhotoUrl_1,sPhotoUrl_2,sPhotoUrl_3,comment,appre,TeacherNotice.this);
                                    mListView.setAdapter(mAdapter);
                                //  mListView.setSelection(mAdapter.getCount() - 1);

                            }
                            catch (JSONException ex)
                            {
                                    ex.printStackTrace();
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