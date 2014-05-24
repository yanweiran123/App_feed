package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.android.pushservice.PushManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.yanweiran.Login.R;
import org.yanweiran.app.MyWidget.RoundImageView;
import org.yanweiran.app.Singleton.User;

/**
 * Created by lenov on 13-12-28.
 */
public class IndividualCenter extends Activity {

    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private TextView tvName;
    private ImageButton back;
    private RelativeLayout relativeLayout;
    private RelativeLayout school_num_line;
    private TextView tv_school_num;
    int defaultImageId = R.drawable.indexicon;
    private RoundImageView headImg;
    private RelativeLayout head_item;
    private Button logout;
    @Override
    public  void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        LinearLayout mainlinearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.individual_center,null);
        setContentView(mainlinearLayout);
        DemoApplication.getInstance().addActivity(this);
       // overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        headImg =(RoundImageView)findViewById(R.id.bigHead);
        tvName = (TextView)findViewById(R.id.userName);
        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        });
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IndividualCenter.this)
                        .setTitle("德蒙家园通")
                        .setMessage("是否退出?");
                builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences pref = IndividualCenter.this.getSharedPreferences(
                                "TweetMemory", 0);
                        pref.edit().clear().commit();
                SharedPreferences pref1 = IndividualCenter.this.getSharedPreferences(
                                "TeacherNoticeMemory", 0);
               pref1.edit().clear().commit();
                        PushManager.stopWork(getApplicationContext());
                        Utils.setBind(IndividualCenter.this,false);
                        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(IndividualCenter.this);
                        SharedPreferences.Editor editor =sf.edit();
                        editor.putString("user_id","");
                        editor.putString("appid","");
                        editor.putString("channel_id","");
                        editor.commit();
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setClass(IndividualCenter.this,Login.class);
                        IndividualCenter.this.startActivity(intent);
                        IndividualCenter.this.finish();
                    }
                });
                builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create().show();
            }
        });
        school_num_line = (RelativeLayout)findViewById(R.id.school_num_line);
        if(User.getUser().tag.equals("1")){
            school_num_line.setVisibility(View.GONE);
        }else {
                tv_school_num = (TextView)findViewById(R.id.tv_school_num);
                tv_school_num.setText(User.getUser().school_num);
        }
        relativeLayout = (RelativeLayout)findViewById(R.id.modify_name);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(IndividualCenter.this,ModifyName.class);
                IndividualCenter.this.startActivity(intent);
                IndividualCenter.this.finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });
        head_item = (RelativeLayout)findViewById(R.id.head_item);
        head_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(IndividualCenter.this,ModifyHeadPhoto.class);
                IndividualCenter.this.startActivity(intent);
                IndividualCenter.this.finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });
        imageLoader = ImageLoader.getInstance();
        tvName.setText(User.getUser().bbname);
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showStubImage(defaultImageId)
                .showImageForEmptyUri(defaultImageId)
                .showImageOnFail(defaultImageId)
                .cacheInMemory()
                .cacheOnDisc()
                .resetViewBeforeLoading()
                .build();
        imageLoader.displayImage(
                User.getUser().headUrl,
                headImg,
                mDisplayImageOptions);

   }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("个人中心"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "个人中心");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人中心");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"个人中心");
        TCAgent.onPause(this);
    }
    @Override
    public  boolean onKeyDown(int keyCode,KeyEvent keyEvent)
    {
        if(keyCode == keyEvent.KEYCODE_BACK)
        {
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
        return  false;
        }
    }
