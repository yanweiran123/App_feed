package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.fragment.WeekArrangeFragment;
import org.yanweiran.app.myadpter.WeekArrangeFragmentAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lenov on 14-1-10.
 */
public class WeekArrange extends FragmentActivity{
    private static final String TAG  = "WeekArrange";
    private  ViewPager mViewPager;
    private ArrayList<Fragment> fragmentArrayList;
    private TextView mon,tue,wed,thurs,friday;

    private int currIndex = 0;
    private int bottomLineWidth;
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_three;
    private  int position_four;
    private int position_five;
    private Resources resources;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout mainLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.week_arrange,null);
        setContentView(mainLayout);
        resources = getResources();
        InitTextView();
      //  InitWidth();
        InitViewPager();
    }

    private  void InitTextView()
    {
        mon=(TextView)findViewById(R.id.mon);
        tue = (TextView)findViewById(R.id.tue);
        wed = (TextView)findViewById(R.id.wed);
        thurs=(TextView)findViewById(R.id.thurs);
        friday=(TextView)findViewById(R.id.fri);

        mon.setOnClickListener(new MyOnClickListener(0));
        tue.setOnClickListener(new MyOnClickListener(1));
        wed.setOnClickListener(new MyOnClickListener(2));
        thurs.setOnClickListener(new MyOnClickListener(3));
        friday.setOnClickListener(new MyOnClickListener(4));
    }

    private void InitViewPager()
    {
        mViewPager = (ViewPager)findViewById(R.id.vPager);
        fragmentArrayList = new ArrayList<Fragment>();

        Fragment monDayActivity = WeekArrangeFragment.newInstance("1");
        Fragment tuesActivity = WeekArrangeFragment.newInstance("2");
        Fragment wedDayActivity = WeekArrangeFragment.newInstance("2");
        Fragment thursDayActivity = WeekArrangeFragment.newInstance("3");
        Fragment friDayActivity = WeekArrangeFragment.newInstance("4");

        fragmentArrayList.add(monDayActivity);
        fragmentArrayList.add(tuesActivity);
        fragmentArrayList.add(wedDayActivity );
        fragmentArrayList.add(thursDayActivity);
        fragmentArrayList.add(friDayActivity );

        mViewPager.setAdapter(new WeekArrangeFragmentAdapter(getSupportFragmentManager(), fragmentArrayList));
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

    }

    private void InitWidth() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (int) ((screenW / 4.0 - bottomLineWidth) / 2);


        position_one = (int) (screenW / 5.0);
        position_two = position_one * 2;
        position_three = position_one * 3;
        position_four = position_one*4;
        position_five = position_one*5;
    }


    public class  MyOnClickListener implements View.OnClickListener
    {
        private  int index = 0 ;
        public MyOnClickListener(int i )
        {
            index = i;
        }
        @Override
        public  void onClick(View view)
        {
            mViewPager.setCurrentItem(index);
        }
    }


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
          //  Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                     //   animation = new TranslateAnimation(position_one, 0, 0, 0);
                        tue.setTextColor(resources.getColor(R.color.yellow));
                    } else if (currIndex == 2) {
                      //  animation = new TranslateAnimation(position_two, 0, 0, 0);
                        wed.setTextColor(resources.getColor(R.color.yellow));
                    } else if (currIndex == 3) {
                       // animation = new TranslateAnimation(position_three, 0, 0, 0);
                        thurs.setTextColor(resources.getColor(R.color.yellow));
                    }
                    else if (currIndex == 4) {
                     //   animation = new TranslateAnimation(position_three, 0, 0, 0);
                        friday.setTextColor(resources.getColor(R.color.white));
                    }

                    mon.setTextColor(resources.getColor(R.color.white));
                    break;
                case 1:
                    if (currIndex == 0) {
                       // animation = new TranslateAnimation(0, position_one, 0, 0);
                        mon.setTextColor(resources.getColor(R.color.yellow));
                    } else if (currIndex == 2) {
                      //  animation = new TranslateAnimation(position_two, position_one, 0, 0);
                        wed.setTextColor(resources.getColor(R.color.yellow));
                    } else if (currIndex == 3) {
                       // animation = new TranslateAnimation(position_three, position_one, 0, 0);
                        thurs.setTextColor(resources.getColor(R.color.yellow));
                    }else if (currIndex == 4) {
                        //animation = new TranslateAnimation(position_three, position_one, 0, 0);
                        friday.setTextColor(resources.getColor(R.color.yellow));
                    }
                    tue.setTextColor(resources.getColor(R.color.white));
                    break;
                case 2:
                    if (currIndex == 0) {
                       // animation = new TranslateAnimation(0, position_two, 0, 0);
                        mon.setTextColor(resources.getColor(R.color.yellow));
                    } else if (currIndex == 1) {
                        //animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        tue.setTextColor(resources.getColor(R.color.yellow));
                    } else if (currIndex == 3) {
                        //animation = new TranslateAnimation(position_three, position_two, 0, 0);
                        thurs.setTextColor(resources.getColor(R.color.yellow));
                    } else if (currIndex == 4) {
                      //  animation = new TranslateAnimation(position_three, position_two, 0, 0);
                        friday.setTextColor(resources.getColor(R.color.yellow));
                    }
                    wed.setTextColor(resources.getColor(R.color.white));
                    break;
                case 3:
                    if (currIndex == 0) {
                       // animation = new TranslateAnimation(0, position_three, 0, 0);
                        mon.setTextColor(resources.getColor(R.color.yellow));
                    } else if (currIndex == 1) {
                       // animation = new TranslateAnimation(position_one, position_three, 0, 0);
                        tue.setTextColor(resources.getColor(R.color.yellow));
                    } else if (currIndex == 2) {
                      //  animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        wed.setTextColor(resources.getColor(R.color.yellow));
                    }else if (currIndex == 4) {
                       // animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        friday.setTextColor(resources.getColor(R.color.yellow));
                    }
                case 4:
                    if (currIndex == 0) {
                      //  animation = new TranslateAnimation(0, position_three, 0, 0);
                        mon.setTextColor(resources.getColor(R.color.yellow));
                    } else if (currIndex == 1) {
                       // animation = new TranslateAnimation(position_one, position_three, 0, 0);
                        tue.setTextColor(resources.getColor(R.color.yellow));
                    } else if (currIndex == 2) {
                       // animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        wed.setTextColor(resources.getColor(R.color.yellow));
                    }else if (currIndex == 3) {
                       // animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        thurs.setTextColor(resources.getColor(R.color.yellow));
                    }
                    friday.setTextColor(resources.getColor(R.color.white));
                    break;
            }
            currIndex = arg0;
//            animation.setFillAfter(true);
//            animation.setDuration(300);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
