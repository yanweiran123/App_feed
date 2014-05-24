package org.yanweiran.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import com.android.volley.toolbox.Volley;

import org.yanweiran.Login.R;
import org.yanweiran.app.MyWidget.RoundImageView;
import org.yanweiran.app.Singleton.NoticeEntity;
import org.yanweiran.app.Singleton.PublicType;
import org.yanweiran.app.activity.TweetDetail;
import org.yanweiran.app.activity.TweetNoticeSinglePhoto;
;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import java.util.ArrayList;

/**
 * Created by lenov on 14-2-1.
 */

public   class TweetAdapter extends BaseAdapter {

    private ArrayList<NoticeEntity> noticeArrayList = new ArrayList<NoticeEntity>();
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;




    public TweetAdapter(ArrayList<NoticeEntity> noticeArrayList, Context context, ImageLoader imageLoader)
    {
        this.noticeArrayList = noticeArrayList;
        this.context=context;
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
    public int getCount() {
        return noticeArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup)

    {
        final   NoticeEntity  noticeEntity = noticeArrayList.get(position);
        final ViewHolder  viewHolder ;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.notice_cell, null);
            viewHolder = new ViewHolder();
            viewHolder.tag = (ImageView)view.findViewById(R.id.tag);
            viewHolder.headImg=(RoundImageView)view.findViewById(R.id.noticeHead);
            viewHolder.tvUserName = (TextView)view.findViewById(R.id.noticeName);
            viewHolder.tvSendTime = (TextView)view.findViewById(R.id.noticeTime);
            viewHolder.tvContent = (TextView)view.findViewById(R.id.noticeContent);
            viewHolder.tvComment =(TextView)view.findViewById(R.id.noticeComment);
            viewHolder.tvAppre =(TextView)view.findViewById(R.id.noticeAppre);
            viewHolder.imgZan = (ImageView)view.findViewById(R.id.isZan);
            viewHolder.sPhoto1=(ImageView)view.findViewById(R.id.s_photo1);
            viewHolder.sPhoto2=(ImageView)view.findViewById(R.id.s_photo2);
            viewHolder.sPhoto3=(ImageView)view.findViewById(R.id.s_photo3);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }

        if(!noticeEntity.getS_photo1().equals(""))
        {
            viewHolder.sPhoto1.setVisibility(View.VISIBLE);
            imageLoader.displayImage(
                    noticeEntity.getS_photo1(),
                    viewHolder.sPhoto1,
                    mDisplayImageOptions);
            viewHolder.IMG_NUM = 1;
            viewHolder.sPhoto1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PublicType.getPublicType().IMG_INDEX=0;
                    PublicType.getPublicType().IMG_NUM = viewHolder.IMG_NUM;
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("noticeEntity",noticeEntity);
                    intent.putExtras(bundle);
                    intent.setClass(context, TweetNoticeSinglePhoto.class);
                    context.startActivity(intent);
                }
            });

        }else {
            viewHolder.sPhoto1.setVisibility(View.GONE);
        }
        if (!noticeEntity.getS_photo2().equals("")){

            viewHolder.sPhoto2.setVisibility(View.VISIBLE);
            imageLoader.displayImage(
                    noticeEntity.getS_photo2(),
                    viewHolder.sPhoto2,
                    mDisplayImageOptions);
            viewHolder.sPhoto2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PublicType.getPublicType().IMG_INDEX=1;
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("noticeEntity",noticeEntity);
                    intent.putExtras(bundle);
                    intent.setClass(context, TweetNoticeSinglePhoto.class);
                    context.startActivity(intent);
                }
            });
        }else {
            viewHolder.sPhoto2.setVisibility(View.GONE);
        }
        if(!noticeEntity.getS_photo3().equals("")){
            viewHolder.sPhoto3.setVisibility(View.VISIBLE);
            imageLoader.displayImage(
                    noticeEntity.getS_photo3(),
                    viewHolder.sPhoto3,
                    mDisplayImageOptions);
            viewHolder.sPhoto3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PublicType.getPublicType().IMG_INDEX=2;
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("noticeEntity",noticeEntity);
                    intent.putExtras(bundle);
                    intent.setClass(context, TweetNoticeSinglePhoto.class);
                    context.startActivity(intent);
                }
            });

        }else {
            viewHolder.sPhoto3.setVisibility(View.GONE);
        }

        viewHolder.tvUserName.setText(noticeEntity.getName());
        viewHolder.tvSendTime.setText(noticeEntity.getSendTime());
        viewHolder.tvContent.setText(noticeEntity.getMsgContent());
        viewHolder.headImg.setTag(noticeEntity.getHeadImgUrl());
        imageLoader.displayImage(
                noticeEntity.getHeadImgUrl(),
                viewHolder.headImg,
                mDisplayImageOptions);
        viewHolder.tvComment.setText(" 评论 "+noticeEntity.getReplyNum());
        viewHolder.tvAppre.setText(" 赞 "+noticeEntity.getAppre());
        if(noticeEntity.getIsZan() ==1)
        {
            viewHolder.imgZan.setImageResource(R.drawable.heart1);
        }
        if(noticeEntity.getIsZan()==0) {
            viewHolder.imgZan.setImageResource(R.drawable.heart);
        }
        if(noticeEntity.getTag()==1){
            viewHolder.tag.setVisibility(View.VISIBLE);
        }else {
            viewHolder.tag.setVisibility(View.GONE);
        }
        return view;
    }
   private static final   class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public TextView tvComment;
        public TextView tvAppre;
        public ImageView imgZan;
        public ImageView tag;
        public  RoundImageView headImg;
        public ImageView sPhoto1;
        public  ImageView sPhoto2;
        public  ImageView sPhoto3;
        public  int IMG_NUM;
    }








}
