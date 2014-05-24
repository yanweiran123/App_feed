package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.AssitEntity;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.NoticeEntity;
import org.yanweiran.app.Singleton.RelativeCommentEntity;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.adapter.RelativeCommentAdapter;
import org.yanweiran.app.dialog.DialogUtil2;

import java.util.ArrayList;

/**
 * Created by lenov on 13-12-28.
 */
public class RelativeComment extends Activity{
    private ArrayList<RelativeCommentEntity>  commentEntities= new ArrayList<RelativeCommentEntity>();
    private ImageLoader mImageLoader;
    private ListView mListView;
    private ImageButton back;
    private Dialog dialog;
    private static int TWEET_DETAI=3;
    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        LinearLayout mainLinearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.relativecomment,null);
        setContentView(mainLinearLayout);
        dialog = DialogUtil2.createLoadingDialog(RelativeComment.this,"刷新中...");
        dialog.show();
        mImageLoader=ImageLoader.getInstance();
        mListView = (ListView)findViewById(R.id.commContainer);
        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(RelativeComment.this,Tile.class);
                startActivity(intent);
                finish();
            }
        });
        initData();
    }

    public  void initData()
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String jsongDataUrl = BaseUrl.BASE_URL+"mycommentlists.php?token="+User.getUser().token;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsongDataUrl,null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try{
                    int status = jsonObject.getInt("status");
                    {
                        if(status==1){
                            dialog.dismiss();
                        }
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("lists");
                    int num = jsonArray.length();
                    for(int i=0;i<num;i++)
                    {
                        RelativeCommentEntity commentEntity = new RelativeCommentEntity();
                        commentEntity.setName(jsonArray.getJSONObject(i).getString("name"));
                        commentEntity.setTid(jsonArray.getJSONObject(i).getString("tid"));
                        commentEntity.setTime(jsonArray.getJSONObject(i).getString("time"));
                        commentEntity.setCommContent(jsonArray.getJSONObject(i).getString("c"));
                        commentEntity.setCommWhat(jsonArray.getJSONObject(i).getString("rc"));
                        commentEntity.setImgUrl(jsonArray.getJSONObject(i).getString("headimg"));
                        commentEntities.add(commentEntity);
                    }
                    RelativeCommentAdapter mAdapter = new RelativeCommentAdapter(commentEntities,RelativeComment.this,mImageLoader);
                    mListView.setAdapter(mAdapter);
                    mListView.setOnItemClickListener(new MyOnClickListener());
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
    }




    public  class  MyOnClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                String jsongDataUrl = BaseUrl.BASE_URL+"feedone.php?token="+User.getUser().token+"&tid="+commentEntities.get(i).getTid();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsongDataUrl
                ,null,new Response.Listener<JSONObject>(){
                        @Override
                        public  void onResponse(JSONObject jsonObject){
                            try{
                                JSONObject object = jsonObject.getJSONObject("lists");
                                NoticeEntity noticeEntity = new NoticeEntity();
                                noticeEntity.setTid(object.optString("tid"));
                                noticeEntity.setName(object.getString("name")) ;
                                Log.e("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",object.getString("name"));
                                noticeEntity.setSendTime(object.getString("time"));
                                noticeEntity.setMsgContent(object.getString("message"));
                                noticeEntity.setHeadImgUrl(object.getString("headimg"));
                                noticeEntity.setReplyNum(object.getString("reply_num"));
                                noticeEntity.setAppre(object.getString("zan"));
                                noticeEntity.setIsZan(object.getInt("iszan"));
                                noticeEntity.setTag(object.getInt("tag"));
                                noticeEntity.setS_photo1(object.optString("s_photo1"));
                                noticeEntity.setS_photo2(object.optString("s_photo2"));
                                noticeEntity.setS_photo3(object.optString("s_photo3"));
                                noticeEntity.setB_photo1(object.optString("b_photo1"));
                                noticeEntity.setB_photo2(object.optString("b_photo2"));
                                noticeEntity.setB_photo3(object.optString("b_photo3"));
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
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("singleMsg",noticeEntity);
                                intent.putExtras(bundle);
                                intent.setClass(RelativeComment.this,TweetDetail.class);
                                RelativeComment.this.startActivityForResult(intent,TWEET_DETAI);
                            }catch (JSONException ex){

                            }
                        }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                requestQueue.add(jsonObjectRequest);
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
        MobclickAgent.onPageStart("我的评论"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "我的评论");
        TCAgent.onResume(this);

    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的评论");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"我的评论");
        TCAgent.onPause(this);
    }
}
