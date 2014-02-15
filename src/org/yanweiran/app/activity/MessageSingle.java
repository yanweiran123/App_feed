package org.yanweiran.app.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.HttpPackage.HttpUtil;
import org.yanweiran.app.Singleton.ChatObject;

import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.chatadapter.ChatMsgEntity;
import org.yanweiran.app.chatadapter.ChatMsgViewAdapter;
import org.yanweiran.app.dialog.DialogUtil;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov on 14-1-28.
 */
public class MessageSingle extends Activity {



        private ListView mListView;
        private Button mBtnBack;
        private Button mBtnSend;
        private   EditText mEditTextContent;
    private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 消息对象数组
        int     msgNum = 3;
        String headImgUrl = null;
        String content=null;
  int ismy=0;
        JSONObject  jsonObject=null;
        TextView talkToPerson ;


        @Override
        public  void onCreate(Bundle  savedInstanceState)
        {

            super.onCreate(savedInstanceState);
            LinearLayout mainLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.talkcontent,null);
            setContentView(mainLayout);
            mListView=(ListView)findViewById(R.id.talkList);
            talkToPerson=(TextView)findViewById(R.id.talk_name);
            ChatObject chatObject =  (ChatObject)getIntent().getSerializableExtra("talkPerson");
            talkToPerson.setText(chatObject.getName());
            RequestQueue requestQueue = Volley.newRequestQueue(MessageSingle.this);
           String jsonDataUrl = HttpUtil.BASE_URL+"talk.php?"+"token="+ User.getUser().token+"&fid="+chatObject.getFid().toString();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,
                   new Response.Listener<JSONObject>()
                    {
                        @Override
                    public  void onResponse(JSONObject jsonObject)
                        {
                            try
                            {
                                JSONArray msgArray = jsonObject.getJSONArray("messages");
                                Integer num = msgArray.length();
                                String[] msgContent = new String[num];
                                String[] headUrl = new String[num];
                                Integer[] isComMsg = new Integer[num];
                                String[]    sendTime = new String[num];
                                for(Integer i=0;i<num;i++)
                                {
                                    msgContent[i] = msgArray.getJSONObject(i).getString("mess");
                                    headUrl[i]  =   msgArray.getJSONObject(i).getString("headimg");
                                    sendTime[i] = msgArray.getJSONObject(i).getString("send_time");
                                    isComMsg[i] = msgArray.getJSONObject(i).getInt("ismy");
                                }
                                ChatMsgViewAdapter adapter = new ChatMsgViewAdapter(MessageSingle.this,num,isComMsg,headUrl,msgContent,sendTime);
                                mListView.setAdapter(adapter);
                            }
                            catch (JSONException ex)
                            {

                            }
                        }
                    },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
           requestQueue.add(jsonObjectRequest);
//            mListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                    DialogUtil.showDialog(MessageSingle.this,(position)+"被单击了");
//                }
//            });
//
//      mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//          @Override
//          public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//              DialogUtil.showDialog(MessageSingle.this,(i)+"被长按了");
//              return false;
//          }
//      });





}}
