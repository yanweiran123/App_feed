package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.RegisterPerson;

/**
 * Created by lenov on 14-2-7.
 */
public class RegisterResult extends Activity {

    @Override
    public  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regnote);
        Button button = (Button)findViewById(R.id.returnLogin);
        TextView name = (TextView)findViewById(R.id.yourEmail);
        TextView pass = (TextView)findViewById(R.id.yourPass);
        TextView babyName = (TextView)findViewById(R.id.yourBabyName);
        Intent intent = getIntent();
        RegisterPerson  p = (RegisterPerson)intent.getSerializableExtra("person");

        name.setText("您的账户为："+p.getEmail());
        pass.setText("您的密码为："+p.getPassword());
        babyName.setText("您宝宝的名字为：" + p.getBabyName());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                intent1.setClass(RegisterResult.this,Login.class);
                startActivity(intent1);
                finish();

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("注册"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "注册");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("注册");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"注册");
        TCAgent.onPause(this);
    }
}
