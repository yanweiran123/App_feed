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
import org.yanweiran.app.Singleton.PublicType;
import org.yanweiran.app.Singleton.RecentTalkEntity;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.activity.MyApplication;

import java.util.ArrayList;

/**
 * Created by lenov on 14-2-13.
 */
public class RecentTalkAdapter extends BaseAdapter {


    private ArrayList<RecentTalkEntity> recentTalkEntities = new ArrayList<RecentTalkEntity>();
    private Context context;

    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private MyApplication application;
    public RecentTalkAdapter(ArrayList<RecentTalkEntity>  recentTalkcEntities, Context context,ImageLoader imageLoader)
    {
                this.recentTalkEntities = recentTalkcEntities;
                this.context = context;
                //application = (MyApplication)context.getApplicationContext();
                int defaultImageId = R.drawable.indexicon;
                this.imageLoader = imageLoader;
                mDisplayImageOptions = new DisplayImageOptions.Builder()
                        .showStubImage(defaultImageId)
                        .showImageForEmptyUri(defaultImageId)
                        .showImageOnFail(defaultImageId)
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .resetViewBeforeLoading(true)
                        .build();

    }

    @Override
    public int  getCount()
    {
        return recentTalkEntities.size();
    }

    @Override
    public Object getItem(int position)
    {
            return  null;
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public View getView(int position,View view,ViewGroup viewGroup)
    {
        MsgViewHolder    viewHolder = null;
        RecentTalkEntity recentTalkEntity=recentTalkEntities.get(position);;

        if(view==null)
        {
            LayoutInflater inflater =  LayoutInflater.from(context);
            view = inflater.inflate(R.layout.msgbox_cell,null);
            viewHolder = new MsgViewHolder();
            viewHolder.tvName = (TextView)view.findViewById(R.id.msgName);
            viewHolder.tvTime = (TextView)view.findViewById(R.id.msgTime);
            viewHolder.tvLastTalk = (TextView)view.findViewById(R.id.msgLast);
            viewHolder.tvImageView = (ImageView)view.findViewById(R.id.msgHead);
            viewHolder.tag = (ImageView)view.findViewById(R.id.tag);
            viewHolder.readnote = (ImageView)view.findViewById(R.id.readnote);
            viewHolder.readnote2 =(ImageView)view.findViewById(R.id.unread);
            view.setTag(viewHolder);

        }
        else{
            viewHolder = (MsgViewHolder)view.getTag();
        }
        if(recentTalkEntity.getIdentity() == 1){
            viewHolder.tag.setVisibility(View.VISIBLE);
        }else {
            viewHolder.tag.setVisibility(View.GONE);
        }
        if(recentTalkEntity.getStatus()==1){
            viewHolder.readnote.setVisibility(View.VISIBLE);
            viewHolder.readnote2.setVisibility(View.VISIBLE);
            User.getUser().news++;
            if(User.getUser().tag.equals("1")){
                User.getUser().classEntityList.get(PublicType.getPublicType().classPosition).setClassNew(User.getUser().news);
            }

        }else {
            viewHolder.readnote.setVisibility(View.GONE);
            viewHolder.readnote2.setVisibility(View.GONE);

        }
        viewHolder.tvName.setText(recentTalkEntity.getMsgName());
        viewHolder.tvTime.setText(recentTalkEntity.getMsgTime());
        viewHolder.tvLastTalk.setText(recentTalkEntity.getLastTalk());
        imageLoader.displayImage(
                recentTalkEntity.getMsgHead(),
                viewHolder.tvImageView,
                mDisplayImageOptions);





        return  view;
    }
    static class MsgViewHolder
    {
        private TextView tvName;
        private TextView    tvTime;
        private TextView    tvLastTalk;
        private ImageView   tvImageView;
        private ImageView tag;
        private ImageView readnote;
        private ImageView readnote2;

    }

}
