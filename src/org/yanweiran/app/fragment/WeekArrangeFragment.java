package org.yanweiran.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.yanweiran.Login.R;
import org.yanweiran.app.activity.WeekArrange;
import org.yanweiran.app.myadpter.WeekArrangeListViewAdapter;

/**
 * Created by lenov on 14-2-18.
 */
public class WeekArrangeFragment extends Fragment {
    private  static final String TAG = "WeekArrangeFragment";
    private String hello;// = "hello android";
    private String defaultHello = "default value";
    private Context context;
   public static WeekArrangeFragment newInstance(String s)
    {
        WeekArrangeFragment newFragment = new WeekArrangeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hello", s);
        newFragment.setArguments(bundle);
        return newFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        hello = args != null ? args.getString("hello") : defaultHello;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.week_arrange_listview,container,false);
        ViewGroup p = (ViewGroup)view.getParent();
        ListView mListView = (ListView)view.findViewById(R.id.listView);
        WeekArrangeListViewAdapter mAdapter = new WeekArrangeListViewAdapter(10,getActivity());
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
}
