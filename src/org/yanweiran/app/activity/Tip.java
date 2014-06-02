package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
public class Tip extends Activity {
    private Button cancel;
    private Button sendMsg;
    private android.widget.EditText editText;
    private TextView note;
    RecentTalkEntity chatObject;
    private ChatMsgEntity chatMsgEntity;
    @Override
    public void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
        setContentView(R.layout.tip);
        chatObject  =  (RecentTalkEntity)getIntent().getSerializableExtra("chatObject");
        initView();
    }


    public void initView()
    {
            cancel = (Button)findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Tip.this.finish();
                }
            });
        editText = (EditText)findViewById(R.id.editext);
        sendMsg =(Button)findViewById(R.id.send);
        sendMsg.setOnClickListener(new SendMsgListener());
        note = (TextView)findViewById(R.id.tip);
        note.setText("正在向"+chatObject.getMsgName()+"发送提醒信息....");
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
                chatMsgEntity.setMsgTag("2");
                //editText.getText().clear();
                RequestQueue requestQueue = Volley.newRequestQueue(Tip.this);
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
                                        Intent intent = new Intent();
                                        Bundle data = new Bundle();
                                        data.putSerializable("msgEntity",chatMsgEntity);
                                        intent.putExtras(data);
                                        Tip.this.setResult(1,intent);
                                        Tip.this.finish();
                                    }else {
                                        Intent intent = new Intent();
                                        intent.setClass(Tip.this,Login.class);
                                        Tip.this.startActivity(intent);
                                        Tip.this.finish();
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
                        params.put("tag","2");
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("提醒页面"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "提醒页面");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("提醒页面");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"提醒页面");
        TCAgent.onPause(this);
    }
}
