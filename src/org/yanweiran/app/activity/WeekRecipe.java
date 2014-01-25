package org.yanweiran.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import org.yanweiran.Login.R;
import org.yanweiran.app.clicklistener.TurnEditText;

/**
 * Created by lenov on 14-1-11.
 */
public class WeekRecipe extends Activity {
    @Override
    public void onCreate(Bundle savedInstance)
        {
            super.onCreate(savedInstance);
            LinearLayout mainLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.week_recipe,null);
            setContentView(mainLayout);

            final Button write = (Button)findViewById(R.id.write);

            write.setOnClickListener(new  TurnEditText(this));

        }
}
