package org.yanweiran.app.myadpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.yanweiran.Login.R;

import java.net.ContentHandler;

/**
 * Created by lenov on 14-2-20.
 */
public class WeekArrangeListViewAdapter extends BaseAdapter {


    private int num;
    private Context context;

    public   WeekArrangeListViewAdapter(int num,Context context)
    {
        this.num=num;
        this.context = context;
    }
    @Override
    public  int getCount()
    {
        return  num;
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
            if(view == null)
            {
                LayoutInflater inflater =LayoutInflater.from(context);
                view = inflater.inflate(R.layout.week_arrange_detail,null);
                viewHolder = new ViewHolder();
                viewHolder.tvOrder=(TextView)view.findViewById(R.id.tvOrder);
                viewHolder.tvTime=(TextView)view.findViewById(R.id.arrangeTime);
                viewHolder.tvContent=(TextView)view.findViewById(R.id.arrangeContent);

                view.setTag(viewHolder);
            }
        else {
                viewHolder = (ViewHolder)view.getTag();
            }

        return  view;
    }

    private final  class  ViewHolder
    {
        private TextView tvOrder;
        private TextView tvTime;
        private TextView tvContent;

    }
}
