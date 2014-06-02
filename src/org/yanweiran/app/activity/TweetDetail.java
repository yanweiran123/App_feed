package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.NoticeCommentEntity;
import org.yanweiran.app.Singleton.NoticeEntity;
import org.yanweiran.app.Singleton.PublicType;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.MyWidget.RoundImageView;
import org.yanweiran.app.adapter.NoticeCommentAdapter;

import java.util.ArrayList;

/**
 * Created by lenov on 14-2-16.
 */
public class TweetDetail extends Activity {

    private ArrayList<NoticeCommentEntity> noticeCommentEntities = new ArrayList<NoticeCommentEntity>();
    private NoticeEntity msgEntity;
    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private Button btn_comm;
    private ImageButton back;
    private TextView title;
    private ListView mListView;
    private NoticeCommentAdapter mAdapter;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView teacherTag;
    private ImageView zanView;
    private TextView zanTextView;
    static  int ZAN_NUM;
    private   int IS_ZAN;
    private TextView no_comment;
    private RelativeLayout dialog;
    private TextView comm;
   private ImageButton  btn_zan;

    @Override
    public  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_detail);
        Intent intent = getIntent();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        title = (TextView)findViewById(R.id.title_name);
        if (PublicType.getPublicType().Detail_TYPE==1){
            title.setText("老师通知");
        }
        msgEntity=(NoticeEntity)intent.getSerializableExtra("singleMsg");
        imageLoader = ImageLoader.getInstance();

        int defaultImageId = R.drawable.indexicon;
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showStubImage(defaultImageId)
                .showImageForEmptyUri(defaultImageId)
                .showImageOnFail(defaultImageId)
                .build();
        mListView = (ListView)findViewById(R.id.commentList);
        mListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.tweet_detail_head,null));
        mListView.setAdapter(null);
        final TextView  txtName = (TextView)findViewById(R.id.noticeName);
        final RoundImageView headImg = (RoundImageView)findViewById(R.id.noticeHead);
        final TextView txtTime = (TextView)findViewById(R.id.noticeTime);
        final TextView txtContent = (TextView)findViewById(R.id.noticeContent);
        final TextView tv_comm = (TextView)findViewById(R.id.noticeComment);
        final TextView tv_zan = (TextView)findViewById(R.id.noticeAppre);
        dialog = (RelativeLayout)findViewById(R.id.dialogLine);
        txtName.setText(msgEntity.getName());
        txtTime.setText(msgEntity.getSendTime());
        txtContent.setText(msgEntity.getMsgContent());
        tv_comm.setText("评论"+msgEntity.getReplyNum());
        tv_zan.setText("赞"+msgEntity.getAppre());
        imageLoader.displayImage(
                msgEntity.getHeadImgUrl(),
                headImg,
                mDisplayImageOptions);
        initComment();
        btn_comm =(Button)findViewById(R.id.comment_btn);
        btn_comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle data = new Bundle();
                data.putSerializable("msgEntity",msgEntity);
                intent.setClass(TweetDetail.this,Comment.class);
                intent.putExtras(data);
                TweetDetail.this.startActivityForResult(intent,1);
            }
        });
        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TweetDetail.this.finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        });
        initView();
        }


    public  void initView(){
        imageView1= (ImageView)findViewById(R.id.s_photo1);
        imageView2= (ImageView)findViewById(R.id.s_photo2);
        imageView3= (ImageView)findViewById(R.id.s_photo3);
        teacherTag = (ImageView)findViewById(R.id.tag);
        if(!msgEntity.getS_photo1().equals(""))
        {
            imageView1.setVisibility(View.VISIBLE);
            imageLoader.displayImage(
                    msgEntity.getS_photo1(),
                    imageView1,
                    mDisplayImageOptions);
            PublicType.getPublicType().IMG_INDEX=0;
            imageView1.setOnClickListener(new ImgOnClickListener());

        }else {
            imageView1.setVisibility(View.GONE);

        }
        if (!msgEntity.getS_photo2().equals("")){
            imageView2.setVisibility(View.VISIBLE);
            imageLoader.displayImage(
                    msgEntity.getS_photo2(),
                    imageView2,
                    mDisplayImageOptions);
            PublicType.getPublicType().IMG_INDEX=1;

            imageView2.setOnClickListener(new ImgOnClickListener());
        }else {
            imageView2.setVisibility(View.GONE);

        }
        if(!msgEntity.getS_photo3().equals("")){
            PublicType.getPublicType().IMG_INDEX=2;

            imageView3.setVisibility(View.VISIBLE);
            imageLoader.displayImage(
                    msgEntity.getS_photo3(),
                    imageView3,
                    mDisplayImageOptions);
            imageView3.setOnClickListener(new ImgOnClickListener());
        }else {
            imageView3.setVisibility(View.GONE);

        }
        if(msgEntity.getTag()==0){
            teacherTag.setVisibility(View.GONE);
        }
        btn_zan  = (ImageButton)findViewById(R.id.btn_zan);
        btn_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zan();
            }
        });
        zanTextView= (TextView)findViewById(R.id.noticeAppre);
        zanTextView.setText(msgEntity.getAppre()+"赞");
        zanView = (ImageView)findViewById(R.id.isZan);
        no_comment = (TextView)findViewById(R.id.tv_no_comment);

        if(msgEntity.getIsZan()==1){
            zanView.setImageResource(R.drawable.heart1);
        }else {
            zanView.setImageResource(R.drawable.heart);
        }
        ZAN_NUM = Integer.parseInt(msgEntity.getAppre());
        IS_ZAN = msgEntity.getIsZan();
        comm = (TextView)findViewById(R.id.noticeComment);
        comm.setText(msgEntity.getReplyNum()+" 评论");

    }

        public void initComment(){
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String jsonDataUrl1 = BaseUrl.BASE_URL+"commentlists.php?"+"token="+ User.getUser().token+"&tid="+msgEntity.getTid();
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET,jsonDataUrl1,null
                    ,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try{
                        if(jsonObject.getInt("status")==1){
                            dialog.setVisibility(View.GONE);

                        JSONArray jsonArray =  jsonObject.getJSONArray("lists");
                        int num = jsonArray.length();
                        for(int i = 0;i<num;i++)
                        {
                            NoticeCommentEntity noticeCommentEntity = new NoticeCommentEntity();
                            noticeCommentEntity.setCommContent(jsonArray.getJSONObject(i).getString("c"));
                            noticeCommentEntity.setCommName(jsonArray.getJSONObject(i).getString("name"));
                            noticeCommentEntity.setCommTime(jsonArray.getJSONObject(i).getString("time"));
                            noticeCommentEntity.setHeadUrl(jsonArray.getJSONObject(i).getString("headimg"));
                            noticeCommentEntity.setRid(jsonArray.getJSONObject(i).getString("rid"));
                            noticeCommentEntity.setTag(jsonArray.getJSONObject(i).getInt("tag"));
                            noticeCommentEntity.setIsmy(jsonArray.getJSONObject(i).getInt("ismy"));
                            noticeCommentEntity.setTid(msgEntity.getTid());
                            noticeCommentEntities.add(noticeCommentEntity);
                        }
                         mAdapter = new NoticeCommentAdapter(noticeCommentEntities,TweetDetail.this,imageLoader,comm,no_comment);
                         mListView.setAdapter(mAdapter);
                        if(Integer.valueOf(msgEntity.getReplyNum())==0)
                        {
                            no_comment.setVisibility(View.VISIBLE);
                        }
                        }else {
                            Intent intent = new Intent();
                            intent.setClass(TweetDetail.this,Login.class);
                            TweetDetail.this.startActivity(intent);
                            TweetDetail.this.finish();
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
            requestQueue.add(jsonObjectRequest1);

        }
        public  void zan()
        {
            if(msgEntity.getIsZan()==1){
                msgEntity.setIsZan(0);
                IS_ZAN = 0;
                PublicType.getPublicType().TweetIsZan=0;
                ZAN_NUM  =ZAN_NUM -1;
                zanTextView.setText(ZAN_NUM+" 赞" );
                btn_zan.setImageResource(R.drawable.heart_btn);
                PublicType.getPublicType().TweetZan =Integer.toString(ZAN_NUM);
                zanView.setImageResource(R.drawable.heart);
            }else {
                msgEntity.setIsZan(1);
                IS_ZAN=1;
                PublicType.getPublicType().TweetIsZan=1;
                ZAN_NUM  = ZAN_NUM +1;
                zanTextView.setText(ZAN_NUM+" 赞" );
                PublicType.getPublicType().TweetZan =Integer.toString(ZAN_NUM);
                zanView.setImageResource(R.drawable.heart1);
                btn_zan.setImageResource(R.drawable.hearted_btn);
            }

            String dataUrl = BaseUrl.BASE_URL+"zan.php?token="+User.getUser().token+"&tid="+msgEntity.getTid();
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,dataUrl,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            JSONObject object = jsonObject;
                            try{
                                int iszan = object.getInt("iszan");
                                if (iszan==1)
                                {

                                }
                            }catch (JSONException ex)
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

    @Override
    protected   void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode){
            case 1:
                PublicType.getPublicType().TweetComm =Integer.toString(noticeCommentEntities.size()+1);
                comm.setText(noticeCommentEntities.size()+1+" 评论");
                PublicType.getPublicType().TweetComm = Integer.toString(noticeCommentEntities.size()+1);
                msgEntity.setReplyNum(Integer.toString(noticeCommentEntities.size()+1));
                NoticeCommentEntity noticeCommentEntity= (NoticeCommentEntity)data.getSerializableExtra("comment");
                noticeCommentEntities.add(0,noticeCommentEntity);
                mAdapter.notifyDataSetChanged();
                no_comment.setVisibility(View.GONE);
                break;
            case 0:
                break;
        }
    }
    public  class  ImgOnClickListener  implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("noticeEntity",msgEntity);
            intent.putExtras(bundle);
            intent.setClass(TweetDetail.this, TweetNoticeSinglePhoto.class);
            TweetDetail.this.startActivity(intent);
        }
    }
    @Override
    public  boolean onKeyDown(int keyCode,KeyEvent keyEvent)
    {
        if(keyCode == keyEvent.KEYCODE_BACK)
        {
//            Bundle bundle = new Bundle();
//            msgEntity.setIsZan(IS_ZAN);
//            msgEntity.setAppre(Integer.toBinaryString(ZAN_NUM));
//            bundle.putSerializable("notice_back", msgEntity);
//            Intent intent=new Intent();
//            intent.putExtras(bundle);
//            TweetDetail.this.setResult(1, intent);
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
        return  false;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("新鲜事详细页面"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "新鲜事详细页面");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("新鲜事详细页面");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"新鲜事详细页面");
        TCAgent.onPause(this);
    }

    }













