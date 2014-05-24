package org.yanweiran.app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import org.yanweiran.app.Singleton.PublicNewsImgEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov on 14-5-4.
 */
public class EditImgFragementAdapter extends FragmentStatePagerAdapter {


    //	private FragmentManager fm;
    private ArrayList<Fragment> fragments = null;
    private ArrayList<PublicNewsImgEntity> publicNewsImgEntities;
    private Context context;



    public EditImgFragementAdapter(Context context, FragmentManager fm, ArrayList<PublicNewsImgEntity> publicNewsImgEntities) {
        super(fm);
        this.context = context;
        this.publicNewsImgEntities=publicNewsImgEntities;
        notifyDataSetChanged();
//		this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
//		Fragment fragment = new ColourFragment();
//		Bundle args = new Bundle();
//		args.putInt("title", arg0);
//		args.putSerializable("content",hotIssuesList.get(arg0));
//		fragment.setArguments(args);
//		return fragment;
        return fragments.get(arg0);
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return  publicNewsImgEntities.size();//hotIssuesList.size();
    }
}
