package org.yanweiran.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import org.yanweiran.Login.R;

/**
 * Created by lenov on 14-1-10.
 */
public class WeekArrange extends Activity {
    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        LinearLayout mainLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.week_arrange,null);
        setContentView(mainLayout);
    }
}
