package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.MyWidget.ImageTools;
import org.yanweiran.app.MyWidget.Tools;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.NoticeCommentEntity;
import org.yanweiran.app.Singleton.NoticeEntity;
import org.yanweiran.app.Singleton.PublicNewsImgEntity;
import org.yanweiran.app.Singleton.PublicType;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.adapter.ChatEmojiGridviewAdapter;
import org.yanweiran.app.adapter.PublicNewsImgAdapter;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.dialog.DialogUtil2;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenov on 14-2-27.
 */
public class PublicNews extends Activity {

    private GridView gridView;

    private ImageButton addFace;
    private Button send;
    private Button cancel;
    ImageView picture = null;
    ImageView cam = null;
    private Dialog dialog;
    private EditText editText;
    private static final String IMAGE_FILE_NAME = "uploadImage.jpg";
    private TextView title;
    private ArrayList<ImageView> imageViewArrayList = new ArrayList<ImageView>();
    private  static int UP_LOAD=0;
    public PublicNewsImgAdapter publicNewsImgAdapter;
    public ArrayList<PublicNewsImgEntity> publicNewsImgEntities;
    public GridView gridViewimg;
    private String filePath=null;
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int EDIT_IMG=3;
    private static ArrayList<String>  photoUrlList = new ArrayList<String>();
    private Thread upLoadThread;
    private ScrollView scrollView;
    private static InputMethodManager imm;
    final static int BUFFER_SIZE = 4096;
    private String[] strArray={"[1f604]","[1f60a]","[1f603]","[263a]","[1f609]","[1f618]",
            "[1f64f]","[1f633]","[1f60c]","[1f601]","[1f61c]","[1f61d]",
            "[1f60f]","[1f61e]","[1f616]","[1f630]","[1f623]","[1f62d]",
            "[1f632]","[1f631]","[1f620]","[1f621]"
    };


