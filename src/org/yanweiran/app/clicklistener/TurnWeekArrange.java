package org.yanweiran.app.clicklistener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import  android.view.View.OnClickListener;

import org.yanweiran.app.activity.WeekArrange;

/**
 * Created by lenov on 14-1-11.
 */
public class TurnWeekArrange implements OnClickListener
    {
        private Activity oldactivity;
        public TurnWeekArrange(Activity oldactivity)
        {
            this.oldactivity = oldactivity;
        }
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(oldactivity, WeekArrange.class);
                oldactivity.startActivity(intent);
                oldactivity.finish();
            }
    }
