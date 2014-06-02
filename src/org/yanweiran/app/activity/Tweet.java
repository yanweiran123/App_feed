package org.yanweiran.app.activity;

import android.app.Activity;
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
import com.nostra13.universalimageloader.core.ImageLoader;
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
import org.yanweiran.app.dialog.PopMenuMain;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lenov on 14-1-19.
 */
public class Tweet extends Activity{

    private static final int LOAD_MORE_SUCCESS = 3;
    private static final int LOAD_NEW_INFO = 5;
    private ProgressBar moreProgressBar;
    TweetAdapter mAdapter;
    private  String[] tvItem;
    private ImageButton write;
    ArrayList<NoticeEntity> noticeEntities = new ArrayList<NoticeEntity>();
    private static int UPLOAD_NUM=0;
    JSONArray arrayClass=null;
    private ImageLoader imageLoader;
    private RTPullListView mListView;
    private RelativeLayout update;
    private TextView lastUpdate;
    private   PopMenuMain popMenu;
    private  ImageButton back;
    private static  String MAX_ID="0";
    private  static String MIN_ID ="0";
    private SharedPreferences pref;
    private Gson gson;
    private  static final int TWEETDETAIL=1;
    private static  final int PUBLICNEW=2;



    @Override
    public  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout mainLinearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.notice_tweet,null);
        setContentView(mainLinearLayout);
        update =  (RelativeLayout)findViewById(R.id. head_contentLayout);
        lastUpdate = (TextView)findViewById(R.id.head_lastUpdatedTextView);
        lastUpdate.setText(getResources().getString(R.string.updating) + new Date().toLocaleString());
        tvItem = new String[]{"幼儿园的新鲜事","班级照片"};
        mListView = (RTPullListView)findViewById(R.id.noticeContainer);
        gson = new Gson();
        imageLoader = ImageLoader.getInstance();
        initView();
        mListView.setOnRefreshListener(new RTPullListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            pullData();
                            Thread.sleep(2000);
                        //    myHandler.sendEmptyMessage(LOAD_NEW_INFO);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        popMenuMain();
        initData();


        //获取跟多监听器
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.list_footview, null);
        RelativeLayout footerView =(RelativeLayout) view.findViewById(R.id.list_footview);
        moreProgressBar = (ProgressBar) view.findViewById(R.id.footer_progress);
        mListView.addFooterView(footerView);


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
                //            myHandler.sendEmptyMessage(LOAD_MORE_SUCCESS);
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


    /*处理弹出菜单1*/
    private void popMenuMain()
    {

     popMenu = new PopMenuMain(this,(TextView)findViewById(R.id.title),2,tvItem,0,1);
        popMenu.lvPopupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                Bundle bundle =new Bundle();
                switch (position)
                {
                    case 0:
                            popMenu.pwMyPopWindow.dismiss();
                        break;
                    case 1:
                        popMenu.pwMyPopWindow.dismiss();
                        intent.setClass(Tweet.this,AlbumActivity.class);
                        Tweet.this.startActivity(intent);
                        break;

                }
            }
        });
    }


    public void initView()
    {
        write = (ImageButton)findViewById(R.id.write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Tweet.this,PublicNews.class);
                PublicType.getPublicType().type="0";
                PublicType.getPublicType().Detail_TYPE=0;
                Tweet.this.startActivityForResult(intent, PUBLICNEW);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });
        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Tweet.this,Tile.class);
                Tweet.this.startActivity(intent);
                Tweet.this.finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        });
