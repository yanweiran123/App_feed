package org.yanweiran.app.Singleton;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lenov on 14-1-22.
 */
public class SchoolClass
    {

        public  String classId;
        public JSONArray messageArray;
        private volatile static SchoolClass schoolClass;
        private SchoolClass()
        {

        }
        public static  SchoolClass getSchoolClass()
        {
            if (schoolClass == null) {
                synchronized (SchoolClass.class) {
                    if (schoolClass == null) {
                        schoolClass = new SchoolClass();
                    }
                }
            }
            return schoolClass;
        }
    }

