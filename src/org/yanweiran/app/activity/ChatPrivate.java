package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.MyWidget.ImageTools;
import org.yanweiran.app.MyWidget.RTPullListView;
import org.yanweiran.app.MyWidget.Tools;
import org.yanweiran.app.Singleton.BaseUrl;

import org.yanweiran.app.Singleton.ChatMsgEntity;
import org.yanweiran.app.Singleton.RecentTalkEntity;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.adapter.ChatEmojiGridviewAdapter;
import org.yanweiran.app.adapter.ChatMsgViewAdapter;
import org.yanweiran.app.dialog.DialogUtil;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

/**
 * Created by lenov on 14-1-28.
 */
public class ChatPrivate extends Activity {


        private RelativeLayout ill_line;
        private RelativeLayout tip_line;
        private RTPullListView mListView;
        private LinearLayout ll_Popup;
        private EditText editText;
        private GridView gridView;
        private ImageButton mBtnBack;
        private Button mFace;
        private Button mBtnSend;
        private Button addOther;
        private ImageLoader imageLoader;
        private ImageView ill;
        private ImageView tip;
        private ImageView camera;
        private ImageView photo;
        public  MyApplication application;
        public  RecentTalkEntity chatObject;
        ChatMsgViewAdapter adapter;
        private ArrayList<ChatMsgEntity> chatMsgEntities = new ArrayList<ChatMsgEntity>();
        String content=null;
        private String filePath=null;
        JSONObject  jsonObject=null;
        TextView talkToPerson ;
        private Thread upLoadThread;
        private static  String imgurl;
        private  static  String saveImgUrl;
        private static final int IMAGE_REQUEST_CODE = 0;
        private static final int CAMERA_REQUEST_CODE = 1;
        private static final int RESULT_REQUEST_CODE = 2;
        private static final String IMAGE_FILE_NAME = "chatImage.jpg";
        private String[] strArray={"[1f604]","[1f60a]","[1f603]","[263a]","[1f609]","[1f618]",
                                    "[1f64f]","[1f633]","[1f60c]","[1f601]","[1f61c]","[1f61d]",
                                    "[1f60f]","[1f61e]","[1f616]","[1f630]","[1f623]","[1f62d]",
                                    "[1f632]","[1f631]","[1f620]","[1f621]"
                                  };
        private static String MAXID=null;
        private static String MINID=null;
        private static final int LOAD_NEW_INFO = 5;

