package org.yanweiran.app.clicklistener;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import org.yanweiran.app.activity.IndividualCenter;

/**
 * Created by lenov on 14-1-11.
 */
public class TurnToPersonData implements OnClickListener {

        private Activity oldactivity;
        public TurnToPersonData(Activity oldactivity)
        {
            this.oldactivity= oldactivity;
        }

        public void onClick(View v)
        {
                Intent intent = new Intent();
                intent.setClass(oldactivity, IndividualCenter.class);
                oldactivity.startActivity(intent);
                oldactivity.finish();
        }
}
