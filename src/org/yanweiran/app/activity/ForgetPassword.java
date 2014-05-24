package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.dialog.DialogUtil2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by lenov on 14-1-12.
 */
public class ForgetPassword extends Activity {

    private EditText mEditextView;
    private  Button mButton;
    private String mEmail;
    private ImageButton backBtn;
    private Dialog dialog;
    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        LinearLayout mainLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.forget_pass,null);
        DemoApplication.getInstance().addActivity(this);
        setContentView(mainLayout);
        initView();
    }


    public void initView()
    {
        dialog = DialogUtil2.createLoadingDialog(this,"请稍后..");
        mEditextView = (EditText)findViewById(R.id.input_mail);
        backBtn = (ImageButton)findViewById(R.id.back);
        mButton = (Button)findViewById(R.id.submit);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()==true)
                {
                    dialog.show();
                    initData();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ForgetPassword.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private  boolean validate()
    {
        String s1 = mEditextView.getText().toString().trim();
        if (s1.equals(""))
        {
            DialogUtil.showDialog(ForgetPassword.this,"邮箱不能为空");
            return  false;
        }else if(!isEmail(s1)){
            DialogUtil.showDialog(ForgetPassword.this,"邮箱格式不正确");
            return false;
        }
        return true;
    }

    public void initData()
    {
        mEmail = mEditextView.getText().toString().trim();
        RequestQueue requestQueue = Volley.newRequestQueue(ForgetPassword.this);
        String jsonDtaUrl  = BaseUrl.BASE_URL+"findpwd.php?email="+mEmail;
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET,jsonDtaUrl,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if(jsonObject.getInt("succ")==1)
                            {
                                dialog.dismiss();
                                DialogUtil.showDialog(ForgetPassword.this,"您好，请登录您的邮箱查看邮件");
                            }else {
                                dialog.dismiss();
                                DialogUtil.showDialog(ForgetPassword.this,"您好，邮箱不存在");
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
        requestQueue.add(jsonObject);
    }

    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Intent intent = new Intent();
            intent.setClass(ForgetPassword.this,Login.class);
            startActivity(intent);
            finish();
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("忘记密码"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "忘记密码");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("忘记密码");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"忘记密码");
        TCAgent.onPause(this);
    }

    @Override
    public   boolean  onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if  (event.getAction() == MotionEvent.ACTION_DOWN) {
            System.out.println("down" );
            if  (ForgetPassword.this .getCurrentFocus() !=  null ) {
                if  (ForgetPassword.this.getCurrentFocus().getWindowToken() !=  null ) {
                    imm.hideSoftInputFromWindow(ForgetPassword.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return   super .onTouchEvent(event);
    }

}
