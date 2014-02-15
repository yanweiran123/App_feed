package org.yanweiran.app.WeekArrange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenov on 14-2-5.
 */
public class WeekArrangeContent {
        public  static  class DayDetail
        {
            public  Integer id;
            public  String  time;
            public  String  content;

            public DayDetail (int id,String time,String content)
            {
                this.id = id;
                this.time=time;
                this.content= content;

            }
            @Override
            public  String toString()
            {
                return time;
            }

        }

    public  static List<DayDetail> ITEMS = new ArrayList<DayDetail>();
    public  static Map<Integer,DayDetail>  ITEM_MAP = new HashMap<Integer, DayDetail>();

    static
    {
        addItem(new DayDetail(1,"6:00","学习JAVA"));
        addItem(new DayDetail(2,"7:00","学习JAVA"));
        addItem(new DayDetail(3,"8:00","学习JAVA"));
    }
        private  static  void addItem(DayDetail dayDetail)
        {
            ITEMS.add(dayDetail);
            ITEM_MAP.put(dayDetail.id,dayDetail);
        }
}
