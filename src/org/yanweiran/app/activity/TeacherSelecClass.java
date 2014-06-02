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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.ClassEntity;
import org.yanweiran.app.Singleton.PublicType;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.baidupushservice.Utils;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.adapter.ClassListviewAdapter;
import org.yanweiran.app.MyWidget.RoundImageView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by lenov on 13-12-11.
 */
public class TeacherSelecClass extends Activity {
        ListView mListView;
        RoundImageView headImg;
        TextView note;
        TextView tvName;

    List<String> tags ;
    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    int defaultImageId = R.drawable.indexicon;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_select_class);
        DemoApplication.getInstance().addActivity(this);
        tags = new LinkedList<String>();
        imageLoader=ImageLoader.getInstance();
        initView();

        /**
         * 得到JSON数据
         * */
        for(int count=0;count<User.getUser().classEntityList.size();count++)
        {
            tags.add(User.getUser().classEntityList.get(count).getTagName());
        }
        ClassListviewAdapter classListviewAdapter = new ClassListviewAdapter(User.getUser().classEntityList,TeacherSelecClass.this);
        mListView.setAdapter(classListviewAdapter);
        mListView.setOnItemClickListener(new ItemClickListener());
        PushManager.setTags(getApplicationContext(), tags);



        }

    public void initView(){
        final  ImageView tag = (ImageView)findViewById(R.id.tag);
        if(User.getUser().tag.equals("1")){
            tag.setVisibility(View.VISIBLE);
        }
        note = (TextView)findViewById(R.id.selectNote);
        note.getBackground().setAlpha(40);
        mListView = (ListView)findViewById(R.id.listView);
        headImg = (RoundImageView)findViewById(R.id.headImg);
        imageLoader.displayImage(
                User.getUser().headUrl,
                headImg,
                mDisplayImageOptions);
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(TeacherSelecClass.this,IndividualCenter.class);
                TeacherSelecClass.this.startActivity(intent);
            }
        });
        tvName =(TextView)findViewById(R.id.tName);
        tvName.setText(User.getUser().bbname);
    }


        public class ItemClickListener implements AdapterView.OnItemClickListener
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User.getUser().news = User.getUser().classEntityList.get(i).getClassNew();
                PublicType.getPublicType().classPosition = i;
                Bundle bundle = new Bundle();
                bundle.putString("classid",Integer.toString(view.getId()));
                User.getUser().classid = Integer.toString(view.getId());
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(TeacherSelecClass.this,Tile.class);
                TeacherSelecClass.this.startActivity(intent);
                TeacherSelecClass.this.finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
              }
        }

    @Override
    public  boolean onKeyDown(int keyCode , KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                PackageManager pm = getPackageManager();
                ResolveInfo homeInfo = pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    ActivityInfo ai = homeInfo.activityInfo;
                    Intent startIntent = new Intent(Intent.ACTION_MAIN);
                    startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
                    startActivitySafely(startIntent);
                } else
                    return super.onKeyDown(keyCode, event);
                return true;
            }
            return super.onKeyDown(keyCode, event);
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("教师导航"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "教师导航");
        TCAgent.onResume(this);

    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("教师导航");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"教师导航");
        TCAgent.onPause(this);
    }

}