    @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.public_news);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            DemoApplication.getInstance().addActivity(this);
            initView();
        }


    public void initView()
    {
        title =(TextView)findViewById(R.id.title);
        if(PublicType.getPublicType().Detail_TYPE==1){
            title.setText("通知班级");
        }
        publicNewsImgEntities = new ArrayList<PublicNewsImgEntity>();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        dialog = DialogUtil2.createLoadingDialog(PublicNews.this,"正在发布...");
        picture = (ImageView) findViewById(R.id.picture_btn);
        cam = (ImageView) findViewById(R.id.cam_btn);
        cam.setOnClickListener(new MyOnClickListener());
        picture.setOnClickListener(new MyOnClickListener());
        gridView = (GridView)findViewById(R.id.gridView);
        gridViewimg = (GridView)findViewById(R.id.gridViewImg);
        photoUrlList.add("");
        photoUrlList.add("");
        photoUrlList.add("");

        addFace = (ImageButton)findViewById(R.id.addFace);
        editText = (EditText)findViewById(R.id.content);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridView.setVisibility(View.GONE);
            }
        });
        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UP_LOAD=0;
                Intent intent = new Intent();
                PublicNews.this.setResult(0, intent);// 跳转回原来的activit
                PublicNews.this.finish();
            }
        });
        send = (Button)findViewById(R.id.send);
        send.setOnClickListener(new SendClickListener());

        scrollView = (ScrollView)findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imm.showSoftInput(editText,1);
                gridView.setVisibility(View.GONE);
                return false;
            }
        });

        gridView.setAdapter(new ChatEmojiGridviewAdapter(strArray,this));
      //  NoticeAll noticeAll=new NoticeAll(this, imageView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        addFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gridView.getVisibility() == View.VISIBLE){
                            gridView.setVisibility(View.GONE);
                }else {
                     gridView.setVisibility(View.VISIBLE);
                    imm.hideSoftInputFromWindow(PublicNews.this.getCurrentFocus().getWindowToken(), 1);
                }

            }
        });

    }


        @Override
        protected   void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    ContentResolver resolver = getContentResolver();
                              //照片的原始资源地址
                           Uri originalUri = data.getData();
                                  try {
                                    //使用ContentProvider通过URI获取原始图片
                                    Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                                   if (photo != null) {
                                         //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                                       PublicNewsImgEntity publicNewsImgEntity = new PublicNewsImgEntity();
                                       InputStream is = resolver.openInputStream(originalUri);
                                       BitmapFactory.Options opts=new BitmapFactory.Options();
                                       opts.inTempStorage = new byte[100 * 1024];
                                       opts.inPreferredConfig = Bitmap.Config.RGB_565;
                                       opts.inPurgeable = true;
                                       opts.inSampleSize = 4;
                                       opts.inInputShareable = true;
                                       Bitmap btp =BitmapFactory.decodeStream(is,null, opts);
                                       photo.recycle();
                                       publicNewsImgEntity.setSmallBitmap(getBytes(btp));
                                       publicNewsImgEntity.setBigBitmap(getBytes(btp));
                                       publicNewsImgEntities.add(publicNewsImgEntity);
                                       publicNewsImgAdapter = new PublicNewsImgAdapter(PublicNews.this, publicNewsImgEntities);
                                       gridViewimg.setAdapter(publicNewsImgAdapter);
                                       gridViewimg.setOnItemLongClickListener(new EditPhotoItemClickListener());
                                       ImageTools.savePhotoToSDCard(btp, Environment.getExternalStorageDirectory().getAbsolutePath(),
                                               "uploadImage");
                                       filePath =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"uploadImage.png";
                                       autoUpload();
                                        }
                                  }catch (FileNotFoundException ex){

                                  }catch (IOException ex){

                                  }
                    break;
                case CAMERA_REQUEST_CODE:
                    if (Tools.hasSdcard()) {
                        String path =Environment.getExternalStorageDirectory()+ "/"
                                        + IMAGE_FILE_NAME;
                        try{
                            InputStream is = new FileInputStream(path);
                            BitmapFactory.Options opts=new BitmapFactory.Options();
                            opts.inTempStorage = new byte[100 * 1024];
                            opts.inPreferredConfig = Bitmap.Config.RGB_565;
                            opts.inPurgeable = true;
                            opts.inSampleSize = 4;
                            opts.inInputShareable = true;
                            Bitmap btp =BitmapFactory.decodeStream(is,null, opts);
                            PublicNewsImgEntity publicNewsImgEntity = new PublicNewsImgEntity();
                            publicNewsImgEntity.setBigBitmap(getBytes(btp));
                            publicNewsImgEntity.setSmallBitmap(getBytes(btp));
                            publicNewsImgEntities.add(publicNewsImgEntity);
                            publicNewsImgAdapter = new PublicNewsImgAdapter(PublicNews.this, publicNewsImgEntities);
                            gridViewimg.setAdapter(publicNewsImgAdapter);
                            gridViewimg.setOnItemLongClickListener(new EditPhotoItemClickListener());
                            ImageTools.savePhotoToSDCard(btp, Environment.getExternalStorageDirectory().getAbsolutePath(), "uploadImage");
                            filePath =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"uploadImage.png";
                            autoUpload();
                        }catch (Exception ex){

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "未找到存储卡，无法存储照片！",
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
            Drawable drawable = new BitmapDrawable(photo);
            imageViewArrayList.get(UP_LOAD).setImageDrawable(drawable);
            ImageTools.savePhotoToSDCard(photo, Environment.getExternalStorageDirectory().getAbsolutePath(),
                    "uploadImage");
            filePath =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"uploadImage.png";
            autoUpload();
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
                            handler.sendMessage(msg);}
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
            }
        }
    };

    public static byte[] getBytes(Bitmap bitmap){
        //实例化字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图
        return baos.toByteArray();//创建分配字节数组
    }

    public class  EditPhotoItemClickListener implements AdapterView.OnItemLongClickListener{

        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            publicNewsImgEntities.remove(position);
            publicNewsImgAdapter.notifyDataSetChanged();
            UP_LOAD--;
            Toast.makeText(getApplicationContext(), "已删除",
                    Toast.LENGTH_SHORT).show();
            return true;
        }

