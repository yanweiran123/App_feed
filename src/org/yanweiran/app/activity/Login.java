package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.HttpPackage.HttpUtil;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.clicklistener.TurnForgetPassword;
import org.yanweiran.app.dialog.DialogUtil;



public class Login extends Activity
{
    EditText etUsername,etPassword;
    String tag="0";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button register = (Button)findViewById(R.id.reg);
        final Button login = (Button)findViewById(R.id.login);
        final CheckBox is_teacher = (CheckBox)findViewById(R.id.is_teacher);
        etUsername = (EditText)findViewById(R.id.input_account);
        etPassword = (EditText)findViewById(R.id.input_ps);
        final TextView forgetpass =(TextView)findViewById(R.id.forget_ps);
        /**
         * 我是老师验证
         * */
        /*=========================
               忘记密码
        ============================ */
        forgetpass.setOnClickListener(new TurnForgetPassword(this));


        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Login.this,Register.class);
                startActivity(intent);
                finish();
            }
        });

        /*==============================
        *   点击登录按钮
        * ===============================*/
        login.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(1==1)
                {
                    String  username = etUsername.getText().toString();
                    String  password = etPassword.getText().toString();
                    RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                    String JSONDataUrl= HttpUtil.BASE_URL+"login.php?";
                    if(is_teacher.isChecked())
                    {
//                        tag="1";
//                        String parameters = "email="+username+"&pwd="+password+"&tag="+tag;
//                        JSONDataUrl = JSONDataUrl+parameters;
                        JSONDataUrl =  "http://115.28.46.167:83/app_feed/login.php?email=sunyibin&pwd=4184&tag=1";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                JSONDataUrl,null,new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try
                                {
                                    int  i = jsonObject.getInt("succ");
                                    if(i==1)
                                    {

                                        Intent intent = new Intent();
                                        intent.setClass(Login.this,AfterLogin_Teacher.class);
                                        startActivity(intent);
                                        finish();
                                        User.getUser().bbname=jsonObject.getString("bbname");
                                        User.getUser().email=jsonObject.getString("email");
                                        User.getUser().teacherClass = jsonObject.getJSONArray("class");
                                        User.getUser().token = jsonObject.getString("token");
                                        User.getUser().tag = "1";

                                    }
                                    else
                                    {
                                        DialogUtil.showDialog(Login.this, "用户名和密码不正确");
                                    }
                                }
                                catch (JSONException ex) {
                                    // 异常处理代码
                                }
                            }
                        },  new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError arg0) {
                            }
                        });
                        requestQueue.add(jsonObjectRequest);
                    }
                    else
                    {
                        tag="0";
                        String parameters = "email="+username+"&pwd="+password+"&tag="+tag;
                        JSONDataUrl = JSONDataUrl+parameters;
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                JSONDataUrl,null,new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                System.out.println("response="+jsonObject);
                                try
                                {
                                    int  i = jsonObject.getInt("succ");
                                    if(i==1)
                                    {
                                        Intent intent = new Intent();
                                        intent.setClass(Login.this,AfterLogin_Student.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        DialogUtil.showDialog(Login.this, "用户名和密码不正确");
                                    }
                                }
                                catch (JSONException ex) {
                                    // 异常处理代码
                                }
                            }
                        },  new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError arg0) {
                            }
                        });
                        requestQueue.add(jsonObjectRequest);
                    }
                }
            }
        });
    }

    /**
     * 校验用户名和密码的输入格式
     */
    private boolean validate()
        {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if(username.equals(""))
            {
                DialogUtil.showDialog(this,"用户名不能为空");
                return false;
            }
            if(password.equals(""))
            {
                DialogUtil.showDialog(this,"密码不能为空");
                return false;
            }
            return true;
        }
    }
