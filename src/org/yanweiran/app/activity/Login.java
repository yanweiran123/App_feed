package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.MyWidget.RoundImageView;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.clicklistener.TurnForgetPassword;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.dialog.DialogUtil2;

import java.io.File;


public class Login extends Activity
{
    EditText etUsername,etPassword;
    String tag="0";
    private Dialog dialog;
    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    int defaultImageId = R.drawable.indexicon;
    private RoundImageView headImg;
    private Button login;
    static final String DEV_CENTER = "https://openapi.baidu.com/";
    /** redirect uri 值为"oob" */
    private static final String REDIRECT = "oob";
    public CheckBox is_teacher;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.loging);
        UmengUpdateAgent.update(getApplicationContext());
//        initImageLoader(getApplicationContext());
        imageLoader=ImageLoader.getInstance();
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(defaultImageId)
                .showImageOnFail(defaultImageId)
                .build();
        initView();
        login();
    }

        public  void initView(){
            final Button register = (Button)findViewById(R.id.reg);
            login = (Button)findViewById(R.id.login);
            is_teacher = (CheckBox)findViewById(R.id.isTeacher);
            final ImageView tagImg = (ImageView)findViewById(R.id.tag);
            headImg = (RoundImageView)findViewById(R.id.forgetImg);
            etUsername = (EditText)findViewById(R.id.etUsername);
            SharedPreferences sharedata = getSharedPreferences("data", 0);
            String data = sharedata.getString("item", null);
            String url = sharedata.getString("headUrl",null);
            int i = sharedata.getInt("check",0);
            if(i==1){
                is_teacher.setChecked(true);
                tagImg.setVisibility(View.VISIBLE);
            }else {
                is_teacher.setChecked(false);
                tagImg.setVisibility(View.GONE);
            }
            etUsername.setText(data);
            imageLoader.displayImage(
                    url,
                    headImg,
                    mDisplayImageOptions);
            etPassword = (EditText)findViewById(R.id.etPass);
            dialog =   DialogUtil2.createLoadingDialog(Login.this, "正在登录");
            final TextView forgetpass =(TextView)findViewById(R.id.forgetPs);

        /*=============================
               忘记密码
        ============================== */
            forgetpass.setOnClickListener(new TurnForgetPassword(this));


            register.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(Login.this,Register.class);
                    startActivity(intent);
                }
            });
        }

    /**
     * 校验用户名和密码的输入格式
     */
    public void  login(){
               /*==============================
        *   点击登录按钮
        * ===============================*/
        login.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(validate())
                {
                    dialog.show();
                    String  username = etUsername.getText().toString();
                    String  password = etPassword.getText().toString();
                    RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                    if(is_teacher.isChecked())
                    {
                        tag="1";
                        String parameters = "email="+username+"&pwd="+password+"&tag="+tag;
                        String  JSONDataUrl =  BaseUrl.BASE_URL+"login.php?"+parameters;
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                JSONDataUrl,null,new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try
                                {
                                    int  i = jsonObject.getInt("succ");
                                    if(i==1)
                                    {
                                        User.getUser().jsonObject = jsonObject;
                                        User.getUser().tag = "1";
                                        User.getUser().token = jsonObject.getString("token");
                                        User.getUser().bbname = jsonObject.getString("bbname");
                                        User.getUser().headUrl = jsonObject.getString("myhead");
                                        User.getUser().school_num = jsonObject.getString("xuehao");
                                        User.getUser().tagname = jsonObject.optString("tagname");
                                        SharedPreferences.Editor sharedata = getSharedPreferences("data", 0).edit();
                                        sharedata.putString("item",jsonObject.getString("email"));
                                        sharedata.putString("headUrl",jsonObject.getString("myhead"));
                                        User.getUser().IsRegister = 0;
                                        sharedata.putInt("check",1);
                                        sharedata.commit();
                                        dialog.dismiss();
                                        Intent intent = new Intent();
                                        intent.setClass(Login.this,TeacherSelecClass.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        DialogUtil.showDialog(Login.this, "用户名和密码不正确");
                                        dialog.dismiss();

                                    }
                                }
                                catch (JSONException ex) {
                                    DialogUtil.showDialog(Login.this,ex.toString());
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
                        String    JSONDataUrl=BaseUrl.BASE_URL+"login.php?"+parameters;
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                JSONDataUrl,null,new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try
                                {
                                    int  i = jsonObject.getInt("succ");
                                    if(i==1)
                                    {
                                        User.getUser().bbname=jsonObject.getString("bbname");
                                        User.getUser().email=jsonObject.getString("email");
                                        User.getUser().token = jsonObject.getString("token");
                                        User.getUser().headUrl = jsonObject.getString("myhead");
                                        User.getUser().tag = "0";
                                        User.getUser().news =jsonObject.getInt("news");
                                        User.getUser().school_num = jsonObject.getString("xuehao");
                                        User.getUser().notifi = jsonObject.getInt("notifi");
                                        User.getUser().classid =jsonObject.getJSONObject("class").getString("school_class_id");
                                        User.getUser().tagname = jsonObject.optString("tagname");
                                        User.getUser().IsRegister = 0;
                                        SharedPreferences.Editor sharedata = getSharedPreferences("data", 0).edit();
                                        sharedata.putString("item",jsonObject.getString("email"));
                                        sharedata.putString("headUrl",jsonObject.getString("myhead"));
                                        sharedata.putInt("check", 0);
                                        sharedata.commit();
                                        dialog.dismiss();
                                        Intent intent = new Intent();
                                        intent.setClass(Login.this,Tile.class);
                                        startActivity(intent);
                                        finish();
                                    }else if(i==3){
                                        User.getUser().bbname=jsonObject.getString("bbname");
                                        User.getUser().email=jsonObject.getString("email");
                                        User.getUser().token = jsonObject.getString("token");
                                        User.getUser().headUrl = jsonObject.optString("myhead");
                                        User.getUser().tag = "0";
                                        User.getUser().news =jsonObject.optInt("news");
                                        User.getUser().school_num = jsonObject.optString("xuehao");
                                        User.getUser().notifi = jsonObject.optInt("notifi");
                                        User.getUser().IsRegister = 1;
                                        SharedPreferences.Editor sharedata = getSharedPreferences("data", 0).edit();
                                        sharedata.putString("item",jsonObject.optString("email"));
                                        sharedata.putString("headUrl",jsonObject.optString("myhead"));
                                        sharedata.putInt("check", 0);
                                        sharedata.commit();
                                        dialog.dismiss();
                                        Intent intent = new Intent();
                                        intent.setClass(Login.this,Tile.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        DialogUtil.showDialog(Login.this, "用户名和密码不正确");
                                        dialog.dismiss();
                                    }
                                }
                                catch (JSONException ex) {
                                    DialogUtil.showDialog(Login.this,ex.toString());
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
    @Override
    public void onStart() {
        super.onStart();
    }

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
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context,
                true);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(5)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new UsingFreqLimitedMemoryCache(2000000)) // You can pass your own memory cache implementation
                .discCache(new UnlimitedDiscCache(cacheDir))
                .build();
          ImageLoader.getInstance().init(config);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("登录页面"); //统计页面
        TCAgent.onPageStart(this, "登录页面");
        TCAgent.onResume(this);
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("登录页面");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"登录页面");
        TCAgent.onPause(this);
    }
    @Override
    public   boolean  onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if  (event.getAction() == MotionEvent.ACTION_DOWN) {
            System.out.println("down" );
            if  (Login.this .getCurrentFocus() !=  null ) {
                if  (Login.this.getCurrentFocus().getWindowToken() !=  null ) {
                    imm.hideSoftInputFromWindow(Login.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return   super .onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                DemoApplication.getInstance().exit();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
