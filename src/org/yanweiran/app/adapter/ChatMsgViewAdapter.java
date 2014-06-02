package org.yanweiran.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.ChatMsgEntity;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.activity.ChatBigPhoto;

import java.util.ArrayList;


/**
 * Created by lenov on 14-1-30.
 */
public class ChatMsgViewAdapter extends BaseAdapter {


   ArrayList<ChatMsgEntity> chatMsgEntities = new ArrayList<ChatMsgEntity>();
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private static final int ITEMCOUNT = 8;// 消息类型的总数
    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;// 收到对方的消息
        int ILL_GET = 1;
        int TIP_GET = 2;
        int IMVT_TO_MSG = 3;
        int ILL_SEND = 4;
        int TIP_SEND = 5;
        int PHOTO_SEND=6;
        int PHOTO_GET=7;
    }

    public ChatMsgViewAdapter(Context context,ArrayList<ChatMsgEntity> chatMsgEntities,ImageLoader imageLoader) {

        this.context = context;
        this.chatMsgEntities = chatMsgEntities;
        this.imageLoader = imageLoader;
        int defaultImageId = R.drawable.fail;
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
    public int getCount() {
        return chatMsgEntities.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMsgEntity entity = chatMsgEntities.get(position);
        if (entity.getMsgType()==0) {// 收到的消息
                if(entity.getMsgTag().equals("0"))
                {
                    return IMsgViewType.IMVT_COM_MSG;
                }else if(entity.getMsgTag().equals("1")){
                    return IMsgViewType.ILL_GET;
                } else if(entity.getMsgTag().equals("2")){
                     return IMsgViewType.TIP_GET;
                }
                else{
                        return  IMsgViewType.PHOTO_GET;
                }
        }
         else {// 自己发送的消息
            if(entity.getMsgTag().equals("0"))
            {
                return IMsgViewType.IMVT_TO_MSG;
            }else if(entity.getMsgTag().equals("1")){
                return IMsgViewType.ILL_SEND;
            } else if(entity.getMsgTag().equals("2")) {
                return IMsgViewType.TIP_SEND;
            }else {
                  return  IMsgViewType.PHOTO_SEND;
            }
        }
    }

    /**
     * Item类型的总数
     */
    @Override
    public int getViewTypeCount() {
        return ITEMCOUNT;
    }

        @Override
    public View getView(final int position,View convertView,ViewGroup parent)
        {

            ChatViewHolder viewHolder =null;
             ChatMsgEntity chatMsgEntity = chatMsgEntities.get(position);
            int isComMsg = chatMsgEntity.getMsgType();
            int type = getItemViewType(position);
            LayoutInflater mInflater = LayoutInflater.from(context);
            if (convertView == null) {
                viewHolder = new ChatViewHolder();
              switch (type){
                  case 0:
                              convertView = mInflater.inflate(R.layout.chatleft, null);
                              viewHolder.headImg = (ImageView)convertView.findViewById(R.id.talkHeadL);
                              viewHolder.isComMsg = isComMsg;
                              viewHolder.iconL =(ImageView)convertView.findViewById(R.id.cg_t);
                              viewHolder.tvSendTime = (TextView)convertView.findViewById(R.id.sendTimeL);
                              viewHolder.tvContent = (TextView)convertView.findViewById(R.id.sendContentL);
                              viewHolder.photoImg=(ImageView)convertView.findViewById(R.id.getImgL);
                              if(User.getUser().tag.equals("0")){
                                  viewHolder.iconL.setVisibility(View.VISIBLE);
                              }else {
                                  viewHolder.iconL.setVisibility(View.GONE);
                              }
                      break;
                  case 1:

                              convertView = mInflater.inflate(R.layout.ill_get, null);
                              viewHolder.headImg = (ImageView)convertView.findViewById(R.id.illtalkHeadL);
                              viewHolder.isComMsg = isComMsg;
                              viewHolder.tvSendTime = (TextView)convertView.findViewById(R.id.illsendTimeL);
                              viewHolder.tvContent = (TextView)convertView.findViewById(R.id.illsendContentL);
                              viewHolder.iconL =(ImageView)convertView.findViewById(R.id.ig_t);
                              viewHolder.photoImg=(ImageView)convertView.findViewById(R.id.illGetImg);
                              if(User.getUser().tag.equals("0")){
                                  viewHolder.iconL.setVisibility(View.VISIBLE);
                              }else {
                                  viewHolder.iconL.setVisibility(View.GONE);
                              }
                              break;
                  case 2:
                              convertView = mInflater.inflate(R.layout.tip_get, null);
                              viewHolder.headImg = (ImageView)convertView.findViewById(R.id.tiptalkHeadL);
                              viewHolder.isComMsg = isComMsg;
                              viewHolder.tvSendTime = (TextView)convertView.findViewById(R.id.tipsendTimeL);
                              viewHolder.tvContent = (TextView)convertView.findViewById(R.id.tipsendContentL);
                              viewHolder.iconL =(ImageView)convertView.findViewById(R.id.tg_t);
                              viewHolder.photoImg=(ImageView)convertView.findViewById(R.id.tipGetImg);
                              if(User.getUser().tag.equals("0")){
                                  viewHolder.iconL.setVisibility(View.VISIBLE);
                              }else {
                                  viewHolder.iconL.setVisibility(View.GONE);
                              }
                      break;
                    case 3:
                              convertView = mInflater.inflate(R.layout.chatright, null);
                              viewHolder.headImg = (ImageView)convertView.findViewById(R.id.talkHeadR);
                              viewHolder.isComMsg = isComMsg;
                              viewHolder.tvSendTime = (TextView)convertView.findViewById(R.id.sendTimeR);
                              viewHolder.tvContent = (TextView)convertView.findViewById(R.id.sendContentR);
                              viewHolder.headImg = (ImageView)convertView.findViewById(R.id.talkHeadR);
                              viewHolder.photoImg=(ImageView)convertView.findViewById(R.id.sendImg);
                              viewHolder.iconR =(ImageView)convertView.findViewById(R.id.cs_t);
                              viewHolder.isComMsg = isComMsg;
                                if(User.getUser().tag.equals("1")){
                                    viewHolder.iconR.setVisibility(View.VISIBLE);
                                }else {
                                    viewHolder.iconR.setVisibility(View.GONE);
                                }
                              break;
                  case 4:
                              convertView = mInflater.inflate(R.layout.ill_send, null);
                              viewHolder.headImg = (ImageView)convertView.findViewById(R.id.illtalkHeadR);
                              viewHolder.isComMsg = isComMsg;
                              viewHolder.tvSendTime = (TextView)convertView.findViewById(R.id.illsendTimeR);
                              viewHolder.tvContent = (TextView)convertView.findViewById(R.id.illsendContentR);
                              viewHolder.headImg = (ImageView)convertView.findViewById(R.id.illtalkHeadR);
                              viewHolder.iconR =(ImageView)convertView.findViewById(R.id.is_t);
                              viewHolder.photoImg=(ImageView)convertView.findViewById(R.id.illSendImg);
                              viewHolder.isComMsg = isComMsg;
                              if(User.getUser().tag.equals("1")){
                                  viewHolder.iconR.setVisibility(View.VISIBLE);
                              }else {
                                  viewHolder.iconR.setVisibility(View.GONE);
                              }
                              break;
                  case 5:
                              convertView = mInflater.inflate(R.layout.tip_send, null);
                              viewHolder.headImg = (ImageView)convertView.findViewById(R.id.tiptalkHeadR);
                              viewHolder.isComMsg = isComMsg;
                              viewHolder.tvSendTime = (TextView)convertView.findViewById(R.id.tipsendTimeR);
                              viewHolder.tvContent = (TextView)convertView.findViewById(R.id.tipsendContentR);
                              viewHolder.iconR =(ImageView)convertView.findViewById(R.id.ts_t);
                              viewHolder.photoImg=(ImageView)convertView.findViewById(R.id.tipSendImg);
                              if(User.getUser().tag.equals("1")){
                                  viewHolder.iconR.setVisibility(View.VISIBLE);
                              }else {
                                  viewHolder.iconR.setVisibility(View.GONE);
                              }
                              break;
                  case IMsgViewType.PHOTO_SEND:
                            convertView = mInflater.inflate(R.layout.chatphotosend,null);
                            viewHolder.headImg = (ImageView)convertView.findViewById(R.id.sendPhotoHead);
                            viewHolder.isComMsg = isComMsg;
                            viewHolder.tvSendTime = (TextView)convertView.findViewById(R.id.sendPhotoTime);
                            viewHolder.tvContent = (TextView)convertView.findViewById(R.id.sendPhotoContent);
                            viewHolder.photoImg = (ImageView)convertView.findViewById(R.id.photoSend);
                            viewHolder.iconR =(ImageView)convertView.findViewById(R.id.ps_t);
                      if(User.getUser().tag.equals("1")){
                          viewHolder.iconR.setVisibility(View.VISIBLE);
                      }else {
                          viewHolder.iconR.setVisibility(View.GONE);
                      }
                      break;
                  case  IMsgViewType.PHOTO_GET:
                            convertView = mInflater.inflate(R.layout.chatphotoget,null);
                            viewHolder.headImg = (ImageView)convertView.findViewById(R.id.photoGetHead);
                            viewHolder.tvSendTime = (TextView)convertView.findViewById(R.id.getPhotoTime);
                            viewHolder.tvContent = (TextView)convertView.findViewById(R.id.getPhotoContent);
                            viewHolder.photoImg = (ImageView)convertView.findViewById(R.id.getPhoto);
                            viewHolder.iconL= (ImageView)convertView.findViewById(R.id.pg_t);
                      if(User.getUser().tag.equals("1")){
                          viewHolder.iconL.setVisibility(View.VISIBLE);
                      }else {
                          viewHolder.iconL.setVisibility(View.GONE);
                      }
                      break;
              }

                convertView.setTag(viewHolder);
        }
            else  {
                viewHolder = (ChatViewHolder)convertView.getTag();
            }
            viewHolder.tvSendTime.setText(chatMsgEntity.getDate());
            viewHolder.tvContent.setText(chatMsgEntity.getMessage());
            viewHolder.isComMsg=chatMsgEntity.getMsgType();
            imageLoader.displayImage(
                    chatMsgEntity.getHeadImgUrl(),
                    viewHolder.headImg,
                    mDisplayImageOptions);
            imageLoader.displayImage(chatMsgEntity.getsPhotoUrl(),
                        viewHolder.photoImg,
                        mDisplayImageOptions);
            viewHolder.photoImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("chatMsgEntity",chatMsgEntities.get(position));
                    intent.putExtras(bundle);
                    intent.setClass(context, ChatBigPhoto.class);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    static class ChatViewHolder {
        public TextView tvSendTime;
        public ImageView headImg;
        public TextView tvContent;
        public ImageView iconL;
        private ImageView iconR;
        private ImageView photoImg;
        public int isComMsg=0;
    }

    static  class ImgViewHolder{
        public ImageView imageView;
        public ImageView headImg;
        public TextView tvSendTime;
        public ImageView iconL;
        private ImageView iconR;
        public int isComMsg=0;
    }


}
