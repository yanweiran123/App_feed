package org.yanweiran.app.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.RecipeEntity;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.dialog.DialogUtil2;
import org.yanweiran.app.fragment.WeekRecipeFragment;
import org.yanweiran.app.adapter.WeekFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov on 14-1-11.
 */
public class WeekRecipe extends FragmentActivity {

    private ViewPager mViewPager;
    private TextView mon ,tue,wed,thurs,friday;
    private ArrayList<Fragment> fragmentArrayList;
    private ImageButton prep_btn,nextp_btn;
    private  TextView tvTitle;
    private TextView timeTitle;
    private ImageButton back;
    private Dialog dialog;

    private  List<RecipeEntity> recipeEntityList1 = new ArrayList<RecipeEntity>();
    private  List<RecipeEntity> recipeEntityList2 = new ArrayList<RecipeEntity>();
    private  List<RecipeEntity> recipeEntityList3 = new ArrayList<RecipeEntity>();
    private  List<RecipeEntity> recipeEntityList4= new ArrayList<RecipeEntity>();
    private  List<RecipeEntity> recipeEntityList5 = new ArrayList<RecipeEntity>();


    private int currIndex = 0;

    private int prep;
    private  int next;
    private Resources resources;



    @Override
    public void onCreate(Bundle savedInstance)
        {
            super.onCreate(savedInstance);
            LinearLayout mainLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.week_info,null);
            setContentView(mainLayout);
            resources = getResources();
            dialog = DialogUtil2.createLoadingDialog(WeekRecipe.this, "刷新中..");
            dialog.show();
            InitTextView();
            InitViewPager("");
        }


    private  void InitTextView()
    {
        tvTitle=(TextView)findViewById(R.id.title);
        tvTitle.setText("一周食谱");

        mon=(TextView)findViewById(R.id.mon);
        tue = (TextView)findViewById(R.id.tue);
        wed = (TextView)findViewById(R.id.wed);
        thurs=(TextView)findViewById(R.id.thurs);
        friday=(TextView)findViewById(R.id.fri);
        back =(ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(WeekRecipe.this,Tile.class);
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

        String jsonDataUrl = BaseUrl.BASE_URL+"foodanpai.php?token="+ User.getUser().token+t;
        RequestQueue requestQueue = Volley.newRequestQueue(WeekRecipe.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try{

                            if (jsonObject.getInt("status")==1){
                                dialog.dismiss();


                            recipeEntityList1.clear();
                            recipeEntityList2.clear();
                            recipeEntityList3.clear();
                            recipeEntityList4.clear();
                            recipeEntityList5.clear();

                            next = jsonObject.getInt("nextp");
                            prep = jsonObject.getInt("prep");

                            tvTitle=(TextView)findViewById(R.id.timeTitle);
                            String title  = jsonObject.getString("title");
                            tvTitle.setText(title);

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
                            JSONObject jsonObject1 =jsonList.getJSONObject("1");
                            JSONObject jsonObject2 =jsonList.getJSONObject("2");
                            JSONObject jsonObject3 =jsonList.getJSONObject("3");
                            JSONObject jsonObject4 =jsonList.getJSONObject("4");
                            JSONObject jsonObject5 =jsonList.getJSONObject("5");


                            RecipeEntity recipeEntity1 = new RecipeEntity();
                            String s1= "";
                            for(int i =0 ;i<jsonObject1.getJSONArray("zaocan").length();i++){
                                s1=s1+" "+jsonObject1.getJSONArray("zaocan").get(i).toString();
                            }
                            recipeEntity1.setCan1(s1);
                            String s2="";
                            for(int i =0 ;i<jsonObject1.getJSONArray("zaocanjia").length();i++){
                                s2=s2+" "+jsonObject1.getJSONArray("zaocanjia").get(i).toString();
                            }
                            recipeEntity1.setCan2(s2);
                            String s3= "";
                            for(int i =0 ;i<jsonObject1.getJSONArray("wucan").length();i++){
                                s3=s3+" "+jsonObject1.getJSONArray("wucan").get(i).toString();
                            }
                            recipeEntity1.setCan3(s3);
                            String s4= "";
                            for(int i =0 ;i<jsonObject1.getJSONArray("wucanjia").length();i++){
                                s4=s4+" "+jsonObject1.getJSONArray("wucanjia").get(i).toString();
                            }
                            recipeEntity1.setCan4(s4);
                            String s5= "";
                            for(int i =0 ;i<jsonObject1.getJSONArray("wancan").length();i++){
                                s5=s5+" "+jsonObject1.getJSONArray("wancan").get(i).toString();
                            }
                            recipeEntity1.setCan5(s5);


                            RecipeEntity recipeEntity2 = new RecipeEntity();
                            String s21= "";
                            for(int i =0 ;i<jsonObject2.getJSONArray("zaocan").length();i++){
                                s21=s21+" "+jsonObject2.getJSONArray("zaocan").get(i).toString();
                            }
                            recipeEntity2.setCan1(s21);
                            String s22="";
                            for(int i =0 ;i<jsonObject2.getJSONArray("zaocanjia").length();i++){
                                s22=s22+" "+jsonObject2.getJSONArray("zaocanjia").get(i).toString();
                            }
                            recipeEntity2.setCan2(s22);
                            String s23= "";
                            for(int i =0 ;i<jsonObject2.getJSONArray("wucan").length();i++){
                                s23=s23+" "+jsonObject2.getJSONArray("wucan").get(i).toString();
                            }
                            recipeEntity2.setCan3(s23);
                            String s24= "";
                            for(int i =0 ;i<jsonObject2.getJSONArray("wucanjia").length();i++){
                                s24=s24+" "+jsonObject2.getJSONArray("wucanjia").get(i).toString();
                            }
                            recipeEntity2.setCan4(s24);
                            String s25= "";
                            for(int i =0 ;i<jsonObject2.getJSONArray("wancan").length();i++){
                                s25=s25+" "+jsonObject2.getJSONArray("wancan").get(i).toString();
                            }
                            recipeEntity2.setCan5(s25);

                            RecipeEntity recipeEntity3 = new RecipeEntity();
                            String s31= "";
                            for(int i =0 ;i<jsonObject3.getJSONArray("zaocan").length();i++){
                                s31=s31+" "+jsonObject3.getJSONArray("zaocan").get(i).toString();
                            }
                            recipeEntity3.setCan1(s31);
                            String s32="";
                            for(int i =0 ;i<jsonObject3.getJSONArray("zaocanjia").length();i++){
                                s32=s32+" "+jsonObject3.getJSONArray("zaocanjia").get(i).toString();
                            }
                            recipeEntity3.setCan2(s32);

                            String s33= "";
                            for(int i =0 ;i<jsonObject3.getJSONArray("wucan").length();i++){
                                s33=s33+" "+jsonObject3.getJSONArray("wucan").get(i).toString();
                            }
                            recipeEntity3.setCan3(s33);
                            String s34= "";
                            for(int i =0 ;i<jsonObject3.getJSONArray("wucanjia").length();i++){
                                s34=s34+" "+jsonObject3.getJSONArray("wucanjia").get(i).toString();
                            }
                            recipeEntity3.setCan4(s34);
                            String s35= "";
                            for(int i =0 ;i<jsonObject3.getJSONArray("wancan").length();i++){
                                s35=s35+" "+jsonObject3.getJSONArray("wancan").get(i).toString();
                            }
                            recipeEntity3.setCan5(s35);


                            RecipeEntity recipeEntity4= new RecipeEntity();
                            String s41= "";
                            for(int i =0 ;i<jsonObject4.getJSONArray("zaocan").length();i++){
                                s41=s41+" "+jsonObject4.getJSONArray("zaocan").get(i).toString();
                            }
                            recipeEntity4.setCan1(s41);
                            String s42="";
                            for(int i =0 ;i<jsonObject4.getJSONArray("zaocanjia").length();i++){
                                s42=s42+" "+jsonObject4.getJSONArray("zaocanjia").get(i).toString();
                            }
                            recipeEntity4.setCan2(s42);

                            String s43= "";
                            for(int i =0 ;i<jsonObject4.getJSONArray("wucan").length();i++){
                                s43=s43+" "+jsonObject4.getJSONArray("wucan").get(i).toString();
                            }
                            recipeEntity4.setCan3(s43);
                            String s44= "";
                            for(int i =0 ;i<jsonObject4.getJSONArray("wucanjia").length();i++){
                                s44=s44+" "+jsonObject4.getJSONArray("wucanjia").get(i).toString();
                            }
                            recipeEntity4.setCan4(s44);
                            String s45= "";
                            for(int i =0 ;i<jsonObject4.getJSONArray("wancan").length();i++){
                                s45=s45+" "+jsonObject4.getJSONArray("wancan").get(i).toString();
                            }
                            recipeEntity4.setCan5(s45);

                            RecipeEntity recipeEntity5= new RecipeEntity();
                            String s51= "";
                            for(int i =0 ;i<jsonObject5.getJSONArray("zaocan").length();i++){
                                s51=s51+" "+jsonObject5.getJSONArray("zaocan").get(i).toString();
                            }
                            recipeEntity5.setCan1(s51);
                            String s52="";
                            for(int i =0 ;i<jsonObject5.getJSONArray("zaocanjia").length();i++){
                                s52=s52+" "+jsonObject5.getJSONArray("zaocanjia").get(i).toString();
                            }
                            recipeEntity5.setCan2(s52);

                            String s53= "";
                            for(int i =0 ;i<jsonObject5.getJSONArray("wucan").length();i++){
                                s53=s53+" "+jsonObject5.getJSONArray("wucan").get(i).toString();
                            }
                            recipeEntity5.setCan3(s53);
                            String s54= "";
                            for(int i =0 ;i<jsonObject5.getJSONArray("wucanjia").length();i++){
                                s54=s54+" "+jsonObject5.getJSONArray("wucanjia").get(i).toString();
                            }
                            recipeEntity5.setCan4(s54);
                            String s55= "";
                            for(int i =0 ;i<jsonObject5.getJSONArray("wancan").length();i++){
                                s55=s55+" "+jsonObject5.getJSONArray("wancan").get(i).toString();
                            }
                            recipeEntity5.setCan5(s55);



                            recipeEntityList1.add(recipeEntity1);
                            recipeEntityList2.add(recipeEntity2);
                            recipeEntityList3.add(recipeEntity3);
                            recipeEntityList4.add(recipeEntity4);
                            recipeEntityList5.add(recipeEntity5);



                            Fragment monfragment1 = WeekRecipeFragment.newInstance(recipeEntityList1);
                            Fragment monfragment2 = WeekRecipeFragment.newInstance(recipeEntityList2);
                            Fragment monfragment3 = WeekRecipeFragment.newInstance(recipeEntityList3);
                            Fragment monfragment4 = WeekRecipeFragment.newInstance(recipeEntityList4);
                            Fragment monfragment5 = WeekRecipeFragment.newInstance(recipeEntityList5);



                            fragmentArrayList.add(monfragment1);
                            fragmentArrayList.add(monfragment2);
                            fragmentArrayList.add(monfragment3);
                            fragmentArrayList.add(monfragment4);
                            fragmentArrayList.add(monfragment5);

                            mViewPager.setAdapter(new WeekFragmentAdapter(getSupportFragmentManager(), fragmentArrayList));
                            mViewPager.setCurrentItem(0);
                            mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

                            }else {
                                Intent intent = new Intent();
                                intent.setClass(WeekRecipe.this,Login.class);
                                WeekRecipe.this.startActivity(intent);
                                WeekRecipe.this.finish();
                            }
                        }catch ( JSONException ex)
                        {

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
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
