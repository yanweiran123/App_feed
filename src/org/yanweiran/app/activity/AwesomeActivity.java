package org.yanweiran.app.activity;

/**
 * Created by lenov on 14-2-26.
 */



import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.yanweiran.Login.R;


/**
 * @author Adil Soomro
 *
 */
@SuppressWarnings("deprecation")
public class AwesomeActivity extends TabActivity {
   public static TabHost tabHost;
   public static RadioGroup radioGroup;

    private
    int index;
    /** Called when the activity is first created. */


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DemoApplication.getInstance().addActivity(this);
        Intent intent =getIntent();
        index = intent.getIntExtra("index",0);


        tabHost = getTabHost();
        setTabs();
        radioGroup = (RadioGroup)findViewById(R.id.tab_group);
        radioGroup.check(0);
        switch (index){
            case  0:
                radioGroup.check(R.id.tab1);

                break;
            case  1:
                radioGroup.check(R.id.tab2);

                break;
            case  2:
                radioGroup.check(R.id.tab3);

                break;
            case  3:
                radioGroup.check(R.id.tab4);

                break;
            case  4:
                radioGroup.check(R.id.tab5);

                break;
            default:
                break;
        }
        tabHost.setCurrentTab(index);
        radioGroup.setOnCheckedChangeListener(new onTabChangeListener());

    }
    private void setTabs()
    {
        Intent intent1 = new Intent(this,Tweet.class);
        Intent intent2 = new Intent(this,TeacherNotice.class);
        Intent intent3 = new Intent(this, MessageBox.class);
        Intent intent4 = new Intent(this,WeekArrange.class);
        Intent intent5 = new Intent(this,WeekRecipe.class);
        Intent intent6 = new Intent(this, RelativeComment.class);


        tabHost.addTab(tabHost.newTabSpec("TAB1").setIndicator("Tab1").setContent(intent1));
        tabHost.addTab(tabHost.newTabSpec("TAB2").setIndicator("Tab2").setContent(intent2));
        tabHost.addTab(tabHost.newTabSpec("TAB3").setIndicator("Tab3").setContent(intent3));
        tabHost.addTab(tabHost.newTabSpec("TAB4").setIndicator("Tab4").setContent(intent4));
        tabHost.addTab(tabHost.newTabSpec("TAB5").setIndicator("Tab5").setContent(intent5));
        tabHost.addTab(tabHost.newTabSpec("TAB6").setIndicator("Tab6").setContent(intent6));
    }
    private void addTab(String labelId, int drawableId, Class<?> c)
    {

        Intent intent = new Intent(this, c);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(labelId);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);
        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }

    private class onTabChangeListener implements RadioGroup.OnCheckedChangeListener
    {
        @Override
        public  void onCheckedChanged(RadioGroup group, int id)
        {
            switch (id)
            {
                case  R.id.tab1:
                    tabHost.setCurrentTabByTag("TAB1");
                    MobclickAgent.onEvent(AwesomeActivity.this,"db-click","新鲜事");
                    TCAgent.onEvent(AwesomeActivity.this,"db-click","新鲜事");
                    break;
                case  R.id.tab2:
                    tabHost.setCurrentTabByTag("TAB2");
                    MobclickAgent.onEvent(AwesomeActivity.this,"db-click","老师通知");
                    TCAgent.onEvent(AwesomeActivity.this,"db-click","老师通知");
                    break;
                case R.id.tab3:
                    tabHost.setCurrentTabByTag("TAB3");
                    MobclickAgent.onEvent(AwesomeActivity.this,"db-click","消息盒子");
                    TCAgent.onEvent(AwesomeActivity.this,"db-click","消息盒子");
                    break;
                case R.id.tab4:
                    tabHost.setCurrentTabByTag("TAB4");
                    MobclickAgent.onEvent(AwesomeActivity.this,"db-click","一周安排");
                    TCAgent.onEvent(AwesomeActivity.this,"db-click","一周安排");
                    break;
                case R.id.tab5:
                    tabHost.setCurrentTabByTag("TAB5");
                    MobclickAgent.onEvent(AwesomeActivity.this,"db-click","一周食谱");
                    TCAgent.onEvent(AwesomeActivity.this,"db-click","一周食谱");
                    break;
                case R.id.tab6:
                    tabHost.setCurrentTabByTag("TAB6");
                    MobclickAgent.onEvent(AwesomeActivity.this,"db-click","相关评论");
                    TCAgent.onEvent(AwesomeActivity.this,"db-click","相关评论");
                    break;
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }



}