package org.yanweiran.app.MyWidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.jar.Attributes;

/**
 * Created by lenov on 14-3-4.
 */
public class AssitGridView extends RelativeLayout {

    public AssitGridView(Context context,AttributeSet attrs,int defStyle)
    {
        super(context,attrs,defStyle);
    }
    public AssitGridView(Context context,AttributeSet attrs)
    {
        super(context,attrs);
    }
    public AssitGridView(Context context)
    {
        super(context);
    }
    @SuppressWarnings("unused")

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec)
    {
        setMeasuredDimension(getDefaultSize(0,widthMeasureSpec),getDefaultSize(0,heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        heightMeasureSpec= widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }
}
