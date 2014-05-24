package org.yanweiran.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.RecipeEntity;

import java.util.List;

/**
 * Created by lenov on 14-2-22.
 */
public class WeekRecipeListViewAdapter extends BaseAdapter {

    private List<RecipeEntity> recipeEntityList;
    private int num;
    private  Context context;

    public WeekRecipeListViewAdapter( int num,List<RecipeEntity> recipeEntityList,Context context)
    {
        this.recipeEntityList = recipeEntityList;
        this.context = context;
        this.num = num;
    }

    @Override
    public int getCount()
    {
        return num;
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
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        ViewHoler viewHoler=null;
        RecipeEntity entity = recipeEntityList.get(position);
        if(view == null)
        {
            LayoutInflater inflater =LayoutInflater.from(context);
            view = inflater.inflate(R.layout.week_recipe_detail,null);
            viewHoler = new ViewHoler();
            viewHoler.tvCan1 = (TextView)view.findViewById(R.id.content1);
            viewHoler.tvCan2=(TextView)view.findViewById(R.id.content2);
            viewHoler.tvCan3=(TextView)view.findViewById(R.id.content3);
            viewHoler.tvCan4=(TextView)view.findViewById(R.id.content4);
            viewHoler.tvCan5=(TextView)view.findViewById(R.id.content5);
            view.setTag(viewHoler);
        }else{
            viewHoler = (ViewHoler)view.getTag();
        }
        viewHoler.tvCan1.setText(entity.getCan1());
        viewHoler.tvCan2.setText(entity.getCan2());
        viewHoler.tvCan3.setText(entity.getCan3());
        viewHoler.tvCan4.setText(entity.getCan4());
        viewHoler.tvCan5.setText(entity.getCan5());

        return view;
    }

    private final  static  class ViewHoler
    {

        private TextView tvCan1;
        private TextView tvCan2;
        private TextView tvCan3;
        private TextView tvCan4;
        private TextView tvCan5;
    }
}
