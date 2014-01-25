package org.yanweiran.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import org.yanweiran.Login.R;

/**
 * Created by lenov on 14-1-11.
 */
public class EditText extends Activity
    {
            @Override
        public void onCreate(Bundle savedInstance)
            {
                super.onCreate(savedInstance);
                LinearLayout  mainLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.edit_message,null);
                setContentView(mainLayout);
            }
    }
