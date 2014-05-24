package org.yanweiran.app.dialog;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.view.Gravity;
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
public class PopMenu1 {
    private  Context context;
    private View view;
    List<Map<String, Object>> moreList;
    private String[] itemText;
    private int[] imgId;
    private PopupWindow pwMyPopWindow;// popupwindow
    public ListView lvPopupList;// popupwindow中的ListView
    private int NUM_OF_VISIBLE_LIST_ROWS ;// 指定popupwindow中Item的数量
    public PopMenu1(Context context,View view,int NUM_OF_VISIBLE_LIST_ROWS, List<Map<String, Object>> moreList)
    {
        this.context=context;
        this.view =view;
        this.moreList = moreList;
        this.NUM_OF_VISIBLE_LIST_ROWS = NUM_OF_VISIBLE_LIST_ROWS;
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (pwMyPopWindow.isShowing()) {

                    pwMyPopWindow.dismiss();// 关闭
                } else {

                 pwMyPopWindow.showAsDropDown(v,-v.getWidth(),5);// 显示
               //     pwMyPopWindow.showAtLocation(v, Gravity.LEFT,0 ,0);
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
        lvPopupList.setAdapter(new SimpleAdapter(context, moreList,
                R.layout.list_item_popupwindow1, new String[] { "share_key","img" },
                new int[] { R.id.tv_list_item ,R.id.img_list_item}));
        // 控制popupwindow的宽度和高度自适应
        lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth());
        pwMyPopWindow.setHeight((lvPopupList.getMeasuredHeight() +25)
                * NUM_OF_VISIBLE_LIST_ROWS);

        // 控制popupwindow点击屏幕其他地方消失
        pwMyPopWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
        pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
        pwMyPopWindow.setTouchable(true);
    }



}
