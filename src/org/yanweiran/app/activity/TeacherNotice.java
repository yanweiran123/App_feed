package org.yanweiran.app.activity;


import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.MyWidget.RTPullListView;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.NoticeEntity;
import org.yanweiran.app.Singleton.PublicType;
import org.yanweiran.app.Singleton.SchoolClass;
import org.yanweiran.app.Singleton.User;

import org.yanweiran.app.adapter.TweetAdapter;
import org.yanweiran.app.dialog.DialogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class  TeacherNotice extends Activity
    {
        private static final int INTERNET_FAILURE = -1;
        private static final int LOAD_SUCCESS = 1;
        private static final int LOAD_MORE_SUCCESS = 3;
        private static final int NO_MORE_INFO = 4;
        private static final int LOAD_NEW_INFO = 5;
        private ProgressBar moreProgressBar;
        private ImageView pull;

        JSONArray arrayClass=null;
        ArrayList<NoticeEntity> noticeEntities = new ArrayList<NoticeEntity>();
        private RTPullListView mListView;
        private ImageLoader mImageLoader;
        List<Map<String,Object>> moreList;
        private  String[] tvItem;
        private RelativeLayout update;
        private TextView lastUpdate;
        private ImageButton write;
        private ImageButton back;
        TweetAdapter mAdapter;
        private static  String MAX_ID="0";
        private  static String MIN_ID ="0";
        private SharedPreferences pref;
        private Gson gson;
        private  static final int TWEETDETAIL=7;
        private static  final int PUBLICNEW=8;
        @Override
        public void  onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            LinearLayout mainLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.notice_tweet,null);
            setContentView(mainLayout);
            tvItem = new String[]{"老师通知","班级照片"};
            write = (ImageButton)findViewById(R.id.write);
            pull = (ImageView)findViewById(R.id.pull);
            pull.setVisibility(View.GONE);
            final TextView tvTitle = (TextView)findViewById(R.id.title);
            tvTitle.setText("老师通知");
            back =(ImageButton)findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(TeacherNotice.this,Tile.class);
                    TeacherNotice.this.startActivity(intent);
                    TeacherNotice.this.finish();
                }
            });
            if(User.getUser().tag.equals("0")){
                write.setVisibility(View.GONE);
            }
            write.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(TeacherNotice.this,PublicNews.class);
                    PublicType.getPublicType().type="1";
                    PublicType.getPublicType().Detail_TYPE=1;
                    TeacherNotice.this.startActivityForResult(intent, PUBLICNEW);

                }
            });
            mListView = (RTPullListView)findViewById(R.id.noticeContainer);

            mImageLoader=ImageLoader.getInstance();
            gson = new Gson();
            update =  (RelativeLayout)findViewById(R.id. head_contentLayout);
            lastUpdate = (TextView)findViewById(R.id.head_lastUpdatedTextView);
            lastUpdate.setText(getResources().getString(R.string.updating) + new Date().toLocaleString());
