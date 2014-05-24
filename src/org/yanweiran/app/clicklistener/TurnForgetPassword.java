package org.yanweiran.app.clicklistener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import org.yanweiran.app.activity.ForgetPassword;

/**
 * Created by lenov on 14-1-12.
 */
public class TurnForgetPassword implements OnClickListener {

    private Activity oldActivity;
    public TurnForgetPassword(Activity oldActivity)
    {
        this.oldActivity=oldActivity;
    }
    @Override
    public void onClick(View v)
    {
            Intent intent = new Intent();
            intent.setClass(oldActivity,ForgetPassword.class);
            oldActivity.startActivity(intent);
            oldActivity.finish();
    }
}
