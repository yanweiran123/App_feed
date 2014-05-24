package org.yanweiran.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.yanweiran.Login.R;
import org.yanweiran.app.MyWidget.RoundImageView;
import org.yanweiran.app.Singleton.NoticeCommentEntity;

import java.util.ArrayList;

/**
 * Created by lenov on 14-2-16.
 */
public class NoticeCommentAdapter extends BaseAdapter {

    private ArrayList<NoticeCommentEntity> freshNewCommentEntities =  new ArrayList<NoticeCommentEntity>();
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    public NoticeCommentAdapter(ArrayList<NoticeCommentEntity> freshNewCommentEntities, Context context,ImageLoader imageLoader)

    {
            this.freshNewCommentEntities= freshNewCommentEntities;
            this.context = context;
            this.imageLoader = imageLoader;
    }
    @Override
    public int getCount()
    {
        return  freshNewCommentEntities.size();
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
        NoticeCommentEntity freshNewCommentEntity = freshNewCommentEntities.get(position);
        if(view==null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.comment_cell,null);
            viewHolder = new FreshOneViewHolder();
            viewHolder.tvName = (TextView)view.findViewById(R.id.commName);
            viewHolder.tvContent = (TextView)view.findViewById(R.id.commContent);
            viewHolder.tvTime=(TextView)view.findViewById(R.id.commTime);
            viewHolder.headImg=(RoundImageView)view.findViewById(R.id.commHead);
            viewHolder.tag  =(ImageView)view.findViewById(R.id.tag);
            view.setTag(viewHolder);

        }else
        {
            viewHolder = (FreshOneViewHolder)view.getTag();
        }
        if(freshNewCommentEntity.getTag()==0){
            viewHolder.tag.setVisibility(View.GONE);
        }
        if(freshNewCommentEntity.getTag()==1){
            viewHolder.tag.setVisibility(View.VISIBLE);
        }

        if(position ==freshNewCommentEntities.size()-1){
            view.setBackgroundResource(R.drawable.notice_detail_progress);
        }else {
            view.setBackgroundResource(R.color.white);
        }

        viewHolder.tvName.setText(freshNewCommentEntity.getCommName());
        viewHolder.tvTime.setText(freshNewCommentEntity.getCommTime());
        viewHolder.tvContent.setText(freshNewCommentEntity.getCommContent());
        imageLoader.displayImage(
                freshNewCommentEntity.getHeadUrl(),
                viewHolder.headImg,
                mDisplayImageOptions);

        return  view;
    }
    final static class FreshOneViewHolder
    {
        private RoundImageView headImg;
        private TextView    tvName;
        private TextView tvContent;
        private TextView tvTime;
        private ImageView tag;
    }



}
