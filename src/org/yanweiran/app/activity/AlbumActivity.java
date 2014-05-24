package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import org.yanweiran.app.Singleton.PhotoListEntity;
import org.yanweiran.app.Singleton.PublicType;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.adapter.PhotoListAdapter;
import org.yanweiran.app.dialog.PopMenuMain;

import java.util.ArrayList;

/**
 * Created by lenov on 14-2-10.
 */
public class AlbumActivity extends Activity {

    private ImageLoader imageLoader;
    private GridView mGridView;
    private  String[] tvItem;
    private ImageButton back;
    private static final int SINGLE_PHOTO=2;
    private static int POSITION;
    private ArrayList<PhotoListEntity> photoListEntities = new ArrayList<PhotoListEntity>();
    private  PhotoListAdapter mPhotoListAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photolist);
        DemoApplication.getInstance().addActivity(this);
        imageLoader = ImageLoader.getInstance();
        tvItem = new String[]{"幼儿园的新鲜事","老师通知"};
        initImageLoader(this);
        back= (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumActivity.this.finish();
            }
        });
        mGridView = (GridView)findViewById(R.id.grid0);
        mGridView.setOnScrollListener(new PauseOnScrollListener(imageLoader,true,true));
        initData();
    }

    public  void initData()
    {
        String jsonDataUrl = "http://app.demkids.com/app_feed/photos.php?"+"token="+ User.getUser().token;
        RequestQueue    requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest   jsonObjectRequest =new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        try
                        {
                            JSONArray    jsonArray   =   jsonObject.getJSONArray("lists");
                            Integer  num = jsonArray.length();

                            for(Integer i=0;i<num;i++)
                            {
                                PhotoListEntity photoListEntity = new PhotoListEntity();
                                photoListEntity.setTid(jsonArray.getJSONObject(i).getString("tid"));
                                photoListEntity.setHeadImgUrl(jsonArray.getJSONObject(i).getString("headimg"));
                                photoListEntity.setContent(jsonArray.getJSONObject(i).getString("c"));
                                photoListEntity.setIsZan(jsonArray.getJSONObject(i).optInt("iszan",0));
                                photoListEntity.setCommentNum(jsonArray.getJSONObject(i).optString("reply_num","0"));
                                photoListEntity.setZanNum(jsonArray.getJSONObject(i).optString("zan","0"));
                                photoListEntity.setTag(jsonArray.getJSONObject(i).optInt("tag",0));
                                photoListEntity.setsPhotoUrl(jsonArray.getJSONObject(i).getString("s_photo"));
                                photoListEntity.setbPhotoUrl(jsonArray.getJSONObject(i).getString("b_photo"));
                                photoListEntity.setTime(jsonArray.getJSONObject(i).getString("time"));
                                photoListEntity.setName(jsonArray.getJSONObject(i).getString("name"));
                                photoListEntity.setS_photo1(jsonArray.getJSONObject(i).optString("s_photo1",jsonArray.getJSONObject(i).getString("s_photo")));
                                photoListEntity.setS_photo2(jsonArray.getJSONObject(i).optString("s_photo2"));
                                photoListEntity.setS_photo3(jsonArray.getJSONObject(i).optString("s_photo3"));
                                photoListEntity.setB_photo1(jsonArray.getJSONObject(i).optString("b_photo1",jsonArray.getJSONObject(i).getString("b_photo")));
                                photoListEntity.setB_photo2(jsonArray.getJSONObject(i).optString("b_photo2"));
                                photoListEntity.setB_photo3(jsonArray.getJSONObject(i).optString("b_photo3"));
                                photoListEntities.add(photoListEntity);
                            }
                            mPhotoListAdapter = new PhotoListAdapter(photoListEntities,AlbumActivity.this,imageLoader);
                            mGridView.setAdapter(mPhotoListAdapter);
                            mGridView.setOnItemClickListener( new MyOnItemClickListener());
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

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }




    class  MyOnItemClickListener   implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            POSITION =i;
            PhotoListEntity photoListEntity  = photoListEntities.get(i);
            Bundle data = new Bundle();
            data.putSerializable("singlePhoto",photoListEntity);
            Intent intent = new Intent();
            intent.setClass(AlbumActivity.this,BigPhoto.class);
            intent.putExtras(data);
            startActivityForResult(intent,SINGLE_PHOTO);
        }
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent intent){
        switch (requestCode){
            case SINGLE_PHOTO:
                PhotoListEntity photoListEntity = photoListEntities.get(POSITION);
                photoListEntity.setIsZan(PublicType.getPublicType().TweetIsZan);
                photoListEntity.setZanNum(PublicType.getPublicType().TweetZan);
                mPhotoListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public  boolean onKeyDown(int keyCode,KeyEvent keyEvent)
    {
        if(keyCode == keyEvent.KEYCODE_BACK)
        {
//            Intent intent = new Intent();
//            intent.setClass(this,Tile.class);
//            startActivity(intent);
              finish();
        }
        return  false;
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("班级相册"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "班级相册");
        TCAgent.onResume(this);

    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("班级相册");
        MobclickAgent.onPause(this);
        TCAgent.onPageStart(this,"班级相册");
        TCAgent.onPause(this);
    }

}
