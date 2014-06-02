package org.yanweiran.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.AssitEntity;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.RegisterPerson;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.adapter.AssitGridViewAdapter;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.dialog.DialogUtil2;
import org.yanweiran.app.dialog.PopMenu;

public class Assit extends Activity {

    private static final int THREE_FOUR=0;
    private static final int FOUR_FIVE=1;
    private static final int FIVR_SIX=2;
    private static final int ALL=3;
    private ArrayList<AssitEntity> assitEntities;
    private ImageLoader mImageLoader;
    private GridView mGridView;
    private ImageButton backBtn;
    private  String[] s;
    AssitGridViewAdapter gridAssitAdapter;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assit);
        DemoApplication.getInstance().addActivity(this);
        mImageLoader=ImageLoader.getInstance();
        assitEntities = new ArrayList<AssitEntity>();
        mGridView = (GridView)findViewById(R.id.gridView);
        backBtn = (ImageButton)findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Assit.this,Tile.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        });
        dialog = DialogUtil2.createLoadingDialog(Assit.this,"正在刷新中...");
        dialog.show();
        User.getUser().flag = 3;

        s= new String[]{"3-4岁","4-5岁","5-6岁","查看全部"};
        initData("&lv=3-6");
        initImageLoader(this);
        popmenu();
//        mHandler = new Handler()
//        {
//            @Override
//            public void handleMessage(Message msg)
//            {
//                if(msg.what==0x123)
//                {
//                    initData(msg.obj.toString());
//                }
//            }
//        };

    }



    private  void initData(String s)
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String jsonDataUrl = BaseUrl.BASE_URL+"helper.php?token="+ User.getUser().token+s;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try{
                            if (jsonObject.getInt("status")==1){
                                dialog.dismiss();
                                Message msg= new Message();
                                msg.what = 0x123;
                                msg.obj = jsonObject;
                                myHandler1.sendMessage(msg);
                            }else {
                                Intent intent = new Intent();
                                intent.setClass(Assit.this,Login.class);
                                Assit.this.startActivity(intent);
                                Assit.this.finish();
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



    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }
    public class onItemClickListener implements  OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            AssitEntity  assitEntity = assitEntities.get(i);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("assitDetail",assitEntity);
            intent.setClass(Assit.this,AssitDetail.class);
            intent.putExtras(bundle);
            Assit.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

        }
    }



    /*处理弹出菜单*/
    private void popmenu()
    {
       final PopMenu popMenu = new PopMenu(this,(ImageButton)findViewById(R.id.select),4,s);
        popMenu.lvPopupList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                   final int position, long id) {
                popMenu.pwMyPopWindow.dismiss();
                dialog.show();
                User.getUser().flag = position;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);

                            switch (position)
                            {
                                case 0:
//                                    //msg.obj = "&lv=3-4";
                                    myHandler.sendEmptyMessage(THREE_FOUR);
                                    break;
                                case 1:
//                                    //msg.obj="&lv=4-5";
                                    myHandler.sendEmptyMessage(FOUR_FIVE);
                                    break;
                                case 2:
//                                    //msg.obj="&lv=5-6";
                                    myHandler.sendEmptyMessage(FIVR_SIX);
                                    break;
                                case 3:
//                                    //msg.obj="&lv3-6";
                                    myHandler.sendEmptyMessage(ALL);
                                    break;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
    private Handler myHandler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0x123){
                try{
                    JSONArray jsonArray = ((JSONObject)msg.obj).getJSONArray("lists");
                    JSONArray targetArray ;
                    JSONArray readyArray;
                    JSONArray operateArray;
                    JSONArray noticeArray;

                    for(int i=0;i<jsonArray.length();i++)
                    {

                        String s1="";
                        String s2="";
                        String s3="";
                        String s4="";
                        AssitEntity assitEntity = new AssitEntity();
                        assitEntity.setTitle(jsonArray.getJSONObject(i).getString("title"));
                        assitEntity.setImgUrl(jsonArray.getJSONObject(i).getString("img"));
                        assitEntity.setPulisher(jsonArray.getJSONObject(i).getString("fabu"));
                        targetArray = jsonArray.getJSONObject(i).getJSONArray("target");
                        for(int count = 0;count<targetArray.length();count++)
                        {
                            s1 = s1+targetArray.get(count).toString()+"\n";
                        }
                        readyArray=jsonArray.getJSONObject(i).getJSONArray("getready");
                        for(int count = 0;count<readyArray.length();count++)
                        {
                            s2 = s2+readyArray.get(count).toString()+"\n";
                        }
                        operateArray=jsonArray.getJSONObject(i).getJSONArray("operate");
                        for(int count = 0;count<operateArray.length();count++)
                        {
                            s3 = s3+operateArray.get(count).toString()+"\n";
                        }
                        noticeArray = jsonArray.getJSONObject(i).getJSONArray("notice");
                        for(int count = 0;count<noticeArray.length();count++)
                        {
                            s4 = s4+noticeArray.get(count).toString()+"\n";
                        }
                        assitEntity.setTarget(s1);
                        assitEntity.setGetReady(s2);
                        assitEntity.setOperate(s3);
                        assitEntity.setNotice(s4);
                        assitEntities.add(assitEntity);
                    }
                    gridAssitAdapter = new AssitGridViewAdapter(Assit.this,assitEntities,mImageLoader);
                    mGridView.setOnItemClickListener(new onItemClickListener());
                    mGridView.setAdapter(gridAssitAdapter);

                }catch (JSONException ex)
                {

                }

            }
        }
    };

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            assitEntities.clear();
            gridAssitAdapter.notifyDataSetChanged();
            switch (msg.what) {
                case THREE_FOUR:

                    //       gridAssitAdapter.notifyDataSetChanged();
                    initData("&lv=3-4");
//                        pullListView.setSelectionfoot();
                    break;

                case FOUR_FIVE:
                    // adapter.notifyDataSetChanged();
                    initData("&lv=4-5");
                    break;
                case FIVR_SIX:
                    initData("&lv=5-6");
                    break;
                case ALL:
                    initData("&lv=3-6");
                    break;
                default:
                    break;
            }
        }

    };
    @Override
    public  boolean onKeyDown(int keyCode,KeyEvent keyEvent)
    {
        if(keyCode == keyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            intent.setClass(Assit.this,Tile.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
        return  false;
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("育儿助手"); //统计页面
        MobclickAgent.onResume(this);
        TCAgent.onPageStart(this, "育儿助手");
        TCAgent.onResume(this);

    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("育儿助手");
        MobclickAgent.onPause(this);
        TCAgent.onPageEnd(this,"育儿助手");
        TCAgent.onPause(this);
    }
}
