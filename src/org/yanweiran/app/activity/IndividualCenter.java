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
        Pixate.setLicenseKey("MP7VF-CAGHI-2NE0T-UG6B0-75HQM-8SVI6-D23SG-AQLHH-MERVK-51GAB-NPG96-GPMUT-CR8VB-UEALM-H6JI7-L0\n", "shijiezhang@ieee.org"
        );
        Pixate.init(this);
        LinearLayout mainlinearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.individual_center,null);
        setContentView(mainlinearLayout);
        LinearLayout middlecontainer = (LinearLayout)findViewById(R.id.qqqq);
        new BottomMenuListener().clickTurn(this);
        for(int i=0;i<3;i++)
        {
            LinearLayout secdondLay = (LinearLayout)getLayoutInflater().inflate(R.layout.indicent_single,null);
            middlecontainer.addView(secdondLay);
        }
   }
}
