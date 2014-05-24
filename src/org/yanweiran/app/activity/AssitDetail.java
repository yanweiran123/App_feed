package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.AssitEntity;
import org.yanweiran.app.adapter.AssitGridViewAdapter;

/**
 * Created by lenov on 14-3-5.
 */
public class AssitDetail extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assit_detail);
        DemoApplication.getInstance().addActivity(this);
        initView();
    }


    public  void initView()
    {

        AssitEntity assitEntity = (AssitEntity)getIntent().getSerializableExtra("assitDetail");
        TextView title = (TextView)findViewById(R.id.big_title);
        TextView tvTarget = (TextView)findViewById(R.id.target);
        TextView tvReady  =(TextView)findViewById(R.id.prepare);
        TextView tvOperate = (TextView)findViewById(R.id.operate);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               AssitDetail.this.finish();
            }
        });

        String s1 = assitEntity.getTarget() ;
        String s2 = assitEntity.getGetReady();
        String s3 = assitEntity.getOperate();

        title.setText(assitEntity.getTitle());
        tvTarget.setText(s1);
        tvReady.setText(s2);
        tvOperate.setText(s3);
    }

    @Override
    public  boolean onKeyDown(int keyCode , KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
        return  false;
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("育儿助手详细页面"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "育儿助手详细页面");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("育儿助手详细页面");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"育儿助手详细页面");
        TCAgent.onPause(this);
    }

    }