//        public void onItemClick(AdapterView<?> adapterView,View view,int i ,long l){
////            PublicType.getPublicType().IMG_INDEX = i;
////            Intent intent = new Intent();
////            Bundle bundle = new Bundle();
////            bundle.putSerializable("imgList",publicNewsImgEntities);
////            intent.putExtras(bundle);
////            intent.setClass(PublicNews.this,EditPhoto.class);
////            PublicNews.this.startActivityForResult(intent,EDIT_IMG);


    }

    public   class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(publicNewsImgEntities.size()==3){
                Toast.makeText(getApplicationContext(), "最多能上传3张",
                        Toast.LENGTH_SHORT).show();
            }else {
            switch (view.getId()) {
                case  R.id.picture_btn:
                    Intent intentFromGallery = new Intent();
                    intentFromGallery.setType("image/*"); // 设置文件类型
                    intentFromGallery
                            .setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intentFromGallery,
                            IMAGE_REQUEST_CODE);
                    break;
                case  R.id.cam_btn:
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
  }




    public class SendClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v)
        {
            if(editText.getText().toString().trim().equals("")){
                DialogUtil.showDialog(PublicNews.this,"写点什么吧...");
            }else {
            dialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(PublicNews.this);
            String dataUrl = BaseUrl.BASE_URL+"publish.php?token="+ User.getUser().token;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,dataUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try
                            {
                                JSONObject jsonObject = new JSONObject(s);
                                JSONObject noticeArray= jsonObject.getJSONObject("lists");
                                if (jsonObject.getInt("status")==1)
                                {

                                    dialog.dismiss();
                                    NoticeEntity noticeEntity = new NoticeEntity();
                                    noticeEntity.setName(noticeArray.getString("name"));
                                    noticeEntity.setAppre(noticeArray.getString("zan"));
                                    noticeEntity.setTid(noticeArray.getString("tid"));
                                    noticeEntity.setReplyNum(noticeArray.getString("reply_num"));
                                    noticeEntity.setMsgContent(noticeArray.getString("message"));
                                    noticeEntity.setS_photo1(noticeArray.getString("s_photo1"));
                                    noticeEntity.setS_photo2(noticeArray.getString("s_photo2"));
                                    noticeEntity.setS_photo3(noticeArray.getString("s_photo3"));
                                    noticeEntity.setHeadImgUrl(noticeArray.getString("headimg"));
                                    noticeEntity.setSendTime(noticeArray.optString("time"));
                                    noticeEntity.setB_photo1(noticeArray.optString("b_photo1"));
                                    noticeEntity.setB_photo2(noticeArray.optString("b_photo2"));
                                    noticeEntity.setB_photo3(noticeArray.optString("b_photo3"));
                                    noticeEntity.setImgNum(publicNewsImgEntities.size());
                                    Bundle data= new Bundle();
                                    data.putSerializable("notice", noticeEntity);
                                    Intent intent = new Intent();
                                    intent.putExtras(data);
                                    Toast.makeText(getApplicationContext(), "发布成功！",
                                            Toast.LENGTH_SHORT).show();
                                    UP_LOAD = 0;
                                    publicNewsImgEntities.clear();
                                    photoUrlList.clear();
                                    PublicNews.this.setResult(1, intent);// 跳转回原来的activit
                                    PublicNews.this.finish();
                                }else {
                                    Intent intent = new Intent();
                                    intent.setClass(PublicNews.this,Login.class);
                                    PublicNews.this.startActivity(intent);
                                    PublicNews.this.finish();
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
                        params.put("notice", PublicType.getPublicType().type);
                        for(int i=0;i<publicNewsImgEntities.size();i++)
                        {
                            photoUrlList.set(i,publicNewsImgEntities.get(i).getUrl());
                        }
                        params.put("photos1",photoUrlList.get(0));
                        params.put("photos2",photoUrlList.get(1));
                        params.put("photos3",photoUrlList.get(2));

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
        String photoUrl=null;
        if (resEntity != null) {
            //System.out.println(EntityUtils.toString(resEntity,"utf-8"));
            json= EntityUtils.toString(resEntity, "utf-8");
            JSONObject p=null;
            try{
                p=new JSONObject(json);
                status = p.getInt("status");
                error = p.getInt("error");
                photoUrl = p.getString("saveimg");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }
        httpclient.getConnectionManager().shutdown();
        if(status==1&&error==0){
            publicNewsImgEntities.get(UP_LOAD).setUrl(photoUrl);
            UP_LOAD = UP_LOAD+1;
            return true;}else {
            return  false;
        }

    }
    @Override
    public  boolean onKeyDown(int keyCode,KeyEvent keyEvent)
    {
        if(keyCode == keyEvent.KEYCODE_BACK)
        {
            UP_LOAD=0;
            Intent intent = new Intent();
            PublicNews.this.setResult(0, intent);// 跳转回原来的activit
            PublicNews.this.finish();
        }
        return  false;
    }
    public static byte[] InputStreamTOByte(InputStream in) throws IOException{

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("发布新鲜事"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "发布新鲜事");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("发布新鲜事");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"发布新鲜事");
        TCAgent.onPause(this);
    }

}
