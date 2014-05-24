package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import  android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.RegisterPerson;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.dialog.DialogUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenov on 13-12-2.
 */
public class Register extends Activity {

        EditText userMail ,pass , confirmPass,babyName ;
        private ImageButton btn_back;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        btn_back = (ImageButton)findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register.this.finish();
            }
        });
        Button submit =(Button)findViewById(R.id.submit);
        userMail = (EditText)findViewById(R.id.re_Email);
        pass = (EditText)findViewById(R.id.password);
        confirmPass=(EditText)findViewById(R.id.confirmPassword);
        babyName = (EditText)findViewById(R.id.babyName);


        final Handler mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if(msg.what==0x123)
                {
                    String etMail = userMail.getText().toString().trim();
                    String password = pass.getText().toString().trim();
                    String getBabyName= babyName.getText().toString().trim();
                    Bundle data = new Bundle();
                    RegisterPerson  regPerson = new RegisterPerson(etMail,password,getBabyName);
                    data.putSerializable("person",regPerson);
                }
            }
        };

        final     String jsonDataUrl = BaseUrl.BASE_URL+"reg.php";
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getMail = userMail.getText().toString();
                final String getPass = pass.getText().toString();
                final String getBabyName = babyName.getText().toString();

                if (validate())
                {
                    RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
                    StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,jsonDataUrl
                    ,new Response.Listener<String>(){
                        @Override
                                public  void onResponse(String string)

                                {
                                    try
                                    {
                                        JSONObject jsonObject = new JSONObject(string);
                                        if(jsonObject.getString("succ").equals("1"))
                                        {
                                            User.getUser().bbname=jsonObject.getString("bbname");
                                            User.getUser().email=userMail.getText().toString().trim();
                                            User.getUser().token = jsonObject.getString("token");
                                            User.getUser().headUrl = jsonObject.optString("myhead");
                                            User.getUser().tag = "0";
                                            User.getUser().news =jsonObject.optInt("news");
                                            User.getUser().school_num = jsonObject.optString("xuehao");
                                            User.getUser().notifi = jsonObject.optInt("notifi");
                                            User.getUser().IsRegister = 1;
                                            SharedPreferences.Editor sharedata = getSharedPreferences("data", 0).edit();
                                            sharedata.putString("item",userMail.getText().toString().trim());
                                            sharedata.putString("headUrl",jsonObject.optString("myhead"));
                                            sharedata.putInt("check", 0);
                                            sharedata.commit();
                                            Intent intent = new Intent();
                                            intent.setClass(Register.this,Tile.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else if(jsonObject.getString("succ").equals("2"))
                                        {
                                            DialogUtil.showDialog(Register.this,"邮箱格式不正确");
                                        }
                                        else
                                        {
                                            DialogUtil.showDialog(Register.this,"邮箱已经被注册");
                                        }
                                    }
                                    catch (JSONException ex)
                                    {
                                        DialogUtil.showDialog(Register.this,ex.toString());
                                    }

                                }

                    },new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        protected Map<String,String> getParams()
                        {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("email",getMail);
                            params.put("pwd",getPass);
                            params.put("bbname",getBabyName);
                            return  params;
                        }


                    @Override
                    public   Map<String,String>  getHeaders() throws AuthFailureError
                        {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Content-Type","application/x-www-form-urlencoded");
                            return params;
                        }
                    };
                    requestQueue.add(jsonObjectRequest);
                }


            }
        });
    }





        public boolean validate()
        {
            String username = userMail.getText().toString().trim();
            String password = pass.getText().toString().trim();
            String conFirm = confirmPass.getText().toString().trim();
            String getBabyName= babyName.getText().toString().trim();
            if(username.equals(""))
            {
                DialogUtil.showDialog(this, "用户名不能为空");
                return false;
            }
            if(!isEmail(username))
            {
                DialogUtil.showDialog(this, "邮箱格式不正确");
                return false;
            }
            if(password.equals(""))
            {
                DialogUtil.showDialog(this,"密码不能为空");
                return false;
            }
            if(!(password.equals(conFirm)))
            {

                DialogUtil.showDialog(this,"两次密码输入不一致");
                return false;
            }
            if (getBabyName.equals(""))
            {
                DialogUtil.showDialog(this,"宝宝名字不能为空");
                return false;
            }
            return true;
        }

    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
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
