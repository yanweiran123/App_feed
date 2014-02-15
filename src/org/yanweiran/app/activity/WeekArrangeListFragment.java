package org.yanweiran.app.activity;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.yanweiran.app.WeekArrange.WeekArrangeContent;

/**
 * Created by lenov on 14-2-5.
 */
public class WeekArrangeListFragment extends ListFragment {

    private Callbacks mCallBacks;
    public  interface Callbacks
    {
        public  void  onItemSelected(Integer id);
    }
    @Override
    public  void  onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setListAdapter(new ArrayAdapter<WeekArrangeContent.DayDetail>(getActivity(),android.R.layout.simple_list_item_activated_1,android.R.id.text1,WeekArrangeContent.ITEMS));
    }
    @Override
    public  void  onAttach(Activity activity)
    {
        super.onAttach(activity);
        if(!(activity instanceof Callbacks))
        {
            throw  new IllegalStateException("所在Activity必须实现接口");
        }
        mCallBacks = (Callbacks)activity;
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        mCallBacks= null;
    }
    @Override
    public  void  onListItemClick(ListView listView,View view,int position,long id)
    {
        super.onListItemClick(listView,view,position,id);
        mCallBacks.onItemSelected(WeekArrangeContent.ITEMS.get(position).id);
    }

    public void  setActivateOnItemClick(boolean activateOnItemClick)
    {
        getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }
}
