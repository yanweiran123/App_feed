package org.yanweiran.app.dialog;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import org.yanweiran.Login.R;
public class NoticeAll {
    private  Context context;
    private View view;
    private PopupWindow pwMyPopWindow;// popupwindow
    public NoticeAll(Context context,View view)
    {
        this.context=context;
        this.view =view;
        pwMyPopWindow.showAtLocation(view,Gravity.CENTER,10,10);// 显示
        iniPopupWindow();
    }
    private void iniPopupWindow() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.task_detail_popupwindow, null);
        pwMyPopWindow = new PopupWindow(layout);
        pwMyPopWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件

        pwMyPopWindow.setWidth(10);
        pwMyPopWindow.setHeight(20);

        // 控制popupwindow点击屏幕其他地方消失
        pwMyPopWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
        pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
    }

}