        @Override
        public  void onCreate(Bundle  savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                LinearLayout mainLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.chat,null);
                setContentView(mainLayout);
                DemoApplication.getInstance().addActivity(this);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                chatObject =  (RecentTalkEntity)getIntent().getSerializableExtra("chatObject");
                imageLoader = ImageLoader.getInstance();
                initView();
                initData();
            }
        public void initView()
        {
            gridView = (GridView)findViewById(R.id.gridView);
            gridView.setAdapter(new ChatEmojiGridviewAdapter(strArray,this));
            mListView=(RTPullListView)findViewById(R.id.talkList);
            mBtnBack=(ImageButton)findViewById(R.id.back);
            talkToPerson=(TextView)findViewById(R.id.talk_name);
            mBtnSend=(Button)findViewById(R.id.btn_send);
            mBtnSend.setOnClickListener(new SendMsgListener());
            editText = (EditText)findViewById(R.id.et_msg);
            mFace = (Button)findViewById(R.id.myFace);
            addOther = (Button)findViewById(R.id.addOther);
            ll_Popup = (LinearLayout)findViewById(R.id.popBottom);
            addOther.setOnClickListener(new MyOnClickListener());
            mFace.setOnClickListener(new MyOnClickListener());
            editText.setOnClickListener(new MyOnClickListener());
            mListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    gridView.setVisibility(View.GONE);
                    ll_Popup.setVisibility(View.GONE);
                    imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ChatPrivate.this.getCurrentFocus().getWindowToken(), 0);
                    return false;
                }
            });
            mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gridView.getVisibility()==View.VISIBLE||ll_Popup.getVisibility()==View.VISIBLE){
                    gridView.setVisibility(View.GONE);
                    ll_Popup.setVisibility(View.GONE);
                }else {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent();
                    intent.setClass(ChatPrivate.this, AwesomeActivity.class);
                    bundle.putInt("index",2);
                    intent.putExtras(bundle);
                    ChatPrivate.this.startActivity(intent);
                    ChatPrivate.this.finish();
                    ChatPrivate.this.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                }

            }
        });
            mListView.setOnRefreshListener(new RTPullListView.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                loadMore();
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index = editText.getSelectionStart();//获取光标所在位置

                String text="I want to input str";

                Editable edit = editText.getEditableText();//获取EditText的文字

                if (index < 0 || index >= edit.length() ){
                    edit.append(strArray[i]);
                }else{
                    edit.insert(index,strArray[i]);//光标所在位置插入文字
                }
            }
        });

        if(User.getUser().tag.equals("1")){
            ill_line = (RelativeLayout)findViewById(R.id.ill_line);
            tip_line = (RelativeLayout)findViewById(R.id.tip_line);
            ill_line.setVisibility(View.GONE);
            tip_line.setVisibility(View.GONE);
        }
        ill = (ImageView)findViewById(R.id.img03);
        tip= (ImageView)findViewById(R.id.img04);
        ill.setOnClickListener(new BottomItemClickListener());
        tip.setOnClickListener(new BottomItemClickListener());
        camera = (ImageView)findViewById(R.id.img01);
        photo = (ImageView)findViewById(R.id.img02);
        camera.setOnClickListener(new SendImgClickListener());
        photo.setOnClickListener(new SendImgClickListener());
     }



        public  void initData()
        {
            talkToPerson.setText(chatObject.getMsgName());
            RequestQueue requestQueue = Volley.newRequestQueue(ChatPrivate.this);
            String jsonDataUrl = BaseUrl.BASE_URL+"talk.php?"+"token="+ User.getUser().token+"&fid="+chatObject.getFid();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public  void onResponse(JSONObject jsonObject)
                        {
                            try
                            {
                                JSONArray msgArray = jsonObject.getJSONArray("messages");
                                MAXID = jsonObject.getString("maxid");
                                MINID = jsonObject.getString("minid");
                                Integer num = msgArray.length();
                                for(int i=0;i<num;i++)
                                {
                                    ChatMsgEntity  chatMsgEntity= new ChatMsgEntity();
                                    chatMsgEntity.setMessage(msgArray.getJSONObject(i).getString("mess"));
                                    chatMsgEntity.setName(msgArray.getJSONObject(i).getString("name"));
                                    chatMsgEntity.setHeadImgUrl(msgArray.getJSONObject(i).getString("headimg"));
                                    chatMsgEntity.setDate(msgArray.getJSONObject(i).getString("send_time"));
                                    chatMsgEntity.setMsgType(msgArray.getJSONObject(i).getInt("ismy"));
                                    chatMsgEntity.setMsgTag(msgArray.getJSONObject(i).getString("tag"));
                                    chatMsgEntity.setsPhotoUrl(msgArray.getJSONObject(i).getString("s_photo"));
                                    chatMsgEntity.setbPhotoUrl(msgArray.getJSONObject(i).getString("b_photo"));
                                    if(!chatMsgEntity.getsPhotoUrl().equals("")){
                                        chatMsgEntity.setMsgTag("3");
                                    }
                                    chatMsgEntities.add(chatMsgEntity);
                                }

                                    adapter = new ChatMsgViewAdapter(ChatPrivate.this,chatMsgEntities,imageLoader);
                                    mListView.setAdapter(adapter);
                                    mListView.setSelection(chatMsgEntities.size()-1);
                                    new Thread(new MyThread()).start();
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
            editText.getText().clear();
        }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_NEW_INFO:
                    mListView.onRefreshComplete();
                    break;
                default:
                    break;
            }
        }

    };
    public class  BottomItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Bundle bundle = new Bundle();
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.img03:
                    intent.setClass(ChatPrivate.this,Ill.class);
                    bundle.putSerializable("chatObject", chatObject);
                    intent.putExtras(bundle);
                    ChatPrivate.this.startActivityForResult(intent,3);
                    break;
                case R.id.img04:
                    intent.setClass(ChatPrivate.this,Tip.class);
                    bundle.putSerializable("chatObject", chatObject);
                    intent.putExtras(bundle);
                    ChatPrivate.this.startActivityForResult(intent,4);
                    break;
            }
        }
    }


    public class SendMsgListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            if(editText.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "消息不能为空",
                        Toast.LENGTH_SHORT).show();
            }else {

                Date date = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 获取当前时间，进一步转化为字符串
                date = new Date();
                final String  str = format.format(date);
                final   ChatMsgEntity  chatMsgEntity= new ChatMsgEntity();
                chatMsgEntity.setMessage(editText.getText().toString().trim());
                chatMsgEntity.setHeadImgUrl(User.getUser().headUrl);
                chatMsgEntity.setDate(str);
                chatMsgEntity.setMsgType(1);
                chatMsgEntity.setMsgTag("0");
                chatMsgEntities.add(chatMsgEntity);
                MAXID =Integer.toString(Integer.valueOf(MAXID)+1);
                adapter.notifyDataSetChanged();
                mListView.setSelection(mListView.getBottom());
                editText.getText().clear();
                RequestQueue requestQueue = Volley.newRequestQueue(ChatPrivate.this);
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

                                        Toast.makeText(getApplicationContext(), "消息成功发送",
                                                Toast.LENGTH_SHORT).show();

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
                        params.put("c",chatMsgEntity.getMessage());
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

    private class MyOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            mListView.setSelection(mListView.getBottom());
            switch (view.getId()){
                case R.id.myFace:
                    if(gridView.getVisibility()==View.GONE)
                    {
                        gridView.setVisibility(View.VISIBLE);

                    }else {
                        gridView.setVisibility(View.GONE);
                    }
                    ll_Popup.setVisibility(View.GONE);
                    imm.hideSoftInputFromWindow(ChatPrivate.this.getCurrentFocus().getWindowToken(), 0);
                    break;
                case R.id.et_msg:
                    gridView.setVisibility(View.GONE);
                    ll_Popup.setVisibility(View.GONE);
                    break;
                case R.id.addOther:
                    gridView.setVisibility(View.GONE);
                    ll_Popup.setVisibility(View.VISIBLE);
                    imm.hideSoftInputFromWindow(ChatPrivate.this.getCurrentFocus().getWindowToken(), 0);
                    break;
            }

        }
    }


    public   class SendImgClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

                switch (view.getId()) {
                    case  R.id.img02:
                        Intent intentFromGallery = new Intent();
                        intentFromGallery.setType("image/*"); // 设置文件类型
                        intentFromGallery
                                .setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intentFromGallery,
                                IMAGE_REQUEST_CODE);
                        break;
                    case  R.id.img01:
                        Intent intentFromCapture = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        // 判断存储卡是否可以用，可用进行存储
                        if (Tools.hasSdcard()) {
                            intentFromCapture.putExtra(
                                    MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(new File(Environment
                                            .getExternalStorageDirectory(),
                                            IMAGE_FILE_NAME)));
                        }
                        startActivityForResult(intentFromCapture,
                                CAMERA_REQUEST_CODE);
                        break;
                }
            }
        }


    public  void autoUpload()
    {

        final String dataUrl = BaseUrl.BASE_URL+"uploadimg.php?token="+ User.getUser().token;
        if(filePath ==null)
        {
            Toast.makeText(getApplicationContext(), "文件不存在",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            upLoadThread  =  new Thread(){
                @Override
                public void run(){
                    try{
                        boolean changeSuccess = post(filePath, dataUrl);
                        if(changeSuccess){
                            Message msg= new Message();
                            msg.what = 0x123;
                            handler.sendMessage(msg);
                        }
                    }catch (ClientProtocolException ex){

                    }catch (IOException ex){

                    }catch (JSONException ex){

                    }
                }

            };
            upLoadThread.start();
        }
    }
    private Handler handler = new Handler(){
        @Override

        public void handleMessage(Message msg){
            if(msg.what==0x123)
            {
                Date date = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 获取当前时间，进一步转化为字符串
                date = new Date();
                final String  str = format.format(date);
                ChatMsgEntity  chatMsgEntity= new ChatMsgEntity();
                chatMsgEntity.setMessage(null);
                chatMsgEntity.setHeadImgUrl(User.getUser().headUrl);
                chatMsgEntity.setDate(str);
                chatMsgEntity.setMsgType(1);
                chatMsgEntity.setMsgTag("3");
                chatMsgEntity.setsPhotoUrl(imgurl);
                chatMsgEntities.add(chatMsgEntity);
                MAXID =Integer.toString(Integer.valueOf(MAXID)+1);
                adapter.notifyDataSetChanged();
                RequestQueue requestQueue = Volley.newRequestQueue(ChatPrivate.this);
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
                        params.put("photo",saveImgUrl);
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
    };

    public boolean post(String pathToOurFile,String urlServer) throws ClientProtocolException, IOException, JSONException {
        HttpClient httpclient = new DefaultHttpClient();
        //设置通信协议版本
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        HttpPost httppost = new HttpPost(urlServer);
        File file = new File(pathToOurFile);

        MultipartEntity mpEntity = new MultipartEntity(); //文件传输
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("file", cbFile); // <input type="file" name="userfile" />  对应的
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());//通信Ok
        String json="";
        int status=0;
        int error=1;

        if (resEntity != null) {
            //System.out.println(EntityUtils.toString(resEntity,"utf-8"));
            json= EntityUtils.toString(resEntity, "utf-8");
            JSONObject p=null;
            try{
                p=new JSONObject(json);
                status = p.getInt("status");
                error = p.getInt("error");
                imgurl= p.getString("imgurl");
                saveImgUrl = p.getString("saveimg");
                Log.e("@@@@@@@@@@@@@@@@@@@@@@++++++++++++++++++++++++++@@@@@@@@@@@@@@@@@@@@@",p.toString());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }
        httpclient.getConnectionManager().shutdown();
        if(status==1&&error==0){
            return true;
        }else {
            return  false;
        }

    }


    @Override
    protected   void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_CANCELED){
        switch (requestCode){
//            case 3:
//                switch (resultCode){
//                    case 1:
//                    ChatMsgEntity chatMsgEntity = (ChatMsgEntity)data.getSerializableExtra("msgEntity");
//                    chatMsgEntities.add(chatMsgEntity);
//                    adapter.notifyDataSetChanged();
//                        Log.d("++++++++++++++++++++++++++++",chatMsgEntity.getMsgTag());
//                        break;
//                }
//                break;
//            case 4:
//                switch (resultCode){
//                    case 1:
//                        ChatMsgEntity chatMsgEntity = (ChatMsgEntity)data.getSerializableExtra("msgEntity");
//                        chatMsgEntities.add(chatMsgEntity);
//                        adapter.notifyDataSetChanged();
//                        break;
//                }
//                break;
            case IMAGE_REQUEST_CODE:
                ContentResolver resolver = getContentResolver();
                //照片的原始资源地址
                Uri originalUri = data.getData();
                try {
                    //使用ContentProvider通过URI获取原始图片
                    Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                    if (photo != null) {
                        //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                        Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / 10, photo.getHeight() / 10);
                        photo.recycle();

                        ImageTools.savePhotoToSDCard(smallBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(),
                                "chatImage");
                        filePath =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"chatImage.png";
                        autoUpload();
                    }
                }catch (FileNotFoundException ex){

                }catch (IOException ex){

                }
                break;
            case CAMERA_REQUEST_CODE:
                if (Tools.hasSdcard()) {
//                        File tempFile = new File(
//                                Environment.getExternalStorageDirectory()+ "/"
//                                        + IMAGE_FILE_NAME);
                    String path =Environment.getExternalStorageDirectory()+ "/"
                    + IMAGE_FILE_NAME;
                    try{
                    InputStream is = new FileInputStream(path);
                    BitmapFactory.Options opts=new BitmapFactory.Options();
                    opts.inTempStorage = new byte[100 * 1024];
                    opts.inPreferredConfig = Bitmap.Config.RGB_565;
                    opts.inPurgeable = true;
                    opts.inSampleSize = 2;
                    opts.inInputShareable = true;
                    Bitmap btp =BitmapFactory.decodeStream(is,null, opts);
                    ImageTools.savePhotoToSDCard(btp, Environment.getExternalStorageDirectory().getAbsolutePath(), "chatImage");
                    filePath =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"chatImage.png";
                    autoUpload();
                    }catch (Exception ex){

                    }
//                        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/"
//                                + IMAGE_FILE_NAME);
//                        Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / 10, bitmap.getHeight() / 10);
//                        bitmap.recycle();
//                                      //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
//                                      //将处理过的图片显示在界面上，并保存到本地
//                        ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), "uploadImage");
//                        filePath =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"uploadImage.png";

//                    File tempFile = new File(
//                            Environment.getExternalStorageDirectory()+ "/"
//                                    + IMAGE_FILE_NAME);
//                    startPhotoZoom(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(ChatPrivate.this, "未找到存储卡，无法存储照片！",
                            Toast.LENGTH_LONG).show();
                }
                break;
            case RESULT_REQUEST_CODE:
                if (data != null) {
                    getImageToView(data);
                }
                break;
             }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            ImageTools.savePhotoToSDCard(photo, Environment.getExternalStorageDirectory().getAbsolutePath(),
                    "chatImage");
            filePath =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"chatImage.png";
            autoUpload();
        }
    }


    Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getMore();
        }
    };
    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(3000);// 线程暂停10秒，单位毫秒
                    Message message = new Message();
                    message.what = 1;
                    handler1.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }



    public void loadMore(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String jsonDataUrl = BaseUrl.BASE_URL +"talktime.php?token="+User.getUser().token+"&fid="+chatObject.getFid()+"&maxid="+MINID+"&tag=1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try{
                            myHandler.sendEmptyMessage(LOAD_NEW_INFO);
                            if(jsonObject.getInt("status")==1)
                            {
                                MINID = jsonObject.getString("minid");
                            }

                        JSONArray msgArray = jsonObject.getJSONArray("messages");
                        int num = msgArray.length();
                        for(int i=num;i>0;i--)
                        {
                            ChatMsgEntity  chatMsgEntity= new ChatMsgEntity();
                            chatMsgEntity.setMessage(msgArray.getJSONObject(i-1).getString("mess"));
                            chatMsgEntity.setName(msgArray.getJSONObject(i-1).getString("name"));
                            chatMsgEntity.setHeadImgUrl(msgArray.getJSONObject(i-1).getString("headimg"));
                            chatMsgEntity.setDate(msgArray.getJSONObject(i-1).getString("send_time"));
                            chatMsgEntity.setMsgType(msgArray.getJSONObject(i-1).getInt("ismy"));
                            chatMsgEntity.setMsgTag(msgArray.getJSONObject(i-1).getString("tag"));
                            chatMsgEntity.setsPhotoUrl(msgArray.getJSONObject(i-1).getString("s_photo"));
                            chatMsgEntity.setbPhotoUrl(msgArray.getJSONObject(i-1).getString("b_photo"));
                            if(!chatMsgEntity.getsPhotoUrl().equals("")){
                                chatMsgEntity.setMsgTag("3");
                            }
                            chatMsgEntities.add(0,chatMsgEntity);

                        }
                        adapter.notifyDataSetChanged();
                        mListView.setSelection(mListView.getTop());
                    }
                    catch (JSONException ex)
                    {
                        DialogUtil.showDialog(ChatPrivate.this,ex.toString());
                    }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    public void getMore(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String jsDataUrl = BaseUrl.BASE_URL + "talktime.php?token="+User.getUser().token+"&fid="+chatObject.getFid()+"&maxid="+MAXID+"&tag=0";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsDataUrl,null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try{

                    MAXID = jsonObject.getString("maxid");
                    JSONArray msgArray = jsonObject.getJSONArray("messages");
                    Integer num = msgArray.length();
                    for(int i=0;i<num;i++)
                    {
                        ChatMsgEntity  chatMsgEntity= new ChatMsgEntity();
                        chatMsgEntity.setMessage(msgArray.getJSONObject(i).getString("mess"));
                        chatMsgEntity.setName(msgArray.getJSONObject(i).getString("name"));
                        chatMsgEntity.setHeadImgUrl(msgArray.getJSONObject(i).getString("headimg"));
                        chatMsgEntity.setDate(msgArray.getJSONObject(i).getString("send_time"));
                        chatMsgEntity.setMsgType(msgArray.getJSONObject(i).getInt("ismy"));
                        chatMsgEntity.setMsgTag(msgArray.getJSONObject(i).getString("tag"));
                        chatMsgEntity.setsPhotoUrl(msgArray.getJSONObject(i).getString("s_photo"));
                        chatMsgEntity.setbPhotoUrl(msgArray.getJSONObject(i).getString("b_photo"));
                        if(!chatMsgEntity.getsPhotoUrl().equals("")){
                            chatMsgEntity.setMsgTag("3");
                        }
                        chatMsgEntities.add(chatMsgEntity);
                    }
                    adapter.notifyDataSetChanged();
                    if(num>0){
                        mListView.setSelectionfoot();
                    }
                }catch (JSONException ex){
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public  boolean onKeyDown(int keyCode , KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            if(gridView.getVisibility()==View.VISIBLE||ll_Popup.getVisibility()==View.VISIBLE){
                gridView.setVisibility(View.GONE);
                ll_Popup.setVisibility(View.GONE);
            }else {
                Bundle bundle = new Bundle();
                Intent intent = new Intent();
                intent.setClass(ChatPrivate.this,AwesomeActivity.class);
                bundle.putInt("index",2);
                intent.putExtras(bundle);
                ChatPrivate.this.startActivity(intent);
                ChatPrivate.this.finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        }
        return  false;
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("私聊"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "私聊");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("私聊");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"私聊");
        TCAgent.onPause(this);
    }


}


