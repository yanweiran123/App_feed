package org.yanweiran.app.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;

import org.yanweiran.app.Singleton.ArrangeEntity;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.dialog.DialogUtil2;
import org.yanweiran.app.fragment.WeekArrangeFragment;
import org.yanweiran.app.adapter.WeekFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov on 14-1-10.
 */
public class WeekArrange extends FragmentActivity{
    private  ViewPager mViewPager;
    private ArrayList<Fragment> fragmentArrayList;
    private TextView mon,tue,wed,thurs,friday;
    private ImageButton prep_btn,nextp_btn;
    private  TextView tvTitle;
    private   int[] styleImgID;
    private  int[] styleColor;
    private List<ArrangeEntity> arrangeList1 = new ArrayList<ArrangeEntity>();
    private List<ArrangeEntity> arrangeList2 = new ArrayList<ArrangeEntity>();
    private List<ArrangeEntity> arrangeList3 = new ArrayList<ArrangeEntity>();
    private List<ArrangeEntity> arrangeList4 = new ArrayList<ArrangeEntity>();
    private List<ArrangeEntity> arrangeList5 = new ArrayList<ArrangeEntity>();
    private List<List<ArrangeEntity>> listList = new ArrayList<List<ArrangeEntity>>();
    private List<JSONArray> jsonArrayList =new ArrayList<JSONArray>();
    private int currIndex = 0;
    private Dialog dialog;