//          mListView.setOnScrollListener(new PauseOnScrollListener(mImageLoader,true,true));

            initData();

            /**
             * 请求Http响应
             * */

            /*获取更多监听器*/
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.list_footview, null);
            RelativeLayout footerView =(RelativeLayout) view.findViewById(R.id.list_footview);
            moreProgressBar = (ProgressBar) view.findViewById(R.id.footer_progress);
            mListView.addFooterView(footerView);
            mListView.setOnRefreshListener(new RTPullListView.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                pullData();
                                Thread.sleep(2000);
                              //  myHandler.sendEmptyMessage(LOAD_NEW_INFO);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moreProgressBar.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getMore();
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
        }

        private Handler myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case LOAD_MORE_SUCCESS:
                        moreProgressBar.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                        mListView.setSelectionfoot();
                        break;

                    case LOAD_NEW_INFO:
                       mAdapter.notifyDataSetChanged();
                       mListView.onRefreshComplete();
                        break;
                    default:
                        break;
                }
            }

        };




        public  void initData()
        {
            pref = TeacherNotice.this.getSharedPreferences("TeacherNoticeMemory", 0);
            MAX_ID = pref.getString("TNoticeMaxId","").equals("")?"0":pref.getString("TNoticeMinId","");
            MIN_ID = pref.getString("TNoticeMinId","").equals("")?"0":pref.getString("TNoticeMinId", "");
            if(MAX_ID.equals("0")){
                String JSONDataUrl = BaseUrl.BASE_URL+"feedmax.php?token="+User.getUser().token+"&tag=0&maxid=0&tz=1";
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,JSONDataUrl,null,
                    new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                if(jsonObject.getInt("status")==1)
                                {
                                    update.setVisibility(View.GONE);
                                    pref.edit().putString("TNoticeMaxId",jsonObject.getString("maxid")).commit();
                                    pref.edit().putString("TNoticeMinId", jsonObject.getString("minid")).commit();
                                    MAX_ID = jsonObject.getString("maxid");
                                    MIN_ID = jsonObject.getString("minid");
                                }
                                SchoolClass.getSchoolClass().messageArray = jsonObject.getJSONArray("lists");
                                arrayClass = SchoolClass.getSchoolClass().messageArray;
                                User.getUser().msgNum = arrayClass.length();
                                int msgNum =arrayClass.length();
                                for(int i=0;i< msgNum;i++)
                                {
                                    NoticeEntity noticeEntity = new NoticeEntity();
                                    noticeEntity.setTid(arrayClass.getJSONObject(i).getString("tid"));
                                    noticeEntity.setName(arrayClass.getJSONObject(i).getString("name")) ;
                                    noticeEntity.setSendTime(arrayClass.getJSONObject(i).getString("time"));
                                    noticeEntity.setMsgContent(arrayClass.getJSONObject(i).getString("message"));
                                    noticeEntity.setHeadImgUrl(arrayClass.getJSONObject(i).getString("headimg"));
                                    noticeEntity.setReplyNum(arrayClass.getJSONObject(i).getString("reply_num"));
                                    noticeEntity.setAppre(arrayClass.getJSONObject(i).getString("zan"));
                                    noticeEntity.setIsZan(arrayClass.getJSONObject(i).getInt("iszan"));
                                    noticeEntity.setTag(arrayClass.getJSONObject(i).getInt("tag"));
                                    noticeEntity.setS_photo1(arrayClass.getJSONObject(i).getString("s_photo1"));
                                    noticeEntity.setS_photo2(arrayClass.getJSONObject(i).getString("s_photo2"));
                                    noticeEntity.setS_photo3(arrayClass.getJSONObject(i).getString("s_photo3"));
                                    noticeEntity.setB_photo1(arrayClass.getJSONObject(i).getString("b_photo1"));
                                    noticeEntity.setB_photo2(arrayClass.getJSONObject(i).getString("b_photo2"));
                                    noticeEntity.setB_photo3(arrayClass.getJSONObject(i).getString("b_photo3"));
                                    if(!noticeEntity.getS_photo1().equals(""))
                                    {
                                           noticeEntity.setImgNum(1);
                                    }
                                    if (!noticeEntity.getS_photo2().equals("")){
                                        noticeEntity.setImgNum(2);
                                    }
                                    if(!noticeEntity.getS_photo3().equals("")){
                                        noticeEntity.setImgNum(3);
                                    }
                                    noticeEntities.add(noticeEntity);
                                }
                                String json = gson.toJson(noticeEntities);
                                pref.edit().putString("TNoticeEntityList", json).commit();
                                mAdapter = new TweetAdapter(noticeEntities,TeacherNotice.this,mImageLoader);
                                mListView.setAdapter(mAdapter);
                                mListView.setOnItemClickListener(new MyOnItemClickListerer());
                                if(User.getUser().notifi==1){
                                    pullData();
                                    User.getUser().notifi=0;
                                }
                            }
                            catch (JSONException ex)
                            {
                                ex.printStackTrace();
                                DialogUtil.showDialog(TeacherNotice.this,ex.toString());
                            }
                        }
                    },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }
            });
            requestQueue.add(jsonObjectRequest);
            }else {
                update.setVisibility(View.GONE);
                Gson gson = new Gson();
                String json = pref.getString("TNoticeEntityList", "");
                noticeEntities = gson.fromJson(json,new TypeToken<ArrayList<NoticeEntity>>(){}.getType());
                mAdapter = new TweetAdapter(noticeEntities,TeacherNotice.this,mImageLoader);
                mListView.setOnItemClickListener(new MyOnItemClickListerer());
                mListView.setAdapter(mAdapter);
                if(User.getUser().notifi==1){
                    pullData();
                    User.getUser().notifi=0;
                }
            }
          }

        public  void  pullData(){
            String JSONDataUrl = BaseUrl.BASE_URL+"feedmax.php?token="+User.getUser().token+"&tag=0&tz=1"+"&maxid="+MAX_ID;
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,JSONDataUrl,null,
                    new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {

                                if(jsonObject.getInt("status")==1)
                                {
                                    update.setVisibility(View.GONE);
                                    myHandler.sendEmptyMessage( LOAD_NEW_INFO);
                                    MAX_ID = jsonObject.getString("maxid");
                                }

                                SchoolClass.getSchoolClass().messageArray = jsonObject.getJSONArray("lists");
                                arrayClass = SchoolClass.getSchoolClass().messageArray;
                                User.getUser().msgNum = arrayClass.length();
                                int msgNum =arrayClass.length();
                                for(int i=msgNum;i> 0;i--)
                                {
                                    NoticeEntity noticeEntity = new NoticeEntity();
                                    noticeEntity.setTid(arrayClass.getJSONObject(i-1).getString("tid"));
                                    noticeEntity.setName(arrayClass.getJSONObject(i-1).getString("name")) ;
                                    noticeEntity.setSendTime(arrayClass.getJSONObject(i-1).getString("time"));
                                    noticeEntity.setMsgContent(arrayClass.getJSONObject(i-1).getString("message"));
                                    noticeEntity.setHeadImgUrl(arrayClass.getJSONObject(i-1).getString("headimg"));
                                    noticeEntity.setReplyNum(arrayClass.getJSONObject(i-1).getString("reply_num"));
                                    noticeEntity.setAppre(arrayClass.getJSONObject(i-1).getString("zan"));
                                    noticeEntity.setIsZan(arrayClass.getJSONObject(i-1).getInt("iszan"));
                                    noticeEntity.setTag(arrayClass.getJSONObject(i-1).getInt("tag"));
                                    noticeEntity.setS_photo1(arrayClass.getJSONObject(i-1).getString("s_photo1"));
                                    noticeEntity.setS_photo2(arrayClass.getJSONObject(i-1).getString("s_photo2"));
                                    noticeEntity.setS_photo3(arrayClass.getJSONObject(i-1).getString("s_photo3"));
                                    if(!noticeEntity.getS_photo1().equals(""))
                                    {
                                        noticeEntity.setImgNum(1);
                                    }
                                    if (!noticeEntity.getS_photo2().equals("")){
                                        noticeEntity.setImgNum(2);
                                    }
                                    if(!noticeEntity.getS_photo3().equals("")){
                                        noticeEntity.setImgNum(3);
                                    }
                                    noticeEntities.add(0,noticeEntity);
                                }
                                    String json = gson.toJson(noticeEntities);
                                    pref.edit().putString("TNoticeEntityList", json).commit();
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
        }


        public  void getMore(){
            String JSONDataUrl = BaseUrl.BASE_URL+"feedmax.php?token="+User.getUser().token+"&maxid="+MIN_ID+"&tag=1&tz=0";
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,JSONDataUrl,null,
                    new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {

                                if(jsonObject.getInt("status")==1)
                                {
                                    myHandler.sendEmptyMessage(LOAD_MORE_SUCCESS);
                                    pref.edit().putString("TNoticeMinID", jsonObject.getString("minid")).commit();
                                    MIN_ID = jsonObject.getString("minid");
                                }
                                SchoolClass.getSchoolClass().messageArray = jsonObject.getJSONArray("lists");
                                arrayClass = SchoolClass.getSchoolClass().messageArray;
                                User.getUser().msgNum = arrayClass.length();
                                int msgNum =arrayClass.length();
                                for(int i=0;i< msgNum;i++)
                                {
                                    NoticeEntity noticeEntity = new NoticeEntity();
                                    noticeEntity.setTid(arrayClass.getJSONObject(i).getString("tid"));
                                    noticeEntity.setName(arrayClass.getJSONObject(i).getString("name")) ;
                                    noticeEntity.setSendTime(arrayClass.getJSONObject(i).getString("time"));
                                    noticeEntity.setMsgContent(arrayClass.getJSONObject(i).getString("message"));
                                    noticeEntity.setHeadImgUrl(arrayClass.getJSONObject(i).getString("headimg"));
                                    noticeEntity.setReplyNum(arrayClass.getJSONObject(i).getString("reply_num"));
                                    noticeEntity.setAppre(arrayClass.getJSONObject(i).getString("zan"));
                                    noticeEntity.setIsZan(arrayClass.getJSONObject(i).getInt("iszan"));
                                    noticeEntity.setTag(arrayClass.getJSONObject(i).getInt("tag"));
                                    noticeEntity.setS_photo1(arrayClass.getJSONObject(i).getString("s_photo1"));
                                    noticeEntity.setS_photo2(arrayClass.getJSONObject(i).getString("s_photo2"));
                                    noticeEntity.setS_photo3(arrayClass.getJSONObject(i).getString("s_photo3"));
                                    noticeEntity.setB_photo1(arrayClass.getJSONObject(i).getString("b_photo1"));
                                    noticeEntity.setB_photo2(arrayClass.getJSONObject(i).getString("b_photo2"));
                                    noticeEntity.setB_photo3(arrayClass.getJSONObject(i).getString("b_photo3"));
                                    if(!noticeEntity.getS_photo1().equals(""))
                                    {
                                        noticeEntity.setImgNum(1);
                                    }
                                    if (!noticeEntity.getS_photo2().equals("")){
                                        noticeEntity.setImgNum(2);
                                    }
                                    if(!noticeEntity.getS_photo3().equals("")){
                                        noticeEntity.setImgNum(3);
                                    }
                                    noticeEntities.add(noticeEntity);
                                }
                                String json = gson.toJson(noticeEntities);
                                pref.edit().putString("TNoticeEntityList", json).commit();
                            }
                            catch (JSONException ex)
                            {
                                ex.printStackTrace();
                                Log.e("@@@@@@@@@@@@@@@@@@@@@@@@@",ex.toString());
                            }
                        }
                    },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
        @Override
        protected void onDestroy() {
            super.onDestroy();
            if (mImageLoader!=null) {
                mImageLoader.clearMemoryCache();
                mImageLoader.clearDiscCache();
            }
        }
        @Override
        public  boolean onKeyDown(int keyCode,KeyEvent keyEvent)
        {
            if(keyCode == keyEvent.KEYCODE_BACK)
            {
                Intent intent = new Intent();
                intent.setClass(this,Tile.class);
                startActivity(intent);
                finish();
            }
            return  false;
        }


        public class MyOnItemClickListerer implements AdapterView.OnItemClickListener
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NoticeEntity msgEntity = noticeEntities.get(i-1);
                PublicType.getPublicType().POSITION = i;
                Bundle data = new Bundle();
                data.putSerializable("singleMsg", msgEntity);
                PublicType.getPublicType().TweetZan = msgEntity.getAppre();
                PublicType.getPublicType().TweetIsZan = msgEntity.getIsZan();
                PublicType.getPublicType().TweetComm = msgEntity.getReplyNum();
                Intent intent = new Intent();
                intent.putExtras(data);
                intent.setClass(TeacherNotice.this,TweetDetail.class);
                PublicType.getPublicType().Detail_TYPE =1;
                TeacherNotice.this.startActivityForResult(intent, TWEETDETAIL);
            }
        }


        @Override
        protected   void onActivityResult(int requestCode, int resultCode, Intent intent)
        {
            switch (requestCode){
                case PUBLICNEW:
                    switch (resultCode){
                        case 1:
//                            NoticeEntity noticeEntity = (NoticeEntity)intent.getSerializableExtra("notice");
//                            noticeEntities.add(0,noticeEntity);
//                            mAdapter.notifyDataSetChanged();
//                            String json = gson.toJson(noticeEntities);
//                            pref.edit().putString("TweetEntityList", json).commit();
                            pullData();
                            break;
                        case 0:
                            break;
                    }
                    break;
                case  TWEETDETAIL:
                    NoticeEntity msgEntity = noticeEntities.get(PublicType.getPublicType().POSITION -1);
                    msgEntity.setAppre(PublicType.getPublicType().TweetZan);
                    msgEntity.setIsZan(PublicType.getPublicType().TweetIsZan);
                    msgEntity.setReplyNum(PublicType.getPublicType().TweetComm);
                    mAdapter.notifyDataSetChanged();
                    String json = gson.toJson(noticeEntities);
                    pref.edit().putString("TweetEntityList", json).commit();
                    break;
            }
        }
        @Override
        public void onResume() {
            super.onResume();
            MobclickAgent.onPageStart("11111"); //统计页面
            MobclickAgent.onResume(this);
            TCAgent.onPageStart(this, "老师通知");
            TCAgent.onResume(this);

        }
        @Override
        public void onPause() {
            super.onPause();
            MobclickAgent.onPageEnd("老师通知");
            MobclickAgent.onPause(this);
            TCAgent.onPageEnd(this,"老师通知");
            TCAgent.onPause(this);
        }
      }



