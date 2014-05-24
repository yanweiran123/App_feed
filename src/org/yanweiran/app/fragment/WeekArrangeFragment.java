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
import org.yanweiran.app.Singleton.ArrangeEntity;
import org.yanweiran.app.adapter.WeekArrangeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov on 14-2-18.
 */
public class WeekArrangeFragment extends Fragment {

    private List<ArrangeEntity> arrangeList;
   public static WeekArrangeFragment newInstance(List<ArrangeEntity> arrangeList)
    {
        WeekArrangeFragment newFragment = new WeekArrangeFragment();
        Bundle bundle = new Bundle();
        ArrayList list = new ArrayList();
        list.add(arrangeList);
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
        arrangeList =(List<ArrangeEntity>)list.get(0)!=null?(List<ArrangeEntity>)list.get(0):null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.week_listview,container,false);
        ViewGroup p = (ViewGroup)view.getParent();
        ListView mListView = (ListView)view.findViewById(R.id.listView);
        WeekArrangeListViewAdapter mAdapter = new WeekArrangeListViewAdapter(arrangeList,getActivity());
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
        MobclickAgent.onPageStart("一周安排"); //统计页面
        TCAgent.onPageStart(getActivity(), "一周安排");

    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("一周安排");
        TCAgent.onPageEnd(getActivity(),"一周安排");
    }

}
