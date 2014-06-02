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
import org.yanweiran.app.Singleton.ChatMsgEntity;
import org.yanweiran.app.Singleton.RecentTalkEntity;
import org.yanweiran.app.Singleton.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenov on 14-4-10.
 */
public class Ill extends Activity {
    private Button cancel;
    private Button sendMsg;
    private EditText editText;
    private TextView note;
    RecentTalkEntity chatObject;
   private ChatMsgEntity chatMsgEntity;
    @Override
    public  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DemoApplication.getInstance().addActivity(this);
        setContentView(R.layout.ill);
        chatObject  =  (RecentTalkEntity)getIntent().getSerializableExtra("chatObject");
        initView();
    }

    public void initView(){
        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ill.this.finish();
            }
        });
        editText = (EditText)findViewById(R.id.editext);
        sendMsg =(Button)findViewById(R.id.send);
        sendMsg.setOnClickListener(new SendMsgListener());
        note = (TextView)findViewById(R.id.tip);
        note.setText("正在向"+chatObject.getMsgName()+"发送请假信息....");
    }

    public class SendMsgListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            if(editText.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "消息不能为空",
                        Toast.LENGTH_SHORT).show();
            }else {

                Date date = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
                date = new Date();
                final String  str = format.format(date);
                chatMsgEntity= new ChatMsgEntity();
                chatMsgEntity.setMessage(editText.getText().toString().trim());
                chatMsgEntity.setHeadImgUrl(User.getUser().headUrl);
                chatMsgEntity.setDate(str);
                chatMsgEntity.setMsgType(1);
                chatMsgEntity.setMsgTag("1");
                RequestQueue requestQueue = Volley.newRequestQueue(Ill.this);
                String dataUrl = BaseUrl.BASE_URL+"sendtalk.php?token="+ User.getUser().token+"&fid="+chatObject.getFid();
                StringRequest stringRequest = new StringRequest(Request.Method.POST,dataUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try
                                {
                                    JSONObject jsonObject = new JSONObject(s);
                                    if (jsonObject.getInt("status")==1)
                                    {
                                        editText.getText().clear();
                                        Toast.makeText(getApplicationContext(), "消息成功发送",
                                                Toast.LENGTH_SHORT).show();
                                        Bundle data = new Bundle();
                                        data.putSerializable("msgEntity",chatMsgEntity);
                                        Intent intent = new Intent();
                                        intent.putExtras(data);
                                        Ill.this.setResult(1,intent);
                                        Ill.this.finish();
                                    }else {
                                        Intent intent = new Intent();
                                        intent.setClass(Ill.this,Login.class);
                                        Ill.this.startActivity(intent);
                                        Ill.this.finish();
                                    }
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
                        params.put("c",editText.getText().toString().trim());
                        params.put("tag","1");
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
                requestQueue.add(stringRequest);

            }
        }

    }


    @Override
    public  boolean onKeyDown(int keyCode,KeyEvent keyEvent)
    {
        if(keyCode == keyEvent.KEYCODE_BACK)
        {
            finish();
        }
        return  false;
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("生病"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "生病");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("生病");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"生病");
        TCAgent.onPause(this);
    }
}
