package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import  android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.RegisterPerson;
import org.yanweiran.app.dialog.DialogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenov on 13-12-2.
 */
public class Register extends Activity {

        EditText userMail ,pass , confirmPass,babyName ;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
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


                        Intent intent = new Intent();
                        intent.setClass(Register.this,RegisterResult.class);
                        intent.putExtras(data);
                        startActivity(intent);
                        finish();





                }
            }
        };

        final     String JsonDataUrl = "http://115.28.46.167:83/app_feed/reg.php";
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getMail = userMail.getText().toString();
                final String getPass = pass.getText().toString();
                final String getConfirmPass = confirmPass.getText().toString();
                final String getBabyName = babyName.getText().toString();

                if (1==1)
                {
                    RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
                    StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,JsonDataUrl
                    ,new Response.Listener<String>(){
                        @Override
                                public  void onResponse(String string)

                                {
                                    try
                                    {
                                        JSONObject jsonObject = new JSONObject(string);
                                        if(jsonObject.getString("succ").equals("1"))
                                        {
                                            Message msg= new Message();
                                            msg.what = 0x123;
                                            mHandler.sendMessage(msg);
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
//                            params.put("email","40766866@qq.com");
//                            params.put("pwd","123456");
//                            params.put("bbname","测试");
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

}