    private int prep;
    private  int next;
    private JSONArray jsonArray1;
    private JSONArray jsonArray2;
    private JSONArray jsonArray3;
    private JSONArray jsonArray4;
    private JSONArray jsonArray5;
    private Resources resources;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout mainLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.week_info,null);
        setContentView(mainLayout);
        dialog = DialogUtil2.createLoadingDialog(WeekArrange.this,"刷新中..");
        dialog.show();
        styleImgID =new int[]{R.drawable.m1_img,R.drawable.m2_img,R.drawable.m3_img,
                R.drawable.m4_img,R.drawable.m5_img,R.drawable.m6_img
        };
        styleColor = new int[]{R.color.m1color,R.color.m2color,R.color.m3color,
                R.color.m4color,R.color.m5color,R.color.m6color,};
        resources = getResources();
        InitTextView();
      //  InitWidth();
        InitViewPager("");
    }


    private  void InitTextView()
    {

        mon=(TextView)findViewById(R.id.mon);
        tue = (TextView)findViewById(R.id.tue);
        wed = (TextView)findViewById(R.id.wed);
        thurs=(TextView)findViewById(R.id.thurs);
        friday=(TextView)findViewById(R.id.fri);
        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(WeekArrange.this,Tile.class);
                startActivity(intent);
                finish();
            }
        });

        prep_btn=(ImageButton)findViewById(R.id.prep);
        nextp_btn =(ImageButton)findViewById(R.id.nextp);
        prep_btn.setClickable(false);
        nextp_btn.setClickable(false);


        mon.setOnClickListener(new MyOnClickListener(0));
        tue.setOnClickListener(new MyOnClickListener(1));
        wed.setOnClickListener(new MyOnClickListener(2));
        thurs.setOnClickListener(new MyOnClickListener(3));
        friday.setOnClickListener(new MyOnClickListener(4));

    }

    private void InitViewPager(String t)
    {
        mViewPager = (ViewPager)findViewById(R.id.vPager);
        fragmentArrayList = new ArrayList<Fragment>();
        String jsonDataUrl = BaseUrl.BASE_URL+"weekanpai.php?token="+User.getUser().token+t;
        RequestQueue requestQueue = Volley.newRequestQueue(WeekArrange.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,
    new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try{

                if(jsonObject.getInt("status")==1){
                    dialog.dismiss();
                }
                    tvTitle=(TextView)findViewById(R.id.timeTitle);
                    String title  = jsonObject.getString("title");
                    tvTitle.setText(title);
                       arrangeList1.clear();
                       arrangeList2.clear();
                       arrangeList3.clear();
                       arrangeList4.clear();
                       arrangeList5.clear();


                     next = jsonObject.getInt("nextp");
                     prep = jsonObject.getInt("prep");
                    if(next>0)
                    {
                        nextp_btn.setClickable(true);
                        nextp_btn.setImageResource(R.drawable.nextimg);
                        nextp_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.show();
                                InitViewPager("&t="+Integer.toString(next));
                            }
                        });
                    }else{
                        nextp_btn.setClickable(false);
                        nextp_btn.setImageResource(R.drawable.nextimg_none);
                    }

                    if(prep>0)
                    {
                        prep_btn.setClickable(true);
                        prep_btn.setImageResource(R.drawable.prepimg);
                        prep_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.show();
                                InitViewPager("&t="+Integer.toString(prep));
                            }
                        });
                    }else {
                        prep_btn.setClickable(false);
                        prep_btn.setImageResource(R.drawable.prepimg_none);
                    }


                    JSONObject jsonList = jsonObject.getJSONObject("lists");
                    jsonArrayList.add(jsonList.getJSONArray("1"));
                    jsonArrayList.add(jsonList.getJSONArray("2"));
                    jsonArrayList.add(jsonList.getJSONArray("3"));
                    jsonArrayList.add(jsonList.getJSONArray("4"));
                    jsonArrayList.add(jsonList.getJSONArray("5"));



                    jsonArray1 = jsonList.getJSONArray("1");
                    jsonArray2 = jsonList.getJSONArray("2");
                    jsonArray3 = jsonList.getJSONArray("3");
                    jsonArray4 = jsonList.getJSONArray("4");
                    jsonArray5 = jsonList.getJSONArray("5");


                    for(int i=0;i<jsonArray1.length();i++)
                    {
                        ArrangeEntity arrangeEntity = new ArrangeEntity();
                        arrangeEntity.setContent( jsonArray1.getJSONObject(i).getString("c"));
                        arrangeEntity.setTime(jsonArray1.getJSONObject(i).getString("t"));
                        String s =jsonArray1.getJSONObject(i).getJSONObject("s").getString("s");
                        arrangeEntity.setStyleImgId(styleImgID[style(s)]);
                        arrangeEntity.setStyleColorId(styleColor[style(s)]);
                        arrangeList1.add(arrangeEntity);
                    }
                    for(int i=0;i<jsonArray2.length();i++)
                    {
                        ArrangeEntity arrangeEntity = new ArrangeEntity();
                        arrangeEntity.setContent(jsonArray2.getJSONObject(i).getString("c"));
                        arrangeEntity.setTime(jsonArray2.getJSONObject(i).getString("t"));
                        String s =jsonArray2.getJSONObject(i).getJSONObject("s").getString("s");
                        arrangeEntity.setStyleImgId(styleImgID[style(s)]);
                        arrangeEntity.setStyleColorId(styleColor[style(s)]);
                        arrangeList2.add(arrangeEntity);
                    }
                    for(int i=0;i<jsonArray3.length();i++)
                    {
                        ArrangeEntity arrangeEntity = new ArrangeEntity();
                        arrangeEntity.setContent( jsonArray3.getJSONObject(i).getString("c"));
                        arrangeEntity.setTime( jsonArray3.getJSONObject(i).getString("t"));
                        String s =jsonArray3.getJSONObject(i).getJSONObject("s").getString("s");
                        arrangeEntity.setStyleImgId(styleImgID[style(s)]);
                        arrangeEntity.setStyleColorId(styleColor[style(s)]);
                        arrangeList3.add(arrangeEntity);
                    }
                    for(int i=0;i<jsonArray4.length();i++)
                    {
                        ArrangeEntity arrangeEntity = new ArrangeEntity();
                        arrangeEntity.setContent( jsonArray4.getJSONObject(i).getString("c"));
                        arrangeEntity.setTime( jsonArray4.getJSONObject(i).getString("t"));
                        String s =jsonArray4.getJSONObject(i).getJSONObject("s").getString("s");
                        arrangeEntity.setStyleImgId(styleImgID[style(s)]);
                        arrangeEntity.setStyleColorId(styleColor[style(s)]);
                        arrangeList4.add(arrangeEntity);
                    }
                    for(int i=0;i<jsonArray5.length();i++)
                    {
                        ArrangeEntity arrangeEntity = new ArrangeEntity();
                        arrangeEntity.setContent( jsonArray5.getJSONObject(i).getString("c"));
                        arrangeEntity.setTime( jsonArray5.getJSONObject(i).getString("t"));
                        String s =jsonArray5.getJSONObject(i).getJSONObject("s").getString("s");
                        arrangeEntity.setStyleImgId(styleImgID[style(s)]);
                        arrangeEntity.setStyleColorId(styleColor[style(s)]);
                        arrangeList5.add(arrangeEntity);
                    }


                fragmentArrayList.add(WeekArrangeFragment.newInstance(arrangeList1));
                fragmentArrayList.add(WeekArrangeFragment.newInstance(arrangeList2));
                fragmentArrayList.add(WeekArrangeFragment.newInstance(arrangeList3));
                fragmentArrayList.add(WeekArrangeFragment.newInstance(arrangeList4));
                fragmentArrayList.add(WeekArrangeFragment.newInstance(arrangeList5));

                mViewPager.setAdapter(new WeekFragmentAdapter(getSupportFragmentManager(), fragmentArrayList));
                mViewPager.setCurrentItem(0);
                mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
            }
            catch (JSONException ex)
            {
                DialogUtil.showDialog(WeekArrange.this,ex.toString());
            }

        }
    },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        requestQueue.add(jsonObjectRequest);
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

    public int style(String s)
    {

        if (s.equals("m1"))
        {
            return 0;
        }else if(s.equals("m2")){
           return   1;
        }else if(s.equals("m3")){
            return  2;
        }else if(s.equals("m4")){
            return 3;
        }else if(s.equals("m5")){
            return 4;
        }else {
        return 5;
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
                        tue.setTextColor(resources.getColor(R.color.red));
                        tue.setBackgroundColor(resources.getColor(R.color.grey1));
                    } else if (currIndex == 2) {
                        //  animation = new TranslateAnimation(position_two, 0, 0, 0);
                        wed.setTextColor(resources.getColor(R.color.red));
                        wed.setBackgroundColor(resources.getColor(R.color.grey1));
                    } else if (currIndex == 3) {
                        // animation = new TranslateAnimation(position_three, 0, 0, 0);
                        thurs.setTextColor(resources.getColor(R.color.red));
                        thurs.setBackgroundColor(resources.getColor(R.color.grey1));
                    }
                    else if (currIndex == 4) {
                        //   animation = new TranslateAnimation(position_three, 0, 0, 0);
                        friday.setTextColor(resources.getColor(R.color.red));
                        friday.setBackgroundColor(resources.getColor(R.color.grey1));
                    }
                    mon.setTextColor(resources.getColor(R.color.white));
                    mon.setBackgroundColor(resources.getColor(R.color.red));
                    break;
                case 1:
                    if (currIndex == 0) {
                        // animation = new TranslateAnimation(0, position_one, 0, 0);
                        mon.setTextColor(resources.getColor(R.color.red));
                        mon.setBackgroundColor(resources.getColor(R.color.grey1));
                    } else if (currIndex == 2) {
                        //  animation = new TranslateAnimation(position_two, position_one, 0, 0);
                        wed.setTextColor(resources.getColor(R.color.red));
                        wed.setBackgroundColor(resources.getColor(R.color.grey1));
                    } else if (currIndex == 3) {
                        // animation = new TranslateAnimation(position_three, position_one, 0, 0);
                        thurs.setTextColor(resources.getColor(R.color.red));
                        thurs.setBackgroundColor(resources.getColor(R.color.grey1));
                    }else if (currIndex == 4) {
                        //animation = new TranslateAnimation(position_three, position_one, 0, 0);
                        friday.setTextColor(resources.getColor(R.color.red));
                        friday.setBackgroundColor(resources.getColor(R.color.grey1));
                    }
                    tue.setTextColor(resources.getColor(R.color.white));
                    tue.setBackgroundColor(resources.getColor(R.color.red));
                    break;
                case 2:
                    if (currIndex == 0) {
                        // animation = new TranslateAnimation(0, position_two, 0, 0);
                        mon.setTextColor(resources.getColor(R.color.red));
                        mon.setBackgroundColor(resources.getColor(R.color.grey1));
                    } else if (currIndex == 1) {
                        //animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        tue.setTextColor(resources.getColor(R.color.red));
                        tue.setBackgroundColor(resources.getColor(R.color.grey1));
                    } else if (currIndex == 3) {
                        //animation = new TranslateAnimation(position_three, position_two, 0, 0);
                        thurs.setTextColor(resources.getColor(R.color.red));
                        thurs.setBackgroundColor(resources.getColor(R.color.grey1));
                    } else if (currIndex == 4) {
                        //  animation = new TranslateAnimation(position_three, position_two, 0, 0);
                        friday.setTextColor(resources.getColor(R.color.red));
                        friday.setBackgroundColor(resources.getColor(R.color.grey1));
                    }
                    wed.setTextColor(resources.getColor(R.color.white));
                    wed.setBackgroundColor(resources.getColor(R.color.red));
                    break;
                case 3:
                    if (currIndex == 0) {
                        // animation = new TranslateAnimation(0, position_three, 0, 0);
                        mon.setTextColor(resources.getColor(R.color.red));
                        mon.setBackgroundColor(resources.getColor(R.color.grey1));
                    } else if (currIndex == 1) {
                        // animation = new TranslateAnimation(position_one, position_three, 0, 0);
                        tue.setTextColor(resources.getColor(R.color.red));
                        tue.setBackgroundColor(resources.getColor(R.color.grey1));
                    } else if (currIndex == 2) {
                        //  animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        wed.setTextColor(resources.getColor(R.color.red));
                        wed.setBackgroundColor(resources.getColor(R.color.grey1));
                    }else if (currIndex == 4) {
                        // animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        friday.setTextColor(resources.getColor(R.color.red));
                        friday.setBackgroundColor(resources.getColor(R.color.grey1));
                    }
                    thurs.setTextColor(resources.getColor(R.color.white));
                    thurs.setBackgroundColor(resources.getColor(R.color.red));
                    break;
                case 4:
                    if (currIndex == 0) {
                        //  animation = new TranslateAnimation(0, position_three, 0, 0);
                        mon.setTextColor(resources.getColor(R.color.red));
                        mon.setBackgroundColor(resources.getColor(R.color.grey1));
                    } else if (currIndex == 1) {
                        // animation = new TranslateAnimation(position_one, position_three, 0, 0);
                        tue.setTextColor(resources.getColor(R.color.red));
                        tue.setBackgroundColor(resources.getColor(R.color.grey1));
                    } else if (currIndex == 2) {
                        // animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        wed.setTextColor(resources.getColor(R.color.red));
                        wed.setBackgroundColor(resources.getColor(R.color.grey1));
                    }else if (currIndex == 3) {
                        // animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        thurs.setTextColor(resources.getColor(R.color.red));
                        thurs.setBackgroundColor(resources.getColor(R.color.grey1));
                    }
                    friday.setTextColor(resources.getColor(R.color.white));
                    friday.setBackgroundColor(resources.getColor(R.color.red));
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

    @Override
    public  boolean onKeyDown(int keyCode,KeyEvent keyEvent)
    {
        if(keyCode == keyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            intent.setClass(this,Tile.class);
            startActivity(intent);
            finish();
        }
        return  false;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
        TCAgent.onResume(this);

    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        TCAgent.onPause(this);
    }

}
