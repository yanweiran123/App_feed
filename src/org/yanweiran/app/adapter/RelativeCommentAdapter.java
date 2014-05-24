package org.yanweiran.app.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.yanweiran.Login.R;
import org.yanweiran.app.MyWidget.RoundImageView;
import org.yanweiran.app.Singleton.RelativeCommentEntity;

import java.util.ArrayList;

/**
 * Created by lenov on 14-2-9.
 */
public class RelativeCommentAdapter extends BaseAdapter {

    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private ArrayList<RelativeCommentEntity> commentEntities = new ArrayList<RelativeCommentEntity>();
    private Context context;




    public RelativeCommentAdapter(ArrayList<RelativeCommentEntity> commentEntities , Context context,ImageLoader imageLoader)

    {
        this.commentEntities = commentEntities;
        this.context =context;
        int defaultImageId = R.drawable.indexicon;
        this.imageLoader = imageLoader;
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showStubImage(defaultImageId)
                .showImageForEmptyUri(defaultImageId)
                .showImageOnFail(defaultImageId)
                .cacheInMemory()
                .cacheOnDisc()
                .resetViewBeforeLoading()
                .build();
    }

    @Override
    public  int  getCount()
    {
            return commentEntities.size();
    }

    @Override
    public Object getItem(int position)
    {
        return  null;
    }
    @Override
    public  long getItemId(int position)
    {
        return  0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        RelaCommViewHolder viewHolder = null;
        RelativeCommentEntity relativeCommentEntity = commentEntities.get(position);
        if(viewHolder == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.relativecomment_cell,null);
            viewHolder = new RelaCommViewHolder();
            viewHolder.tvCommName = (TextView)view.findViewById(R.id.userName);
            viewHolder.tvCommTime = (TextView)view.findViewById(R.id.commentTime);
            viewHolder.tvCommContent = (TextView)view.findViewById(R.id.commContent);
            viewHolder.tvCommWhat = (TextView)view.findViewById(R.id.commWhat);
            viewHolder.tvHeadImg=(RoundImageView)view.findViewById(R.id.headImg);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (RelaCommViewHolder)view.getTag();
        }
        imageLoader.displayImage(
               relativeCommentEntity.getImgUrl(),
                viewHolder.tvHeadImg,
                mDisplayImageOptions);
        viewHolder.tvCommName.setText(relativeCommentEntity.getName());
        viewHolder.tvCommTime.setText(relativeCommentEntity.getTime());
        viewHolder.tvCommContent.setText(relativeCommentEntity.getCommContent());
        viewHolder.tvCommWhat.setText(relativeCommentEntity.getCommWhat());
        return  view;
    }

     static class RelaCommViewHolder
    {
        public RoundImageView tvHeadImg;
        public TextView tvCommName;
        public TextView tvCommTime;
        public TextView tvCommContent;
        public TextView tvCommWhat;
    }


}
