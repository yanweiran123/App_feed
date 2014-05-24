package org.yanweiran.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.yanweiran.Login.R;

import org.yanweiran.app.Singleton.RecipeEntity;
import org.yanweiran.app.adapter.WeekRecipeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov on 14-2-22.
 */
public class WeekRecipeFragment extends Fragment {

    private List<RecipeEntity> recipeEntityList;

    public static WeekRecipeFragment newInstance(List<RecipeEntity> recipeEntityList)
    {
        WeekRecipeFragment newFragment = new WeekRecipeFragment();
        Bundle bundle = new Bundle();
        ArrayList list = new ArrayList();
        list.add(recipeEntityList);
        bundle.putParcelableArrayList("list", list);
        newFragment.setArguments(bundle);
        return newFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        ArrayList list = args.getParcelableArrayList("list");
        recipeEntityList =(List<RecipeEntity>)list.get(0)!=null?(List<RecipeEntity>)list.get(0):null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.week_listview,container,false);
        ViewGroup p = (ViewGroup)view.getParent();
        ListView mListView = (ListView)view.findViewById(R.id.listView);
        WeekRecipeListViewAdapter mAdapter = new WeekRecipeListViewAdapter(recipeEntityList.size(),recipeEntityList,getActivity());
        mListView.setAdapter(mAdapter);
        if(p!=null)
        {
            p.removeAllViewsInLayout();
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("一周食谱"); //统计页面
        TCAgent.onPageStart(getActivity(),"一周食谱");
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("一周食谱");
        TCAgent.onPageEnd(getActivity(),"一周食谱");
    }


}
