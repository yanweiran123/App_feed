package org.yanweiran.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by lenov on 14-3-13.
 */
public class ChatBottomViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentArrayList;

    public ChatBottomViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }
    public ChatBottomViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArrayList)
    {
        super(fm);
        this.fragmentArrayList=fragmentArrayList;
    }
    @Override
    public int getCount()
    {
        return  fragmentArrayList.size();
    }
    @Override
    public Fragment getItem(int arg0) {
        return fragmentArrayList.get(arg0);
    }
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
