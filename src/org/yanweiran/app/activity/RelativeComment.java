package org.yanweiran.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import org.yanweiran.Login.R;
import org.yanweiran.app.clicklistener.BottomMenuListener;

/**
 * Created by lenov on 13-12-28.
 */
public class RelativeComment extends Activity{
    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        LinearLayout mainLinearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.relativecomment,null);
        setContentView(mainLinearLayout);
                  /*底部按钮的触发事件*/

        new BottomMenuListener().clickTurn(this);
    }
}