//        mListView.setOnScrollListener(new PauseOnScrollListener(imageLoader, true, true));
    }
    public void initData()
    {
        pref = Tweet.this.getSharedPreferences("TweetMemory"+User.getUser().classid+User.getUser().email, 0);
        MAX_ID = pref.getString("TweetMaxId","").equals("")?"0":pref.getString("TweetMaxId","");
        MIN_ID = pref.getString("TweetMinId","").equals("")?"0":pref.getString("TweetMinId", "");
        if(MAX_ID.equals("0")){
        String JSONDataUrl = BaseUrl.BASE_URL+"feedmax.php?token="+User.getUser().token+"&tag=0&tz=0"+"&maxid="+MAX_ID;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.GET,JSONDataUrl,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try
                        {
                            if(jsonObject.getInt("status")==1)
                            {
                                update.setVisibility(View.GONE);
                                pref.edit().putString("TweetMaxId",jsonObject.getString("maxid")).commit();
                                pref.edit().putString("TweetMinID", jsonObject.getString("minid")).commit();
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
                                noticeEntity.setIsmy(arrayClass.getJSONObject(i).optInt("ismy",0));
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
                            pref.edit().putString("TweetEntityList", json).commit();
                            mAdapter = new TweetAdapter(noticeEntities,Tweet.this,imageLoader,pref,"TweetEntityList");
                            mListView.setOnItemClickListener(new MyOnItemClickListerer());
                            mListView.setAdapter(mAdapter);
                        }
                        catch (JSONException ex)
                        {
                            Log.e("@@@@@@@@@@@@@@@@@@@@@@@@@",ex.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        DialogUtil.showDialog(Tweet.this,"无法连接");
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }else {
            update.setVisibility(View.GONE);
            Gson gson = new Gson();
            String json = pref.getString("TweetEntityList", "");
            noticeEntities = gson.fromJson(json,new TypeToken<ArrayList<NoticeEntity>>(){}.getType());
            mAdapter = new TweetAdapter(noticeEntities,Tweet.this,imageLoader,pref,"TweetEntityList");
            mListView.setOnItemClickListener(new MyOnItemClickListerer());
            mListView.setAdapter(mAdapter);
        }
    }

    public  void  pullData(){
        String JSONDataUrl = BaseUrl.BASE_URL+"feedmax.php?token="+User.getUser().token+"&maxid="+MAX_ID+"&tag=0&tz=0";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,JSONDataUrl,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            if(jsonObject.getInt("status")==1)
                            {
                                update.setVisibility(View.GONE);
                                myHandler.sendEmptyMessage(LOAD_NEW_INFO);
                                MAX_ID = jsonObject.getString("maxid");
                                pref.edit().putString("TweetMaxId",jsonObject.getString("maxid")).commit();


                            SchoolClass.getSchoolClass().messageArray = jsonObject.getJSONArray("lists");
                            arrayClass = SchoolClass.getSchoolClass().messageArray;
                            User.getUser().msgNum = arrayClass.length();
                            int msgNum =arrayClass.length();
                            for(int i=msgNum;i>0;i--)
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
                                noticeEntity.setIsmy(arrayClass.getJSONObject(i-1).optInt("ismy",0));
                                noticeEntity.setS_photo1(arrayClass.getJSONObject(i-1).getString("s_photo1"));
                                noticeEntity.setS_photo2(arrayClass.getJSONObject(i-1).getString("s_photo2"));
                                noticeEntity.setS_photo3(arrayClass.getJSONObject(i-1).getString("s_photo3"));
                                noticeEntity.setB_photo1(arrayClass.getJSONObject(i-1).getString("b_photo1"));
                                noticeEntity.setB_photo2(arrayClass.getJSONObject(i-1).getString("b_photo2"));
                                noticeEntity.setB_photo3(arrayClass.getJSONObject(i-1).getString("b_photo3"));
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
                            mAdapter.notifyDataSetChanged();
                            String json = gson.toJson(noticeEntities);
                            pref.edit().putString("TweetEntityList", json).commit();
                        }else {
                                Intent intent = new Intent();
                                intent.setClass(Tweet.this,Login.class);
                                Tweet.this.startActivity(intent);
                                Tweet.this.finish();
                            }
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
                                pref.edit().putString("TweetMinID", jsonObject.getString("minid")).commit();
                                MIN_ID = jsonObject.getString("minid");

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
                                noticeEntity.setIsmy(arrayClass.getJSONObject(i).optInt("ismy",0));
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
                            pref.edit().putString("TweetEntityList", json).commit();
                        }else {
                                Intent intent = new Intent();
                                intent.setClass(Tweet.this,Login.class);
                                Tweet.this.startActivity(intent);
                                Tweet.this.finish();
                            }
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



 //切换到详细新鲜事的部分

     public class MyOnItemClickListerer implements AdapterView.OnItemClickListener
     {
         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                           NoticeEntity msgEntity = noticeEntities.get(i-1);
                            Bundle data = new Bundle();
                            data.putSerializable("singleMsg",msgEntity);
                            Intent intent = new Intent(Tweet.this, TweetDetail.class);
                            intent.putExtras(data);
                            PublicType.getPublicType().Detail_TYPE =0;
                            PublicType.getPublicType().POSITION = i;
                            PublicType.getPublicType().TweetZan = msgEntity.getAppre();
                            PublicType.getPublicType().TweetIsZan = msgEntity.getIsZan();
                            PublicType.getPublicType().TweetComm = msgEntity.getReplyNum();
                            Tweet.this.startActivityForResult(intent, TWEETDETAIL);
                            Tweet.this.overridePendingTransition(R.anim.slide_right_in, 0);
             }
         }




    @Override
    protected   void onActivityResult(int requestCode, int resultCode, Intent intent)
    { 
        switch (requestCode){
            case PUBLICNEW:
                switch (resultCode){
                    case 1:
//                        NoticeEntity noticeEntity = (NoticeEntity)intent.getSerializableExtra("notice");
//                        noticeEntities.add(0,noticeEntity);
//                        mAdapter.notifyDataSetChanged();
//                        String json = gson.toJson(noticeEntities);
//                        pref.edit().putString("TweetEntityList", json).commit();
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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("幼儿园新鲜事"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "幼儿园新鲜事");
        TCAgent.onResume(this);

    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("幼儿园新鲜事");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"幼儿园新鲜事");
        TCAgent.onPause(this);
    }

}
