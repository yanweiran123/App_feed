package org.yanweiran.app.chatadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.yanweiran.Login.R;

import java.util.List;

/**
 * Created by lenov on 14-1-30.
 */
public class ChatMsgViewAdapter extends BaseAdapter {


    private String[]    headUrl;
    private String[]    talkContent;
    private Integer[]    isComMsg;
    private String[]    sendTime;
    private Integer  num;
    private Context context;

    private static final int ITEMCOUNT = 2;// 消息类型的总数
    private List<ChatMsgEntity> coll;// 消息对象数组

    public ChatMsgViewAdapter(Context context,Integer num,Integer[] isComMsg,String[] headUrl,String[] talkContent,String[] sendTime) {

        this.context = context;
        this.num = num;
        this.isComMsg = isComMsg;
        this.sendTime = sendTime;
        this.headUrl = headUrl;
        this.talkContent=talkContent;


    }
    @Override
    public int getCount() {
        return num;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * Item类型的总数
     */

        @Override
    public View getView(int position,View convertView,ViewGroup parent)
        {

            ChatViewHolder viewHolder =null;
            LayoutInflater mInflater = LayoutInflater.from(context);
            if (convertView == null) {
                if (isComMsg[position]==0) {
                    convertView = mInflater.inflate(R.layout.chatright, null);
                } else {
                    convertView = mInflater.inflate(
                            R.layout.chatleft, null);
                }


                viewHolder = new ChatViewHolder();
                viewHolder.tvSendTime = (TextView)convertView.findViewById(R.id.sendTime);
                viewHolder.tvContent = (TextView)convertView.findViewById(R.id.send_content);
                viewHolder.headImg = (ImageView)convertView.findViewById(R.id.talkHead);
                convertView.setTag(viewHolder);
        }
            else {
                viewHolder = (ChatViewHolder)convertView.getTag();
            }

            viewHolder.tvContent.setText(talkContent[position]);
            viewHolder.tvSendTime.setText(sendTime[position]);
            loadImageByVolley(viewHolder.headImg,headUrl[position]);

            return convertView;


        }
    static class ChatViewHolder {
        public TextView tvSendTime;
        public ImageView headImg;
        public TextView tvContent;
       // public ImageView icon;
        //public String isComMsg;
    }

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
    public void loadImageByVolley1(ImageView imageView,String imgUrl){
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
}
