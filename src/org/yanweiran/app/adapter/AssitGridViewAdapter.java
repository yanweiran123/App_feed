package org.yanweiran.app.adapter;

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
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.AssitEntity;

import java.util.ArrayList;

/**
 * Created by lenov on 14-3-4.
 */
public class AssitGridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AssitEntity> assitEntities;
    private RequestQueue mQueue;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;

    public AssitGridViewAdapter(Context context, ArrayList<AssitEntity> assitEntities,ImageLoader imageLoader)
    {
        this.context = context;
        this.assitEntities = assitEntities;
        int defaultImageId = R.drawable.fail;
        this.imageLoader = imageLoader;
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(defaultImageId)
                .showImageOnFail(defaultImageId)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
    }
    @Override
    public  int getCount()
    {
        return  assitEntities.size();
    }

    @Override
    public Object getItem(int position)
    {
        return  assitEntities.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }
    @Override
    public View getView(int position,View view,ViewGroup viewGroup)
    {
        ViewHolder viewHolder= null;
        AssitEntity assitEntity = assitEntities.get(position);

        if (view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.assit_cell,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)view.findViewById(R.id.icon);
            viewHolder.textView = (TextView)view.findViewById(R.id.text);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.textView.setText(assitEntity.getTitle());
        imageLoader.displayImage(
                assitEntity.getImgUrl(),
                viewHolder.imageView,
                mDisplayImageOptions);
        return  view;
    }

    final private  class ViewHolder
    {
        private ImageView imageView;
        private TextView textView;
    }



}
