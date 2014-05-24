
package org.yanweiran.app.MyWidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

public class InnerListView extends ListView {

    ScrollView parentScrollView;

    public ScrollView getParentScrollView() {
        return parentScrollView;
    }

    public void setParentScrollView(ScrollView parentScrollView) {
        this.parentScrollView = parentScrollView;
    }

    private int maxHeight;

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public InnerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub

        if (maxHeight > -1) {

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);

        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println(getChildAt(0));
    }
    int currentY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        View child = getChildAt(0);
        if (parentScrollView != null) {
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                int height = child.getMeasuredHeight();
                height = height - getMeasuredHeight();

                // System.out.println("height=" + height);
                int scrollY = getScrollY();
                // System.out.println("scrollY" + scrollY);
                int y = (int)ev.getY();

                // ��ָ���»���
                if (currentY < y) {
                    if (scrollY <= 0) {
                        // ������»�����ͷ���Ͱѹ�������Scrollview
                        setParentScrollAble(true);
                        return false;
                    } else {
                        setParentScrollAble(false);

                    }
                } else if (currentY > y) {
                    if (scrollY >= height) {
                        // ������ϻ�����ͷ���Ͱѹ�������Scrollview
                        setParentScrollAble(true);
                        return false;
                    } else {
                        setParentScrollAble(false);

                    }

                }
                currentY = y;
            }
        }

        return super.onTouchEvent(ev);
    }



    /**
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {

        parentScrollView.requestDisallowInterceptTouchEvent(!flag);
    }

}
