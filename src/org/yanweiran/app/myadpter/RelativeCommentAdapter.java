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
 * Created by lenov on 14-2-9.
 */
public class RelativeCommentAdapter extends BaseAdapter {

    private int num;
    private String[]    imgUrl;
    private String[]    name;
    private String[]    time;
    private String[]    commContent;
    private String[]    commWhat;
    private Context context;




    public RelativeCommentAdapter(Integer num,String[] imgUrl, String[] name, String[] time, String[] commContent, String[] commWhat, Context context)

    {
        this.num    =   num;
        this.imgUrl = imgUrl;
        this.name = name;
        this.time=time;
        this.commContent =commContent;
        this.commWhat = commWhat;
        this.context =context;
    }

    @Override
    public  int  getCount()
    {
            return num;
    }

    @Override
    public Object getItem(int position)
    {
        return  null;
    }
    @Override
    public  long getItemId(int postion)
    {
        return  0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        RelaCommViewHolder viewHolder = null;
        if(viewHolder == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.my_single_comment,null);

            viewHolder = new RelaCommViewHolder();

            viewHolder.tvCommName = (TextView)view.findViewById(R.id.userName);
            viewHolder.tvCommTime = (TextView)view.findViewById(R.id.commentTime);
            viewHolder.tvCommContent = (TextView)view.findViewById(R.id.commContent);
            viewHolder.tvCommWhat = (TextView)view.findViewById(R.id.commWhat);
            viewHolder.tvHeadImg=(ImageView)view.findViewById(R.id.headImg);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (RelaCommViewHolder)view.getTag();
        }
        loadImageByVolley(viewHolder.tvHeadImg,imgUrl[position]);
        viewHolder.tvCommName.setText(name[position]);
        viewHolder.tvCommTime.setText(time[position]);
        viewHolder.tvCommContent.setText(commContent[position]);
        viewHolder.tvCommWhat.setText(commWhat[position]);
        return  view;
    }

    final class RelaCommViewHolder
    {
        public ImageView tvHeadImg;
        public TextView tvCommName;
        public TextView tvCommTime;
        public TextView tvCommContent;
        public TextView tvCommWhat;
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
