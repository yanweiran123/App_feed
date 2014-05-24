package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import android.widget.EditText;

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
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.dialog.DialogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenov on 14-4-1.
 */
public class ModifyName extends Activity {

    private EditText etName;
    private Button btn_ok;
    private Button btn_cancle;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_center2);
        DemoApplication.getInstance().addActivity(this);
        etName = (EditText)findViewById(R.id.etName);
        etName.setText(User.getUser().bbname);
        username = etName.getText().toString().trim();
        btn_ok = (Button)findViewById(R.id.ok);
        btn_cancle =(Button)findViewById(R.id.cancel);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ModifyName.this,IndividualCenter.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate())
                {
                    initData();
                }
            }
        });
    }



    public void initData()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(ModifyName.this);
        String jsonDataUrl =  BaseUrl.BASE_URL+"personal.php"+ "?token="+ User.getUser().token +"&act=ok";
        StringRequest jsonObject = new StringRequest(Request.Method.POST,jsonDataUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject= new JSONObject(s);
                            User.getUser().bbname = jsonObject.getString("uname");
                            Toast.makeText(getApplicationContext(), "修改成功！",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(ModifyName.this,IndividualCenter.class);
                            ModifyName.this.startActivity(intent);
                            ModifyName.this.finish();
                        }catch (JSONException ex)
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
                params.put("uname",username);
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
        requestQueue.add(jsonObject);
    }

    public boolean validate()
    {
        username= etName.getText().toString().trim();
        if (username.equals(""))
        {
            DialogUtil.showDialog(this,"用户名不能为空");
            return false;
        }else {
            return  true;
        }
    }

    @Override
    public  boolean  onKeyDown(int keyCode,KeyEvent keyEvent)
    {
        if(keyCode == keyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            intent.setClass(ModifyName.this,IndividualCenter.class);
            startActivity(intent);
            finish();
        }
        return  false;
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("修改名字"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "修改名字");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("修改名字");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"修改名字");
        TCAgent.onPause(this);
    }
}
