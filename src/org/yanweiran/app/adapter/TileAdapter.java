package org.yanweiran.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.User;


/**
 * Created by lenov on 14-3-26.
 */
public class TileAdapter extends BaseAdapter {
        private int[] bgImgId;

        private Context context;

        public TileAdapter(int[] bgImgId,Context context)
        {
            this.bgImgId = bgImgId;

            this.context = context;
        }

        @Override
        public  int getCount()
        {
            return bgImgId.length;
        }
        @Override
        public  long getItemId(int position)
        {
            return 0;
        }
        @Override
        public Object getItem(int position)
        {
            return  null;
        }
        @Override
        public View getView(int position,View view,ViewGroup viewGroup)
        {
            ViewHolder viewHolder = null;
            if(view == null)
            {
                viewHolder = new ViewHolder();
                LayoutInflater inflater =LayoutInflater.from(context);
                view = inflater.inflate(R.layout.title_cell,null);
                viewHolder.bgImg = (ImageView)view.findViewById(R.id.bgImg);
                viewHolder.newsImg = (ImageView)view.findViewById(R.id.alert);
                view.setTag(viewHolder);
            }else {
                    viewHolder = (ViewHolder)view.getTag();
            }


            if(User.getUser().news>0&&position==3){
                viewHolder.newsImg.setVisibility(View.VISIBLE);
            }
            if(User.getUser().notifi>0&&position==1){
                viewHolder.newsImg.setVisibility(View.VISIBLE);
            }

            viewHolder.bgImg.setImageResource(bgImgId[position]);
            return view;
        }

        final    private class  ViewHolder
        {
                private ImageView bgImg;
                private ImageView newsImg;
        }
}
