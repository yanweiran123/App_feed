package org.yanweiran.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.ClassEntity;

import java.util.ArrayList;

/**
 * Created by lenov on 14-2-25.
 */
public class ClassListviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ClassEntity> classEntityArrayList;

    public ClassListviewAdapter(ArrayList<ClassEntity> classEntityArrayList,Context context)
    {
        this.context = context;

        this.classEntityArrayList = classEntityArrayList;
    }

    @Override
    public  int getCount()
    {
        return classEntityArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position,View view,ViewGroup viewGroup)
    {
        ViewHolder viewHolder=null;
      final   ClassEntity classEntity = classEntityArrayList.get(position);
        if(view ==null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.teacher_select_class_cell,null);
            view.getBackground().setAlpha(0);
            viewHolder = new ViewHolder();

            viewHolder.tvClassName = (TextView)view.findViewById(R.id.className);
            viewHolder.imgNewMsg = (ImageView)view.findViewById(R.id.newMessage);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.tvClassName.setText(classEntity.getClassName());
        view.setId(Integer.parseInt(classEntity.getClassId()));
        if(classEntity.getClassNew()==0)
        {
            viewHolder.imgNewMsg.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.imgNewMsg.setVisibility(View.VISIBLE);
        }

        return  view;
    }

    private class ViewHolder
    {
        public TextView tvClassName;
        public ImageView imgNewMsg;
    }
}
