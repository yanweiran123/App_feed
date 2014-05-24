package org.yanweiran.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.yanweiran.Login.R;

import java.util.List;
import java.util.Map;

/**
 * Created by lenov on 14-3-18.
 */
public class ChatEmojiGridviewAdapter extends BaseAdapter {
    private String[] strArray;
    private Context context;

    public ChatEmojiGridviewAdapter(String[] strArray,Context context)
    {
        this.strArray=strArray;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return strArray.length;
    }
    @Override
    public Object getItem(int position)
    {
        return  null;
    }
    @Override
    public long getItemId( int position)
    {
        return 0;
    }

    @Override
    public View getView(int position,View view,ViewGroup viewGroup)
    {
        ViewHolder viewHolder = null;
        if(view ==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.chatface_cell,null);
            viewHolder = new ViewHolder();
            viewHolder.tvStrName = (TextView)view.findViewById(R.id.singleText);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }
            viewHolder.tvStrName.setText(strArray[position]);
            return  view;
    }

    final private class ViewHolder
    {
        private TextView tvStrName;
    }

}
