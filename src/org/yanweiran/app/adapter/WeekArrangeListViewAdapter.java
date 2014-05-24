package org.yanweiran.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.ArrangeEntity;

import java.util.List;

/**
 * Created by lenov on 14-2-20.
 */
public class WeekArrangeListViewAdapter extends BaseAdapter {



    private Context context;
    private List<ArrangeEntity> arrangeList;


    public   WeekArrangeListViewAdapter( List<ArrangeEntity> arrangeList,Context context)
    {

        this.arrangeList = arrangeList;
        this.context = context;
    }
    @Override
    public  int getCount()
    {
        return  arrangeList.size();
    }
    @Override
    public Object getItem(int position)
    {
        return  null;
    }

    @Override
    public long getItemId(int position)
    {
        return  0;
    }

    public View getView(int position, View view, ViewGroup viewGroup)
    {
            ViewHolder viewHolder = null;
            ArrangeEntity entity = arrangeList.get(position);
            if(view == null)
            {
                LayoutInflater inflater =LayoutInflater.from(context);
                view = inflater.inflate(R.layout.week_arrange_detail,null);
                viewHolder = new ViewHolder();
                viewHolder.tvTime=(TextView)view.findViewById(R.id.arrangeTime);
                viewHolder.tvContent=(TextView)view.findViewById(R.id.arrangeContent);
                viewHolder.imageView=(ImageView)view.findViewById(R.id.border);
                view.setTag(viewHolder);
            }
        else {
                viewHolder = (ViewHolder)view.getTag();
            }
        viewHolder.imageView.setImageResource(entity.getStyleColorId());
        viewHolder.tvTime.setBackgroundResource(entity.getStyleImgId());
        viewHolder.tvContent.setText(entity.getContent());
        viewHolder.tvTime.setText(entity.getTime());
        return  view;
    }

    private final  class  ViewHolder
    {
        private ImageView imageView;
        private TextView tvTime;
        private TextView tvContent;

    }
}
