package org.yanweiran.app.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.yanweiran.Login.R;
import org.yanweiran.app.WeekArrange.WeekArrangeContent;

/**
 * Created by lenov on 14-2-5.
 */
public class WeekArrangeDetailFragment extends Fragment {
    public  static  final String ITEM_ID = "item_id";
    WeekArrangeContent.DayDetail dayDetail;
    public  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getArguments().containsKey(ITEM_ID))
        {
                dayDetail = WeekArrangeContent.ITEM_MAP.get(getArguments().getInt(ITEM_ID));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.week_arrange_detail,container,false);
        if(dayDetail !=null)
        {
            ((TextView)rootView.findViewById(R.id.arrangeTime)).setText(dayDetail.time);
            ((TextView)rootView.findViewById(R.id.arrangeContent)).setText(dayDetail.content);
        }
        return  rootView;
    }

}
