package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.logging.Log;
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
import org.yanweiran.app.Singleton.NoticeEntity;
import org.yanweiran.app.Singleton.PublicType;
import org.yanweiran.app.Singleton.RegisterPerson;
import org.yanweiran.app.Singleton.UploadUtil;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.dialog.DialogUtil2;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenov on 14-4-12.
 */
public class ModifyHeadPhoto extends Activity {

    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private Button cam;
    private Button album;
    private Button cancel;
    private ImageView faceImage;
    int defaultImageId = R.drawable.indexicon;
    private Button savedImag;
    private String filePath=null;
    private Dialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState){
                super.onCreate(savedInstanceState);
                setContentView(R.layout.modify_head_photo);
                DemoApplication.getInstance().addActivity(this);
                dialog = DialogUtil2.createLoadingDialog(this,"正在上传");
                imageLoader=ImageLoader.getInstance();
                mDisplayImageOptions = new DisplayImageOptions.Builder()
                        .showStubImage(defaultImageId)
                        .showImageForEmptyUri(defaultImageId)
                        .showImageOnFail(defaultImageId)
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .resetViewBeforeLoading()
                        .build();

                initView();
    }

    public  void initView(){
        faceImage = (ImageView)findViewById(R.id.icon);
        imageLoader.displayImage(User.getUser().headUrl,faceImage,mDisplayImageOptions);
        cam = (Button)findViewById(R.id.cam);
        album=(Button)findViewById(R.id.album);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*"); // 设置文件类型
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery,
                        IMAGE_REQUEST_CODE);
            }
        });
     cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ModifyHeadPhoto.this,IndividualCenter.class);
                ModifyHeadPhoto.this.startActivity(intent);
                ModifyHeadPhoto.this.finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        });
        savedImag = (Button)findViewById(R.id.savedImg);
        savedImag.setOnClickListener(new SendClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (Tools.hasSdcard()) {
                        File tempFile = new File(
                                Environment.getExternalStorageDirectory()+ "/"
                                        + IMAGE_FILE_NAME);


                        //filePath = "/storage/sdcard0/DCIM/Camera/1397367040179.jpg";
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(ModifyHeadPhoto.this, "未找到存储卡，无法存储照片！",
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


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            faceImage.setImageDrawable(drawable);
            ImageTools.savePhotoToSDCard(photo, Environment.getExternalStorageDirectory().getAbsolutePath(),
                    "faceImage");
            filePath =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"faceImage.png";
        }
    }

    public class SendClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v)
        {
            dialog.show();
            final String dataUrl = BaseUrl.BASE_URL+"uploadhead.php?token="+ User.getUser().token;

            if(filePath ==null)
            {
                Toast.makeText(getApplicationContext(), "文件不存在",
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            else {
            new Thread(){
                @Override
                public void run(){


                            try{
                                boolean changeSuccess =  post(filePath, dataUrl);
                                if(changeSuccess){
                                    Message msg= new Message();
                                    msg.what = 0x123;
                                    handler.sendMessage(msg);}
                            }catch (ClientProtocolException ex){

                            }catch (IOException ex){

                            }catch (JSONException ex){

                            }
                 }

            }.start();
            }

        }
    }
    private Handler handler =new Handler(){
        @Override

        public void handleMessage(Message msg){
            if(msg.what==0x123)
            {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "上传成功!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(ModifyHeadPhoto.this,IndividualCenter.class);
                startActivity(intent);
                finish();
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
        String path="";
        int status=0;
        int error=1;
        if (resEntity != null) {
            //System.out.println(EntityUtils.toString(resEntity,"utf-8"));
            json= EntityUtils.toString(resEntity, "utf-8");
            JSONObject p=null;
            try{
                p=new JSONObject(json);
                User.getUser().headUrl = p.getString("headimg");
                status = p.getInt("status");
                error = p.getInt("error");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }
        httpclient.getConnectionManager().shutdown();
        if(status==1&&error==0){
        return true;}else {
            return  false;
        }

    }
    @Override
    public  boolean onKeyDown(int keyCode,KeyEvent keyEvent)
    {
        if(keyCode == keyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            intent.setClass(this,IndividualCenter.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);

        }
        return  false;
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("修改头像"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "修改头像");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("修改头像");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"修改头像");
        TCAgent.onPause(this);
    }

}
