package org.yanweiran.app.myadpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.yanweiran.Login.R;

/**
 * Created by lenov on 14-2-10.
 */
public class PhotoListAdapter   extends BaseAdapter {

    private int num;
    private Context context;
    private String[] sPhotoUrl;
    public PhotoListAdapter(Integer num,String[] sPhotoUrl,Context context)
    {
        this.num=num;
        this.context=context;
        this.sPhotoUrl=sPhotoUrl;
    }
    @Override
    public int getCount()
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
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        PhotoViewHolder viewHolder = null;
        if(view==null)
        {
            LayoutInflater  inflater = LayoutInflater.from(context);
            view=inflater.inflate(R.layout.cell_photo,null);

            viewHolder  =   new PhotoViewHolder();
            viewHolder.sPhoto   =   (ImageView)view.findViewById(R.id.smallPhoto);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (PhotoViewHolder)view.getTag();
        }
        loadImageByVolley(viewHolder.sPhoto,sPhotoUrl[position]);
        return view;
    }
    final class PhotoViewHolder
    {
        public ImageView  sPhoto;
        public ImageView  bPhoto;
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
