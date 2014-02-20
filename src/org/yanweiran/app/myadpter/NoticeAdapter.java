package org.yanweiran.app.myadpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.HttpPackage.HttpUtil;
import org.yanweiran.app.Singleton.FreshOneEntity;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.activity.FreshNewsOne;

/**
 * Created by lenov on 14-2-1.
 */

public   class NoticeAdapter extends BaseAdapter {

    private int num;
    private String[] tid;
    private String nameString[];
    private  String sendTime[];
    private  String msgContent[];
    private  String s_photo1[];
    private  String s_photo2[];
    private  String s_photo3[];
    private  String headImgUrl[];
    private String comment[];
    private  String appre[];
    private Context context;


    public  NoticeAdapter(String[] tid,int num,String[] headImgUrl,String[] nameString,String[] sendTime, String[] msgContent,String[] s_photo1,String[] s_photo2,String[] s_photo3,String[] comment,String[] appre,Context context)
    {
        this.num=num;
        this.tid=tid;
        this.headImgUrl=headImgUrl;
        this.nameString=nameString;
        this.sendTime=sendTime;
        this.s_photo1=s_photo1;
        this.s_photo2=s_photo2;
        this.s_photo3=s_photo3;
        this.msgContent=msgContent;
        this.comment=comment;
        this.appre=appre;
        this.context=context;
    }
    @Override
    public int getCount() {
        return num;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)

    {
         ViewHolder  viewHolder = null;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.teachernotice_single, null);

            viewHolder = new ViewHolder();

           viewHolder.headImg=(RoundImageView)view.findViewById(R.id.noticeHead);
            viewHolder.tvUserName = (TextView)view.findViewById(R.id.noticeName);
            viewHolder.tvSendTime = (TextView)view.findViewById(R.id.noticeTime);
            viewHolder.tvContent = (TextView)view.findViewById(R.id.noticeContent);
            viewHolder.tvComment =(TextView)view.findViewById(R.id.noticeComment);
            viewHolder.tvAppre =(TextView)view.findViewById(R.id.noticeAppre);
            viewHolder.sPhoto1=(ImageView)view.findViewById(R.id.s_photo1);
            viewHolder.sPhoto1=(ImageView)view.findViewById(R.id.s_photo1);
            viewHolder.sPhoto1=(ImageView)view.findViewById(R.id.s_photo1);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.tvUserName.setText(nameString[position]);
        viewHolder.tvSendTime.setText(sendTime[position]);
        viewHolder.tvContent.setText(msgContent[position]);
        loadImageByVolley2(viewHolder.sPhoto1, s_photo1[position]);
        loadImageByVolley3(viewHolder.sPhoto2,s_photo2[position]);
        loadImageByVolley4(viewHolder.sPhoto3,s_photo3[position]);
       loadImageByVolley(viewHolder.headImg,headImgUrl[position]);
        viewHolder.tvComment.setText(" 评论 "+comment[position]);
        viewHolder.tvAppre.setText(" 赞 "+appre[position]);

//        if(s_photo1[position]==null&s_photo2[position]==null&&s_photo3[position]==null)
//        {
//            viewHolder.sPhoto1.setVisibility(View.INVISIBLE);
//            viewHolder.sPhoto2.setVisibility(View.INVISIBLE);
//            viewHolder.sPhoto3.setVisibility(View.INVISIBLE);
//        }


          final   int i = position;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String jsonDataUrl = HttpUtil.BASE_URL + "feedone.php?"+"token="+ User.getUser().token+"&tid="+tid[i];

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try{

                            FreshOneEntity msgEntity = new FreshOneEntity();
                            JSONObject list = jsonObject.getJSONObject("lists");
                            String headImgUrl = list.getString("headimg");

                            Bundle data = new Bundle();
                            msgEntity.setMsgContent(list.getString("message"));
                            msgEntity.setCommNum(list.getString("reply_num") + " 评论");
                            msgEntity.setMsgTime(list.getString("time"));
                            msgEntity.setZanNum(list.getString("zan") + " 赞");
                            msgEntity.setSendName(list.getString("name"));
                            msgEntity.setHeadUrl(list.getString("headimg"));
                            msgEntity.setMsgId(tid[i]);
                            data.putSerializable("singleMsg",msgEntity);
                            Intent intent = new Intent(context, FreshNewsOne.class);
                            intent.putExtras(data);
                            context.startActivity(intent);



                        }
                        catch (JSONException ex)
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
        });

        return view;
    }
    final   class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public TextView tvComment;
        public TextView tvAppre;

       // public  ImageView headImg;
        public  RoundImageView headImg;
        public ImageView sPhoto1;
        public  ImageView sPhoto2;
        public  ImageView sPhoto3;
    }

    /*加载图片函数*/
    public void loadImageByVolley(ImageView imageView,String imgUrl){
        String imageUrl=imgUrl;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, R.drawable.head_photo,R.drawable.head_photo);
        imageLoader.get(imageUrl, listener);
    }

    public void loadImageByVolley2(ImageView imageView,String imgUrl){
        String imageUrl=imgUrl;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, 0,0);
        imageLoader.get(imageUrl, listener);
    }

    public void loadImageByVolley3(ImageView imageView,String imgUrl){
        String imageUrl=imgUrl;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, 0,0);
        imageLoader.get(imageUrl, listener);
    }
    public void loadImageByVolley4(ImageView imageView,String imgUrl){
        String imageUrl=imgUrl;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, 0,0);
        imageLoader.get(imageUrl, listener);
    }



}
