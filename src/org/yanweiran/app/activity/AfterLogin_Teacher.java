package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.HttpPackage.HttpUtil;
import org.yanweiran.app.Singleton.SchoolClass;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.clicklistener.BottomMenuListener;
import org.yanweiran.app.dialog.DialogUtil;





/**
 * Created by lenov on 13-12-11.
 */
public class AfterLogin_Teacher extends Activity {



        int ScreenWidth;
        int ScreenHeight;
        TextView selectClass ;
        TextView newsNotice;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afterlogin_teacher);
        /*===========================
        * 得到屏幕的大小
    * ===========================*/
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ScreenWidth = size.x;
        ScreenHeight= size.y;


        /**
         * 得到JSON数据
         * */
        JSONArray arrayClass = User.getUser().teacherClass ;
        JSONObject jsonObjectClass;
        for(int count=0;count<arrayClass.length();count++)
        {
            try
            {
                jsonObjectClass = arrayClass.getJSONObject(count);
                addContent(jsonObjectClass.getString("classname"),jsonObjectClass.getInt("news"),jsonObjectClass.getString("school_class_id"));

            }
            catch (JSONException ex)
            {
                    DialogUtil.showDialog(this,"您暂时没有班级，请到...创建班级");
            }
        }

    }



    /**
     * 加载数据
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflator = new MenuInflater(this);
        inflator.inflate(R.menu.my_menu,menu);
       // MenuItem item1= menu.add(1,1,1,"Item1");
       // MenuItem home_page = (MenuItem)findViewById(R.id.home_page);
         menu.getItem(0).setIntent(new Intent(this,Login.class));
      // messageBox.setIntent(new Intent(this,Login.class));
       // home_page.setIntent(new Intent(this,Login.class));
       // item1.setIntent(new Intent(this,Login.class));
        return super.onCreateOptionsMenu(menu);

    }

        public void addContent(String nameOfClass,int news,String classId)
        {
            LinearLayout containerLayout = (LinearLayout)findViewById(R.id.teacherIndexContent);
            /**
             * 给每行设置一个布局
             * */
            LinearLayout singleLine = new LinearLayout(this);
            LinearLayout.LayoutParams singleLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ScreenHeight/20);
            singleLayoutParams.setMargins(0,ScreenHeight/80,0,ScreenHeight/80);
            singleLine.setOrientation(LinearLayout.HORIZONTAL);
            singleLine.setLayoutParams(singleLayoutParams);
            /**
             * 将班级的名字显示出来
             * */
            selectClass = new TextView(this);
            selectClass.setId(Integer.parseInt(classId));//将班级的ID传给按钮做ID
            selectClass.setText(nameOfClass);
            selectClass.setBackgroundColor(Color.parseColor("#E74B3C"));
            selectClass.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams viewClassLayoutParams = new LinearLayout.LayoutParams(ScreenWidth*5/10,ScreenHeight/20);
            selectClass.setLayoutParams(viewClassLayoutParams);
            selectClass.setTextColor(Color.parseColor("#EEEEEE"));
            selectClass.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {


                    SchoolClass.getSchoolClass().classId = Integer.toString(view.getId());
                    DialogUtil.showDialog(AfterLogin_Teacher.this, "你点击的班级为"+view.getId());
                    Intent intent = new Intent();
                    intent.setClass(AfterLogin_Teacher.this, TeacherNotice.class);
                    startActivity(intent);
                    finish();

                }
            });

            singleLine.addView(selectClass);//将班级的名字栏目添加到单行内容中
            /**
             * 如果有新消息，那么在后面提示
             * */
            if(news>0)
            {
                newsNotice = new TextView(this);
                newsNotice.setText("信");
                LinearLayout.LayoutParams newsNoticeLayoutParam = new LinearLayout.LayoutParams(ScreenHeight/20,ScreenHeight/20);
                newsNoticeLayoutParam.setMargins(ScreenWidth/30,0,0,0);
                newsNotice.setLayoutParams(newsNoticeLayoutParam);
                newsNotice.setGravity(Gravity.CENTER);
                newsNotice.setBackgroundColor(Color.parseColor("#FFFF00"));
                singleLine.addView(newsNotice);

                newsNotice.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(AfterLogin_Teacher.this,MessageBox.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            containerLayout.addView(singleLine);//将单行内容添加到整个内容容器中
        }

}
