package org.yanweiran.app.dialog;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.User;

public class PopMenu {
    private  Context context;
    private View view;
    private String[] s;
    private int FLAG=0;
    List<Map<String, String>> moreList;
    private  View layout;
    public PopupWindow pwMyPopWindow;// popupwindow
    public ListView lvPopupList;// popupwindow中的ListView
    private int NUM_OF_VISIBLE_LIST_ROWS ;// 指定popupwindow中Item的数量
    public PopMenu(Context context,View view,int NUM_OF_VISIBLE_LIST_ROWS,String[] s )
    {
        this.context=context;
        this.view =view;
        this.s = s;
        this.NUM_OF_VISIBLE_LIST_ROWS = NUM_OF_VISIBLE_LIST_ROWS;
        view.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (pwMyPopWindow.isShowing()) {
                pwMyPopWindow.dismiss();// 关闭
            } else {
                pwMyPopWindow.showAsDropDown(v, -v.getWidth()*3+65,0 );// 显示
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
        lvPopupList.setAdapter(new  PopMenuAdapter(context, s));
        // 控制popupwindow的宽度和高度自适应
        lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth());
        pwMyPopWindow.setHeight((lvPopupList.getMeasuredHeight() + 10)
                * NUM_OF_VISIBLE_LIST_ROWS);

        // 控制popupwindow点击屏幕其他地方消失
        pwMyPopWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
        pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
        pwMyPopWindow.setTouchable(true);
    }
    public  class PopMenuAdapter extends BaseAdapter{
        private Context context;
        private String[] text;
        private int FLAG;
        public PopMenuAdapter(Context context,String[] text)
        {
            this.context = context;
            this.FLAG =FLAG;
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
                view = inflater.inflate(R.layout.list_item_popupwindow,null);
                viewHolder.textView = (TextView)view.findViewById(R.id.tv_list_item);
                view.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder)view.getTag();
            }
            viewHolder.textView.setText(text[position]);
            if(position== User.getUser().flag)
            {
                viewHolder.textView.setTextColor(context.getResources().getColor(R.color.yellow));
            }else {
                viewHolder.textView.setTextColor(context.getResources().getColor(R.color.white));
            }
            return  view;
        }
        private   class ViewHolder
        {
            private TextView textView;

        }
    }

}
