package org.yanweiran.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.PublicNewsImgEntity;

import java.util.ArrayList;

/**
 * Created by lenov on 14-5-3.
 */
public class PublicNewsImgAdapter extends BaseAdapter {
    private ArrayList<PublicNewsImgEntity>  publicNewsImgEntityArrayList;
    private Context context;

    public PublicNewsImgAdapter(Context context,ArrayList<PublicNewsImgEntity> publicNewsImgEntityArrayList){
                this.context = context;
                this.publicNewsImgEntityArrayList = publicNewsImgEntityArrayList;
    }

    @Override
    public int getCount(){
        return publicNewsImgEntityArrayList.size();
    }

    @Override
    public  Object getItem(int position){
        return  publicNewsImgEntityArrayList.get(position);
    }

    @Override
    public  long getItemId(int position){
        return  position;
    }

    @Override
    public View getView(int position,View view,ViewGroup viewGroup){
       ViewHolder viewHolder =null;
        PublicNewsImgEntity publicNewsImgEntity = publicNewsImgEntityArrayList.get(position);
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater  inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.public_photo_cell,null);
            viewHolder.imgView = (ImageView)view.findViewById(R.id.smallPhoto);

            view.setTag(viewHolder);
        }else {
            viewHolder =(ViewHolder)view.getTag();
        }

        viewHolder.imgView.setImageBitmap(getBitmap(publicNewsImgEntity.getSmallBitmap()));
        return  view;
    }

    public static Bitmap getBitmap(byte[] data){
        return BitmapFactory.decodeByteArray(data, 0, data.length);//从字节数组解码位图
    }
    private static class ViewHolder{
        private ImageView  imgView;
    }
}
