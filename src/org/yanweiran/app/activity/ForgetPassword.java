package org.yanweiran.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
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

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET,)

    }
}
