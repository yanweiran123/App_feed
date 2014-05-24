package org.yanweiran.app.activity;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.NoticeEntity;
import org.yanweiran.app.Singleton.PhotoListEntity;
import org.yanweiran.app.Singleton.PublicType;
import org.yanweiran.app.adapter.SinglePhotoFragmentAdapter;
import org.yanweiran.app.fragment.TSinglePhotoFragment;

import java.util.ArrayList;

/**
 * Created by lenov on 14-4-19.
 */
public class TweetNoticeSinglePhoto extends FragmentActivity {

    Dialog dialog;
    private ImageButton back;
    private ArrayList<Fragment> fragmentArrayList;
    private NoticeEntity entity;
    private TextView tvCount;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        setContentView(R.layout.tweet_notice_singimg);
        entity =(NoticeEntity)getIntent().getSerializableExtra("noticeEntity");
        initView();
        initViewPager();
    }

    public void initView(){
        back = (ImageButton)findViewById(R.id.back);
        tvCount = (TextView)findViewById(R.id.count);
        tvCount.setText(PublicType.getPublicType().IMG_INDEX+1+"/"+entity.getImgNum());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TweetNoticeSinglePhoto.this.finish();
            }
        });
    }
    public void initViewPager(){
      final ViewPager  mViewPager = (ViewPager)findViewById(R.id.vPager);
      fragmentArrayList = new ArrayList<Fragment>();
        if(!entity.getS_photo1().equals(""))
        {
            PhotoListEntity photoListEntity = new PhotoListEntity();
            photoListEntity.setName(entity.getName());
            photoListEntity.setTime(entity.getSendTime());
            photoListEntity.setbPhotoUrl(entity.getB_photo1());
            photoListEntity.setsPhotoUrl(entity.getS_photo1());
            fragmentArrayList.add(TSinglePhotoFragment.newsInstance(photoListEntity));
        }
        if (!entity.getS_photo2().equals("")){
            PhotoListEntity photoListEntity = new PhotoListEntity();
            photoListEntity.setName(entity.getName());
            photoListEntity.setTime(entity.getSendTime());
            photoListEntity.setbPhotoUrl(entity.getB_photo2());
            photoListEntity.setsPhotoUrl(entity.getS_photo2());
            fragmentArrayList.add(TSinglePhotoFragment.newsInstance(photoListEntity));
        }
        if(!entity.getS_photo3().equals("")){
            PhotoListEntity photoListEntity = new PhotoListEntity();
            photoListEntity.setName(entity.getName());
            photoListEntity.setTime(entity.getSendTime());
            photoListEntity.setbPhotoUrl(entity.getB_photo3());
            photoListEntity.setsPhotoUrl(entity.getS_photo3());
            fragmentArrayList.add(TSinglePhotoFragment.newsInstance(photoListEntity));
        }
        mViewPager.setAdapter(new SinglePhotoFragmentAdapter(getSupportFragmentManager(), fragmentArrayList));
        mViewPager.setCurrentItem(PublicType.getPublicType().IMG_INDEX);
        mViewPager.setOnPageChangeListener( new MyOnPageChangeListener());
    }


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            //  Animation animation = null;
            tvCount.setText(arg0+1+"/"+entity.getImgNum());
//            animation.setFillAfter(true);
//            animation.setDuration(300);

        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
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
        MobclickAgent.onPageStart("新鲜事大图"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "新鲜事大图");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("新鲜事大图");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"新鲜事大图");
        TCAgent.onPause(this);
    }

}
