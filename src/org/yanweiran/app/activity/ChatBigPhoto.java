package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.ChatMsgEntity;
import org.yanweiran.app.dialog.DialogUtil2;


/**
 * Created by lenov on 14-4-27.
 */
public class ChatBigPhoto extends Activity {

    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    int defaultImageId = R.drawable.fail;
    private Dialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bigphoto);
        DemoApplication.getInstance().addActivity(this);
        initView();

    }

    public  void  initView(){
        imageLoader = ImageLoader.getInstance();
        dialog = DialogUtil2.createLoadingDialog(this,"加载中...");
        dialog.show();
        final    ChatMsgEntity chatMsgEntity = (ChatMsgEntity)getIntent().getSerializableExtra("chatMsgEntity");
        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatBigPhoto.this.finish();
            }
        });
       final   ImageView imgView = (ImageView)findViewById(R.id.icon);
        TextView tvName = (TextView)findViewById(R.id.tvName);
        TextView tvTime = (TextView)findViewById(R.id.tvTime);
        ImageButton saveImg = (ImageButton)findViewById(R.id.savedImg);
        tvName.setText(chatMsgEntity.getName());
        tvTime.setText(chatMsgEntity.getDate());
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(defaultImageId)
                .showImageOnFail(defaultImageId)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .resetViewBeforeLoading(true)
                .build();
        imageLoader.displayImage(
                chatMsgEntity.getbPhotoUrl(),
                imgView,
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

    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("聊天大图"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "聊天大图");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("聊天大图");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"聊天大图");
        TCAgent.onPause(this);
    }
}
