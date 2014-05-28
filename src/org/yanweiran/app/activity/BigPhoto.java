package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.AssitEntity;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.NoticeEntity;
import org.yanweiran.app.Singleton.PhotoListEntity;
import org.yanweiran.app.Singleton.PublicType;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.dialog.DialogUtil2;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lenov on 14-3-29.
 */
public class BigPhoto extends Activity {

    private ImageButton mBtnSave;
    private Bitmap mBitmap;
    private TextView tvName;
    private RelativeLayout publisherItem;
    private TextView tvTime;
    private ImageView imageView;
    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private ImageButton back;
    private NoticeEntity noticeEntity;
    private  static final int TWEET_NOTICE_DETAIL=1;
    Dialog dialog;
    PhotoListEntity photoListEntity;
    int defaultImageId = R.drawable.fail;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bigphoto);
        DemoApplication.getInstance().addActivity(this);
        initImageLoader(this);
        dialog = DialogUtil2.createLoadingDialog(this,"加载中...");
        dialog.show();
        photoListEntity = (PhotoListEntity)getIntent().getSerializableExtra("singlePhoto");
        intiData();
         mBtnSave = (ImageButton)findViewById(R.id.savedImg);
         back = (ImageButton)findViewById(R.id.back);
         back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BigPhoto.this.finish();
            }
        });
        // 下载图片
        mBtnSave.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "图片成功保存在(/sdcard/jyt/myImage)中",
                        Toast.LENGTH_SHORT).show();
                new Thread(saveFile).start();
            }
        });
       publisherItem = (RelativeLayout)findViewById(R.id.publisherItem);
        tvName = (TextView)findViewById(R.id.tvName);
        tvTime = (TextView)findViewById(R.id.tvTime);
        imageView =(ImageView)findViewById(R.id.icon);
        imageLoader=ImageLoader.getInstance();
        mDisplayImageOptions = new DisplayImageOptions.Builder().
                showImageOnFail(defaultImageId).
                cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        imageLoader.displayImage(
                photoListEntity.getbPhotoUrl(),
                imageView,
                mDisplayImageOptions,new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    dialog.dismiss();
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        tvTime.setText(photoListEntity.getTime());
        publisherItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("singleMsg",noticeEntity);
                intent.putExtras(bundle);
                intent.setClass(BigPhoto.this,TweetDetail.class);
                startActivityForResult(intent, TWEET_NOTICE_DETAIL);
            }
        });
        tvName.setText(photoListEntity.getName());
    }


    public void intiData(){
                            noticeEntity = new NoticeEntity();
                            noticeEntity.setName(photoListEntity.getName());
                            noticeEntity.setTid(photoListEntity.getTid());
                            noticeEntity.setHeadImgUrl(photoListEntity.getHeadImgUrl());
                            noticeEntity.setMsgContent(photoListEntity.getContent());
                            noticeEntity.setAppre(photoListEntity.getZanNum());
                            noticeEntity.setIsZan(photoListEntity.getIsZan());
                            noticeEntity.setReplyNum(photoListEntity.getCommentNum());
                            noticeEntity.setTag(photoListEntity.getTag());
                            noticeEntity.setS_photo1(photoListEntity.getS_photo1());
                            noticeEntity.setS_photo2(photoListEntity.getS_photo2());
                            noticeEntity.setS_photo3(photoListEntity.getS_photo3());
                            noticeEntity.setB_photo1(photoListEntity.getB_photo1());
                            noticeEntity.setB_photo2(photoListEntity.getB_photo2());
                            noticeEntity.setB_photo3(photoListEntity.getB_photo3());
                            PublicType.getPublicType().TweetComm = photoListEntity.getCommentNum();
                            PublicType.getPublicType().TweetIsZan = photoListEntity.getIsZan();
                            PublicType.getPublicType().TweetZan = photoListEntity.getZanNum();
                     }

    @Override
    public  void onActivityResult(int requsetCode,int resultCode,Intent intent){
        switch (requsetCode){
            case TWEET_NOTICE_DETAIL:
               noticeEntity.setIsZan(PublicType.getPublicType().TweetIsZan);
               noticeEntity.setAppre(PublicType.getPublicType().TweetZan);
               noticeEntity.setReplyNum(PublicType.getPublicType().TweetComm);
               break;
        }
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .imageDownloader(new BaseImageDownloader(context))
                .build();
        ImageLoader.getInstance().init(config);
    }
        private Runnable saveFile = new Runnable() {
            @Override
            public void run() {
                try{

                        String sdStatus = Environment.getExternalStorageState();
                        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                            return;
                        }
                        URL url = new URL(photoListEntity.getbPhotoUrl());
                        InputStream is = url.openStream();
                        mBitmap = BitmapFactory.decodeStream(is);
                    String str = null;
                    Date date = null;
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
                    date = new Date();
                    str = format.format(date);
                    String fileName = "/sdcard/jyt/myImage/" + str + ".jpg";
                    FileOutputStream b = null;
                    File file = new File("/sdcard/jyt/myImage/");
                    file.mkdirs();// 创建文件夹，名称为myimage
                    b = new FileOutputStream(fileName);
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

                }catch (Exception e){

                }
            }
        };
    @Override
    public  boolean onKeyDown(int keyCode,KeyEvent keyEvent)
    {
        if(keyCode == keyEvent.KEYCODE_BACK)
        {

            finish();
        }
        return  false;
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("照片列表单个图片"); //统计页面
        MobclickAgent.onResume(this);

        TCAgent.onPageStart(this, "照片列表单个图片");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("照片列表单个图片");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"照片列表单个图片");
        TCAgent.onPause(this);
    }

    }




