package org.yanweiran.app.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.PhotoListEntity;

import java.util.ArrayList;

/**
 * Created by lenov on 14-2-10.
 */
public class PhotoListAdapter   extends BaseAdapter {

    private Context context;
    private ArrayList<PhotoListEntity> photoListEntities = new ArrayList<PhotoListEntity>();
    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    public PhotoListAdapter(ArrayList<PhotoListEntity> photoListEntities,Context context,ImageLoader imageLoader)
    {
        this.imageLoader = imageLoader;
        this.context=context;
        this.photoListEntities=photoListEntities;
        int defaultImageId = R.drawable.fail;
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(defaultImageId)
                .showImageOnFail(defaultImageId)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
    }
    @Override
    public int getCount()
    {
        return photoListEntities.size();
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
        PhotoListEntity photoListEntity = photoListEntities.get(position);
        if(view==null)
        {
            LayoutInflater  inflater = LayoutInflater.from(context);
            view=inflater.inflate(R.layout.photo_cell,null);
            viewHolder  =   new PhotoViewHolder();
            viewHolder.sPhoto   =   (ImageView)view.findViewById(R.id.smallPhoto);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (PhotoViewHolder)view.getTag();
        }
        imageLoader.displayImage(
                photoListEntity.getsPhotoUrl(),
                viewHolder.sPhoto,
                mDisplayImageOptions);
        return view;
    }
    static final  class PhotoViewHolder
    {
        public ImageView  sPhoto;
        public ImageView  bPhoto;
    }

}
