package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.Notification;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.adapter.TileAdapter;
import org.yanweiran.app.MyWidget.RoundImageView;


import java.util.LinkedList;
import java.util.List;


/**
 * Created by lenov on 13-12-20.
 */
public class Tile extends Activity {

    private GridView gridView;
    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    int defaultImageId = R.drawable.indexicon;
    private ImageButton btn_back;
    List<String> tags ;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tile);
        Utils.logStringCache = Utils.getLogText(getApplicationContext());
        DemoApplication.getInstance().addActivity(this);
        tags = new LinkedList<String>();
        imageLoader=ImageLoader.getInstance();
        initData();
        initView();
        pushMsg();
//        Intent intent = new Intent();
//        intent .setAction("com.baidu.android.pushservice.action.PUSH_SERVICE");
//        startService(intent);
    }


    public  void initView()
    {

        int[] bgImgId = new int[]{R.drawable.tile1,R.drawable.tile2,R.drawable.tile3,
                R.drawable.tile4,R.drawable.tile5,R.drawable.tile6};
        final ImageView tag = (ImageView)findViewById(R.id.tag);
        if(User.getUser().tag.equals("1")){
            tag.setVisibility(View.VISIBLE);
        }
        gridView =(GridView)findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                Bundle bundle =new Bundle();
                switch (i)
                {
                    case 0:
                        MobclickAgent.onEvent(Tile.this,"db-click","育儿助手");
                        TCAgent.onEvent(Tile.this,"db-click","育儿助手");
                        intent.setClass(Tile.this,Assit.class);
                        Tile.this.startActivity(intent);
                        Tile.this.finish();
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        break;
                    case 1:
                        if(User.getUser().IsRegister==0){
                            MobclickAgent.onEvent(Tile.this,"db-click","老师通知");
                            TCAgent.onEvent(Tile.this,"db-click","老师通知");
                            intent.setClass(Tile.this,AwesomeActivity.class);
                            bundle.putInt("index",1);
                            intent.putExtras(bundle);
                            Tile.this.startActivity(intent);
                            Tile.this.finish();
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        }else {
                            Toast.makeText(getApplicationContext(), "您还没有班级，暂时只能查看育儿助手",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        if(User.getUser().IsRegister==0){
                            MobclickAgent.onEvent(Tile.this,"db-click","新鲜事");
                            TCAgent.onEvent(Tile.this,"db-click","新鲜事");
                            intent.setClass(Tile.this,AwesomeActivity.class);
                            bundle.putInt("index",0);
                            intent.putExtras(bundle);;
                            Tile.this.startActivity(intent);
                            Tile.this.finish();
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        }else {
                            Toast.makeText(getApplicationContext(), "您还没有班级，暂时只能查看育儿助手",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        if(User.getUser().IsRegister==0){
                            MobclickAgent.onEvent(Tile.this,"db-click","消息盒子");
                            TCAgent.onEvent(Tile.this,"db-click","消息盒子");
                            intent.setClass(Tile.this,AwesomeActivity.class);
                            bundle.putInt("index",2);
                            intent.putExtras(bundle);;
                            Tile.this.startActivity(intent);
                            Tile.this.finish();
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        }else {
                            Toast.makeText(getApplicationContext(), "您还没有班级，暂时只能查看育儿助手",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 4:
                        if(User.getUser().IsRegister==0){
                            MobclickAgent.onEvent(Tile.this,"db-click","一周安排");
                            TCAgent.onEvent(Tile.this,"db-click","一周安排");
                            intent.setClass(Tile.this,AwesomeActivity.class);
                            bundle.putInt("index",3);
                            intent.putExtras(bundle);;
                            Tile.this.startActivity(intent);
                            Tile.this.finish();
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        }else {
                            Toast.makeText(getApplicationContext(), "您还没有班级，暂时只能查看育儿助手",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 5:
                        if(User.getUser().IsRegister==0){
                            MobclickAgent.onEvent(Tile.this,"db-click","一周食谱");
                            TCAgent.onEvent(Tile.this,"db-click","一周食谱");
                                intent.setClass(Tile.this,AwesomeActivity.class);
                                bundle.putInt("index",4);
                                intent.putExtras(bundle);
                                Tile.this.startActivity(intent);
                                Tile.this.finish();
                                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        }else {
                            Toast.makeText(getApplicationContext(), "您还没有班级，暂时只能查看育儿助手",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        gridView.setAdapter(new TileAdapter(bgImgId,this));
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(defaultImageId)
                .showImageOnFail(defaultImageId)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        final RoundImageView headImg = (RoundImageView)findViewById(R.id.headImg);
        imageLoader.displayImage(
                User.getUser().headUrl,
                headImg,
                mDisplayImageOptions);
        headImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Tile.this,IndividualCenter.class);
                Tile.this.startActivity(intent);
            }
        });
        final TextView tvName = (TextView)findViewById(R.id.name);
        tvName.setText(User.getUser().bbname);

         btn_back = (ImageButton)findViewById(R.id.back);
        if(User.getUser().tag.equals("0")){
            btn_back.setVisibility(View.GONE);
        }
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Tile.this,TeacherSelecClass.class);
                Tile.this.startActivity(intent);
                Tile.this.finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        });
    }

    public void initData()
    {
        Intent intent = getIntent();
        if(User.getUser().tag.equals("1"))
        {
            String  jsonDtaUrl= BaseUrl.BASE_URL+"chooseclass.php?token="+User.getUser().token+"&classid="+intent.getStringExtra("classid");
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsonDtaUrl,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                        }
                    },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }
            });
            requestQueue.add(jsonObjectRequest);
        }else {
           tags.add(User.getUser().tagname);
           PushManager.setTags(getApplicationContext(), tags);
        }
    }
    @Override
    public  boolean onKeyDown(int keyCode,KeyEvent keyEvent)
    {
        if(keyCode == keyEvent.KEYCODE_BACK)
        {
            if (User.getUser().tag.equals("1")){
               Intent intent = new Intent();
                intent.setClass(Tile.this,TeacherSelecClass.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }else {
                PackageManager pm = getPackageManager();
                ResolveInfo homeInfo =
                        pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    ActivityInfo ai = homeInfo.activityInfo;
                    Intent startIntent = new Intent(Intent.ACTION_MAIN);
                    startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
                    startActivitySafely(startIntent);
                    return true;
                } else
                    return super.onKeyDown(keyCode, keyEvent);
            }

        }
        return  false;
    }
    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("瓷砖页"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "瓷砖页");
        TCAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("瓷砖页");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"瓷砖页");
        TCAgent.onPause(this);
    }

    public void pushMsg(){
        Resources resource = this.getResources();
        String pkgName = this.getPackageName();
        if (!Utils.hasBind(getApplicationContext())) {
            PushManager.startWork(getApplicationContext(),
                    PushConstants.LOGIN_TYPE_API_KEY,
                    Utils.getMetaValue(Tile.this, "api_key"));
            PushConstants.startPushService(getApplicationContext());
            // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
            // PushManager.enableLbs(getApplicationContext());
        }

        // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                getApplicationContext(), resource.getIdentifier(
                "notification_custom_builder", "layout", pkgName),
                resource.getIdentifier("notification_icon", "id", pkgName),
                resource.getIdentifier("notification_title", "id", pkgName),
                resource.getIdentifier("notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier(
                "simple_notification_icon", "drawable", pkgName));
        PushManager.setNotificationBuilder(this, 1, cBuilder);
    }


}








