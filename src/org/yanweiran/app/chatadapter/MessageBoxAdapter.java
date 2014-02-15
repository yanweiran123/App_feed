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
import org.yanweiran.app.Singleton.ChatObject;
import org.yanweiran.app.activity.MessageSingle;

/**
 * Created by lenov on 14-2-13.
 */
public class MessageBoxAdapter  extends BaseAdapter {

    private Integer num;
    private String[]   msgHead;
    private String[]    msgName;
    private String[]    msgTime;
    private String[]    lastTalk;
    private Integer[]   fid;
    private Context context;
    public MessageBoxAdapter(Integer num,Integer[] fid ,String[]  msgHead,String[] msgName,String[] lastTalk,String[] msgTime ,Context  context)
    {
                this.num = num;
                this.fid = fid;
                this.msgHead=msgHead;
                this.msgName=msgName;
                this.lastTalk=lastTalk;
                this.msgTime = msgTime;
                this.context = context;

    }
    @Override
    public int  getCount()
    {
        return num;
    }

    @Override
    public Object getItem(int position)
    {
            return  null;
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public View getView(int position,View view,ViewGroup viewGroup)
    {
       MsgViewHolder    viewHolder = null;
        final  int i = position;
        if(view==null)
        {
            LayoutInflater inflater =  LayoutInflater.from(context);
            view = inflater.inflate(R.layout.msgbox_single,null);

            viewHolder = new MsgViewHolder();

            viewHolder.tvName = (TextView)view.findViewById(R.id.msgName);
            viewHolder.tvTime = (TextView)view.findViewById(R.id.msgTime);
            viewHolder.tvLastTalk = (TextView)view.findViewById(R.id.msgLast);
            viewHolder.tvImageView = (ImageView)view.findViewById(R.id.msgHead);
            view.setTag(viewHolder);

        }
        else{

            viewHolder = (MsgViewHolder)view.getTag();

        }
        viewHolder.tvName.setText(msgName[position]);
        viewHolder.tvTime.setText(msgTime[position]);
        viewHolder.tvLastTalk.setText(lastTalk[position]);
        loadImageByVolley(viewHolder.tvImageView,msgHead[position]);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatObject chatObject = new ChatObject();
                chatObject.setName(msgName[i]);
                chatObject.setFid(fid[i]);
                Intent intent = new Intent(context, MessageSingle.class);
                intent.putExtra("talkPerson", chatObject);
                context.startActivity(intent);
            }
        });

        return  view;
    }
    final class MsgViewHolder
    {
        private TextView tvName;
        private TextView    tvTime;
        private TextView    tvLastTalk;
        private ImageView   tvImageView;

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
}
