package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.yanweiran.Login.R;



/**
 * Created by lenov on 14-2-24.
 */
public class StartActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler handler = new Handler();
        handler.postDelayed(new SplashHandler(), 3000);
    }
    class SplashHandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), Login.class));
            StartActivity.this.finish();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "首页");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"首页");
        TCAgent.onPause(this);
    }
}
