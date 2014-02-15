package org.yanweiran.app.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.LruCache;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yanweiran.Login.R;
import org.yanweiran.app.HttpPackage.HttpUtil;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.myadpter.PhotoListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenov on 14-2-10.
 */
public class PhotoList  extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photolist);

        final GridView mGridView = (GridView)findViewById(R.id.grid0);

        String jsonDataUrl = HttpUtil.BASE_URL + "photos.php?"+"token="+ User.getUser().token;
        RequestQueue    requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest   jsonObjectRequest =new JsonObjectRequest(Request.Method.GET,jsonDataUrl,null,
           new Response.Listener<JSONObject>() {
               @Override
               public void onResponse(JSONObject jsonObject) {
                   DialogUtil.showDialog(PhotoList.this,jsonObject.toString());
                   try
                   {
                        JSONArray    jsonArray   =   jsonObject.getJSONArray("lists");
                       Integer  num = jsonArray.length();
                       String[]    smallPhoto = new  String[num];
                       for(Integer i=0;i<num;i++)
                       {
                           smallPhoto[i]=jsonArray.getJSONObject(i).getString("s_photo");
                       }
                       PhotoListAdapter mPhotoListAdapter = new PhotoListAdapter(num,smallPhoto,PhotoList.this);
                       mGridView.setAdapter(mPhotoListAdapter);

                   }
                   catch (JSONException ex)
                   {

                   }
               }
           },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
            requestQueue.add(jsonObjectRequest);

    }

}
