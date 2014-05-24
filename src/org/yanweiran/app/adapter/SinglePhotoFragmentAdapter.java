package org.yanweiran.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by lenov on 14-4-19.
 */
public class SinglePhotoFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment>  fragmentArrayList;
    public SinglePhotoFragmentAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }
    public SinglePhotoFragmentAdapter(FragmentManager fragmentManager,ArrayList fragmentArrayList){
        super(fragmentManager);
        this.fragmentArrayList = fragmentArrayList;
    }
    @Override
    public int getCount(){
        return fragmentArrayList.size();
    }
    @Override
    public Fragment getItem(int arg0){
        return fragmentArrayList.get(arg0);
    }
    @Override
    public  int getItemPosition(Object object){
        return super.getItemPosition(object);
    }
    @Override
    public void destroyItem(View collection, int position, Object o) {
        View view = (View)o;
        ((ViewPager) collection).removeView(view);
        view = null;
    }
}
