package org.yanweiran.app.activity;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.content.Context;


import com.baidu.frontia.FrontiaApplication;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

/*
 * 如果您的工程中实现了Application的继承类，那么，您需要将父类改为com.baidu.frontia.FrontiaApplication。
 * 如果您没有实现Application的继承类，那么，请在AndroidManifest.xml的Application标签中增加属性： 
 * <application android:name="com.baidu.frontia.FrontiaApplication"
 * 。。。
 */
public class DemoApplication extends FrontiaApplication {
    private List<Activity> activityList = new LinkedList<Activity>();
    private static DemoApplication instance;
    public DemoApplication(){ };

    @Override
    public void onCreate() {
        super.onCreate();
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "UniversalImageLoader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(5)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new UsingFreqLimitedMemoryCache(2000000)) // You can pass your own memory cache implementation
                .discCache(new UnlimitedDiscCache(cacheDir))
                .build();
        ImageLoader.getInstance().init(config);
    }
        //单例模式中获取唯一的MyApplication实例
        public static DemoApplication getInstance() {
            if(null == instance) {
                instance = new DemoApplication();
            }
            return instance;
        }
//添加Activity到容器中
        public void addActivity(Activity activity)  {
            activityList.add(activity);
        }
//遍历所有Activity并finish
        public void exit(){
            for(Activity activity:activityList) {
                activity.finish();

            }
            System.exit(0);
        }

    }


