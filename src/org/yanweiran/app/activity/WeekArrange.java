package org.yanweiran.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.dialog.DialogUtil;

/**
 * Created by lenov on 14-1-10.
 */
public class WeekArrange extends Activity  implements  WeekArrangeListFragment.Callbacks{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout mainLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.activity_week_twopane,null);
        setContentView(mainLayout);

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String jsonDataUrl = "http://115.28.46.167:83/app_feed/weekanpai.php?token=36a88bc0efce07b903e0e7af406ac022";
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,
//                new Response.Listener< JSONObject>()
//                {
//                    @Override
//                  public void onResponse(JSONObject jsonObject)
//                    {
//                        DialogUtil.showDialog(WeekArrange.this,jsonObject.toString());
//                    }
//                },new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//
//        requestQueue.add(jsonObjectRequest);
    }
    @Override
    public  void onItemSelected(Integer id)
    {
//        Bundle arguments = new Bundle();
//        arguments.putInt(WeekArrangeDetailFragment.ITEM_ID,id);
//        WeekArrangeDetailFragment fragment = new WeekArrangeDetailFragment();
//        fragment.setArguments(arguments);
//        getFragmentManager().beginTransaction().replace(R.id.detail_container,fragment).commit();
    }
}
