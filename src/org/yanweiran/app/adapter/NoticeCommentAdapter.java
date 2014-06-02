package org.yanweiran.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.MyWidget.RoundImageView;
import org.yanweiran.app.Singleton.BaseUrl;
import org.yanweiran.app.Singleton.NoticeCommentEntity;
import org.yanweiran.app.Singleton.PublicType;
import org.yanweiran.app.Singleton.User;

import java.util.ArrayList;

/**
 * Created by lenov on 14-2-16.
 */
public class NoticeCommentAdapter extends BaseAdapter {
    private ArrayList<NoticeCommentEntity> freshNewCommentEntities =  new ArrayList<NoticeCommentEntity>();
    private Context context;
    private ImageLoader imageLoader;
    private TextView comm;
    private DisplayImageOptions mDisplayImageOptions;
    private TextView no_comment;
    public NoticeCommentAdapter(ArrayList<NoticeCommentEntity> freshNewCommentEntities, Context context,ImageLoader imageLoader,TextView comm,TextView no_comment)
    {
            this.freshNewCommentEntities= freshNewCommentEntities;
            this.context = context;
            this.imageLoader = imageLoader;
            this.comm = comm;
            this.no_comment = no_comment;
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
    public View getView(final int position,View view,ViewGroup viewGroup)
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
            viewHolder.delete = (ImageButton)view.findViewById(R.id.delete);
            view.setTag(viewHolder);

        }else
        {
            viewHolder = (FreshOneViewHolder)view.getTag();
        }
        if(freshNewCommentEntity.getTag()==0){
            viewHolder.tag.setVisibility(View.GONE);
            viewHolder.tvName.setTextColor(context.getResources().getColor(R.color.black));
        }
        if(freshNewCommentEntity.getTag()==1){
            viewHolder.tag.setVisibility(View.VISIBLE);
            viewHolder.tvName.setTextColor(context.getResources().getColor(R.color.red));
        }

        if(position ==freshNewCommentEntities.size()-1){
            view.setBackgroundResource(R.drawable.notice_detail_progress);
        }else {
            view.setBackgroundResource(R.color.white);
        }

        if(freshNewCommentEntity.getIsmy()==1){
            viewHolder.delete.setVisibility(View.VISIBLE);
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteClick(position);
                }
            });
        }else {
            viewHolder.delete.setVisibility(View.GONE);
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
        private ImageButton delete;
    }

    public  void deleteClick(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("德蒙家园通")
                .setMessage("是否删除此条评论?");
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteSend(position);
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }
    public  void deleteSend(final int position){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String jsonDataUrl = BaseUrl.BASE_URL + "delcomment.php?token="+ User.getUser().token+
                "&tid="+freshNewCommentEntities.get(position).getTid()+"&rid="+freshNewCommentEntities.get(position).getRid();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                     try {
                         if(jsonObject.getInt("succ")==1){
                             freshNewCommentEntities.remove(position);
                             notifyDataSetChanged();
                             comm.setText( freshNewCommentEntities.size()+" 评论");
                             if (freshNewCommentEntities.size()==0){
                                 no_comment.setVisibility(View.VISIBLE);
                             }else {
                                 no_comment.setVisibility(View.GONE);
                             }
                             PublicType.getPublicType().TweetComm = Integer.toString(freshNewCommentEntities.size());
                             Toast.makeText(context,"已删除该评论",Toast.LENGTH_SHORT).show();

                         }
                     }catch (JSONException ex){
                         Toast.makeText(context,"删除失败 ",Toast.LENGTH_SHORT).show();
                     }
                }
            },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}
