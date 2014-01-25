package org.yanweiran.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import org.yanweiran.Login.R;


/**
 * Created by lenov on 14-1-12.
 */
public class ForgetPassword extends Activity {

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        LinearLayout mainLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.forget_pass,null);
        setContentView(mainLayout);
    }
}
