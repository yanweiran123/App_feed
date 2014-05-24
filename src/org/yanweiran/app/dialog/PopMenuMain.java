package org.yanweiran.app.dialog;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.yanweiran.Login.R;

import java.util.List;
import java.util.Map;

/**
 * Created by lenov on 14-3-21.
 */
public class PopMenuMain {

    private Context context;
    private View view;
    private int flag;
    private int which=0;
    private String[] tvItem;
    public PopupWindow pwMyPopWindow;// popupwindow
    public ListView lvPopupList;// popupwindow中的ListView
    private int NUM_OF_VISIBLE_LIST_ROWS ;// 指定popupwindow中Item的数量
    public PopMenuMain(Context context,View view,int NUM_OF_VISIBLE_LIST_ROWS,String[] tvItem,int flag,int which)
    {
        this.context=context;
        this.view =view;
        this.tvItem = tvItem;
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        final int w_screen = dm.widthPixels;
        final int h_screen = dm.heightPixels;
        this.flag = flag;
        final int tag =which;
        this.NUM_OF_VISIBLE_LIST_ROWS = NUM_OF_VISIBLE_LIST_ROWS;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.task_detail_popupwindow, null);
        lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
        lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        view.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (pwMyPopWindow.isShowing()) {

                    pwMyPopWindow.dismiss();// 关闭
                } else {
                    if(tag == 1)
                    {
                        pwMyPopWindow.showAsDropDown(v, -v.getWidth()/4, 0);// 显示
                    }else{
                        pwMyPopWindow.showAsDropDown(v,-v.getWidth()/3,0);
                    }
                }

            }
        });
        iniPopupWindow();
    }
    private void iniPopupWindow() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.task_detail_popupwindow, null);
        lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
        pwMyPopWindow = new PopupWindow(layout);
        pwMyPopWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件
        lvPopupList.setAdapter(new MyAdapter(context,tvItem));
        // 控制popupwindow的宽度和高度自适应
        lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth());
        pwMyPopWindow.setHeight((lvPopupList.getMeasuredHeight() + 20)
                * NUM_OF_VISIBLE_LIST_ROWS);

        // 控制popupwindow点击屏幕其他地方消失
        pwMyPopWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.title_menu));// 设置背景图片，不能在布局中设置，要通过代码来设置
        pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
    }

    class  MyAdapter extends BaseAdapter
    {
        private Context context;
        private String[] text;
        public MyAdapter(Context context,String[] text)
        {
            this.context = context;
            this.text   =   text;
        }
        @Override
        public int getCount()
        {
            return text.length;
        }
        @Override
        public Object getItem(int position)
        {
            return null;
        }
        @Override
        public long getItemId(int position)
        {
            return 0;
        }
        @Override
        public View getView(int position,View view,ViewGroup viewGroup)
        {
            ViewHolder viewHolder =null;
            if(view == null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.list_item_popupwindow2,null);
                viewHolder.textView = (TextView)view.findViewById(R.id.tv_list_item);
                viewHolder.imageView = (ImageView)view.findViewById(R.id.img_list_item);
                view.setTag(viewHolder);
            }else {
                    viewHolder = (ViewHolder)view.getTag();
            }
            viewHolder.textView.setText(text[position]);
            if(position==flag)
            {
                viewHolder.textView.setTextColor(context.getResources().getColor(R.color.red));
            }
            return  view;
        }
       private   class ViewHolder
       {
           private TextView textView;
           private ImageView imageView;
       }
    }
}
