package org.yanweiran.app.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.PublicNewsImgEntity;
import org.yanweiran.app.Singleton.PublicType;
import org.yanweiran.app.adapter.EditImgFragementAdapter;
import org.yanweiran.app.adapter.SinglePhotoFragmentAdapter;
import org.yanweiran.app.fragment.EditPhotoFragment;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by lenov on 14-5-4.
 */
public class EditPhoto extends FragmentActivity{

    private ArrayList<PublicNewsImgEntity> publicNewsImgEntities;
    private ArrayList<Fragment> fragmentArrayList;
    private ImageButton imageButton;
    private static int current;
    private ViewPager mViewPager ;
    private EditImgFragementAdapter singlePhotoFragmentAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_photo);
        publicNewsImgEntities = (ArrayList<PublicNewsImgEntity>)getIntent().getSerializableExtra("imgList");
        initViewPager();
    }



    public void initViewPager(){

        mViewPager = (ViewPager)findViewById(R.id.vPager);
        fragmentArrayList = new ArrayList<Fragment>();
        singlePhotoFragmentAdapter =new EditImgFragementAdapter(this,getSupportFragmentManager(), publicNewsImgEntities);
        for(int i=0;i<publicNewsImgEntities.size();i++ ){
            PublicNewsImgEntity publicNewsImgEntity = publicNewsImgEntities.get(i);
            fragmentArrayList.add(EditPhotoFragment.newsInstance(publicNewsImgEntity));
            mViewPager.setAdapter(singlePhotoFragmentAdapter);
            mViewPager.setCurrentItem(PublicType.getPublicType().IMG_INDEX);
        }
        imageButton = (ImageButton)findViewById(R.id.delete);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current = mViewPager.getCurrentItem();
                Log.e("***********************************",current+"");
                publicNewsImgEntities.remove(current);
                singlePhotoFragmentAdapter.notifyDataSetChanged();

            }
        });

    }

}
