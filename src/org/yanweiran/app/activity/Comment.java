package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.NoticeCommentEntity;
import org.yanweiran.app.Singleton.NoticeEntity;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.adapter.ChatEmojiGridviewAdapter;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.dialog.DialogUtil2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenov on 14-3-22.
 */
public class Comment extends Activity {


    private Button addFace;
    private GridView faceView;
    private EditText editText;
    private Button send;
    private InputMethodManager imm ;
    private NoticeEntity msgEntity;
    private ArrayList<NoticeCommentEntity> noticeCommentEntities = new ArrayList<NoticeCommentEntity>();
    private Dialog dialog;
    private Button cancel;
    private String[] strArray={"[1f604]","[1f60a]","[1f603]","[263a]","[1f609]","[1f618]",
            "[1f64f]","[1f633]","[1f60c]","[1f601]","[1f61c]","[1f61d]",
            "[1f60f]","[1f61e]","[1f616]","[1f630]","[1f623]","[1f62d]",
            "[1f632]","[1f631]","[1f620]","[1f621]"
    };
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);
        DemoApplication.getInstance().addActivity(this);
        Intent intent = getIntent();
        msgEntity=(NoticeEntity)intent.getSerializableExtra("msgEntity");
        imm  = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        dialog = DialogUtil2.createLoadingDialog(Comment.this,"正在评论中...");
        initView();
    }

    public void initView()
    {

        addFace = (Button)findViewById(R.id.addFace);
        faceView= (GridView)findViewById(R.id.gridView);
        editText=(EditText)findViewById(R.id.editext);
        send = (Button)findViewById(R.id.send);
        send.setOnClickListener(new SendCommentClickListener());
        cancel=(Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment.this.setResult(0, new Intent());// 跳转回原来的activit
                Comment.this.finish();
            }
        });

        faceView.setAdapter(new ChatEmojiGridviewAdapter(strArray, this));

        faceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index = editText.getSelectionStart();//获取光标所在位置
                Editable edit = editText.getEditableText();//获取EditText的文字
                if (index < 0 || index >= edit.length() ){
                    edit.append(strArray[i]);
                }else{
                    edit.insert(index,strArray[i]);//光标所在位置插入文字
                }
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faceView.setVisibility(View.GONE);
            }
        });
        addFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(faceView.getVisibility()==View.VISIBLE)
                {
                    faceView.setVisibility(View.GONE);
                    imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
                }else{
                    imm.hideSoftInputFromWindow(Comment.this.getCurrentFocus().getWindowToken(), 0);
                    faceView.setVisibility(View.VISIBLE);
                }


            }
        });
    }

    @Override
    protected   void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode==0)
        {
            DialogUtil.showDialog(this,"评论");
        }
    }
    public class SendCommentClickListener implements View.OnClickListener
    {
        @Override
         public void onClick(View view)
        {

            if(editText.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "评论不能为空！",
                        Toast.LENGTH_SHORT).show();
            }else {
            dialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(Comment.this);
            String dataUrl = BaseUrl.BASE_URL+"comment.php?token="+ User.getUser().token+"&tid="+msgEntity.getTid();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,dataUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try
                            {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getInt("status")==1)
                                {
                                    dialog.dismiss();
                                    JSONObject commentity = jsonObject.getJSONObject("list");
                                    NoticeCommentEntity noticeCommentEntity = new NoticeCommentEntity();
                                    noticeCommentEntity.setCommContent(commentity.getString("c"));
                                    noticeCommentEntity.setCommName(commentity.getString("name"));
                                    noticeCommentEntity.setCommTime(commentity.getString("time"));
                                    noticeCommentEntity.setHeadUrl(commentity.getString("headimg"));
                                    noticeCommentEntity.setRid(commentity.getString("rid"));
                                    noticeCommentEntity.setTag(Integer.valueOf(User.getUser().tag));
                                    noticeCommentEntity.setIsmy(1);
                                    noticeCommentEntity.setTid(msgEntity.getTid());
                                    noticeCommentEntities.add(noticeCommentEntity);
                                    Bundle data = new Bundle();
                                    data.putSerializable("comment", noticeCommentEntity);
                                    Intent intent = new Intent();
                                    intent.putExtras(data);
                                    Comment.this.setResult(1, intent);// 跳转回原来的activit
                                    intent.setClass(Comment.this,TweetDetail.class);
                                    Comment.this.finish();
                                }else {
                                    Intent intent = new Intent();
                                    intent.setClass(Comment.this,Login.class);
                                    Comment.this.startActivity(intent);
                                    Comment.this.finish();
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

            Comment.this.setResult(0, new Intent());// 跳转回原来的activit
            Comment.this.finish();
        }
        return  false;
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("发评论"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "发评论");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("发评论");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"发评论");
        TCAgent.onPause(this);
    }

}
