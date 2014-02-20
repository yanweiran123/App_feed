package org.yanweiran.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.pixate.Pixate;

import org.yanweiran.Login.R;
import org.yanweiran.app.clicklistener.BottomMenuListener;

/**
 * Created by lenov on 13-12-28.
 */
public class IndividualCenter extends Activity {
    @Override
    public  void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        LinearLayout mainlinearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.individual_center,null);
        setContentView(mainlinearLayout);
        new BottomMenuListener().clickTurn(this);
   }
}
