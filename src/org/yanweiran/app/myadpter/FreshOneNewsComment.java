package org.yanweiran.app.myadpter;

import android.content.Context;
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

/**
 * Created by lenov on 14-2-16.
 */
public class FreshOneNewsComment extends BaseAdapter {

    private String[] commName;
    private  int num;
    private String[] headUrl;
    private String[] commContent;
    private String[]  commTime;
    private Context context;

    public FreshOneNewsComment(int num,String[] commName,String[] headUrl,String[] commContent,String[] commTime,Context context)
    {
            this.num=num;
            this.commName = commName;
            this.headUrl = headUrl;
            this.commContent = commContent;
            this.commTime = commTime;
        this.context = context;
    }
    @Override
    public int getCount()
    {
        return  num;
    }
    @Override
    public  Object getItem(int position)
    {
        return  null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position,View view,ViewGroup viewGroup)
    {

        FreshOneViewHolder viewHolder = null;

        if(view==null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.fresh_one_comment,null);
            viewHolder = new FreshOneViewHolder();
            viewHolder.tvName = (TextView)view.findViewById(R.id.commName);
            viewHolder.tvContent = (TextView)view.findViewById(R.id.commContent);
            viewHolder.tvTime=(TextView)view.findViewById(R.id.commTime);
            viewHolder.headImg=(RoundImageView)view.findViewById(R.id.commHead);

            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (FreshOneViewHolder)view.getTag();
        }

        viewHolder.tvName.setText(commName[position]);
        viewHolder.tvTime.setText(commTime[position]);
        viewHolder.tvContent.setText(commContent[position]);
        loadImageByVolley(viewHolder.headImg,headUrl[position]);

        return  view;
    }
    final private class FreshOneViewHolder
    {
        private RoundImageView headImg;
        private TextView    tvName;
        private TextView tvContent;
        private TextView tvTime;
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
