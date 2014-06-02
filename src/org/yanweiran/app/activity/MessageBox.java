package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.NoticeEntity;
import org.yanweiran.app.Singleton.PublicType;
import org.yanweiran.app.Singleton.RecentTalkEntity;
import org.yanweiran.app.Singleton.RecipeEntity;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.adapter.RecentTalkAdapter;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.dialog.DialogUtil2;
import org.yanweiran.app.dialog.PopMenu1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenov on 13-12-28.
 */
public class MessageBox extends Activity
    {
        List<Map<String,Object>> moreList;
        private ArrayList<RecentTalkEntity> recentTalkEntities = new ArrayList<RecentTalkEntity>();
        private  ListView mListView;
        private ImageLoader imageLoader;
        private ImageButton back;
        Dialog dialog;


        @Override
        public void onCreate(Bundle savedInstance)
        {
                    super.onCreate(savedInstance);
                    LinearLayout mainLinearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.msgbox,null);
                    DemoApplication.getInstance().addActivity(this);
                    setContentView(mainLinearLayout);
                    imageLoader = ImageLoader.getInstance();
                    initImageLoader(this);
                    //application = (MyApplication) this.getApplicationContext();
                    initView();
                    initData();
                    User.getUser().news =0;
            if(User.getUser().tag.equals("1")){
                User.getUser().classEntityList.get(PublicType.getPublicType().classPosition).setClassNew(0);
            }
        }

        public  void initView()
        {
            mListView = (ListView)findViewById(R.id.msgContainer);
            dialog = DialogUtil2.createLoadingDialog(this,"刷新中...");
            dialog.show();
            back = (ImageButton)findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(MessageBox.this,Tile.class);
                    startActivity(intent);
                    finish();
                }
            });

        }



        public  void initData()
        {
            /**
             * 请求数据格式
             * */
            String jsonDataUrl = BaseUrl.BASE_URL+ "inbox.php?"+"token="+User.getUser().token;
            RequestQueue  requestQueue = Volley.newRequestQueue(MessageBox.this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try
                            {
                                if(jsonObject.getInt("status")==1){
                                    dialog.dismiss();

                                JSONArray talkMessList = jsonObject.optJSONArray("talkmess");
                                int num=talkMessList.length();
                                for(int i=0;i<num;i++)
                                {
                                    RecentTalkEntity recentTalkEntity = new RecentTalkEntity();
                                    recentTalkEntity.setFid(talkMessList.getJSONObject(i).getInt("uid"));
                                    recentTalkEntity.setMsgName(talkMessList.getJSONObject(i).getString("name"));
                                    recentTalkEntity.setMsgHead(talkMessList.getJSONObject(i).optString("headimg"));
                                    recentTalkEntity.setLastTalk(talkMessList.getJSONObject(i).getString("mess"));
                                    recentTalkEntity.setMsgTime(talkMessList.getJSONObject(i).getString("time"));
                                    recentTalkEntity.setIdentity(talkMessList.getJSONObject(i).optInt("t"));
                                    recentTalkEntity.setStatus(talkMessList.getJSONObject(i).optInt("status"));
                                    recentTalkEntities.add(recentTalkEntity);
                                }
                                JSONArray noTalkPeople = jsonObject.getJSONArray("talk_names");
                                int num2 = noTalkPeople.length();
                                for (int i = 0;i<num2;i++)
                                {
                                    RecentTalkEntity recentTalkEntity = new RecentTalkEntity();
                                    recentTalkEntity.setFid(noTalkPeople.getJSONObject(i).getInt("uid"));
                                    recentTalkEntity.setMsgName(noTalkPeople.getJSONObject(i).getString("name"));
                                    recentTalkEntity.setMsgHead(noTalkPeople.getJSONObject(i).optString("headimg"));
                                    recentTalkEntity.setLastTalk("点击创建对话....");
                                    recentTalkEntity.setIdentity(noTalkPeople.getJSONObject(i).optInt("t"));
                                    recentTalkEntity.setMsgTime("");
                                    recentTalkEntity.setStatus(noTalkPeople.getJSONObject(i).optInt("status",0));
                                    recentTalkEntities.add(recentTalkEntity);
                                }
                                RecentTalkAdapter mAdapter = new RecentTalkAdapter(recentTalkEntities,MessageBox.this,imageLoader);
                                mListView.setAdapter(mAdapter);
                                mListView.setOnItemClickListener(new RecentItemClickListener());
                            }else {
                                    Intent intent = new Intent();
                                    intent.setClass(MessageBox.this,Login.class);
                                    MessageBox.this.startActivity(intent);
                                    MessageBox.this.finish();
                                }
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

        public static void initImageLoader(Context context) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration
                    .Builder(context)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .discCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .build();
            ImageLoader.getInstance().init(config);
        }






       public class  RecentItemClickListener implements AdapterView.OnItemClickListener
       {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    RecentTalkEntity recentTalkEntity = recentTalkEntities.get(i);
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    data.putSerializable("chatObject",recentTalkEntity);
                    intent.putExtras(data);
                    intent.setClass(MessageBox.this,ChatPrivate.class);
                    startActivityForResult(intent,1);
                    MessageBox.this.finish();
                    MessageBox.this.overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
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
            MobclickAgent.onPageStart("消息箱"); //统计页面
            MobclickAgent.onResume(this);
            TCAgent.onPageStart(this,"消息箱");
            TCAgent.onResume(this);

        }
        @Override
        public void onPause() {
            super.onPause();
            MobclickAgent.onPageEnd("消息箱");
            MobclickAgent.onPause(this);
            TCAgent.onPageEnd(this,"消息箱");
            TCAgent.onPause(this);
        }

    }



