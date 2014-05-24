package org.yanweiran.app.activity;

import java.util.ArrayList;
import java.util.LinkedList;



import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.yanweiran.app.Singleton.RecentTalkEntity;
import org.yanweiran.app.adapter.RecentTalkAdapter;

public class MyApplication extends Application {
	private boolean isClientStart;// 客户端连接是否启动
	private NotificationManager mNotificationManager;
	private int newMsgNum = 0;// 后台运行的消息
	private ArrayList<RecentTalkEntity> mRecentList;
	private RecentTalkAdapter mRecentAdapter;
    private ImageLoader imageLoader;
	private int recentNum = 0;

	@Override
	public void onCreate() {
        imageLoader = ImageLoader.getInstance();
        initImageLoader(this);
		mRecentList = new ArrayList<RecentTalkEntity>();
		mRecentAdapter = new RecentTalkAdapter(mRecentList,getApplicationContext(),
				imageLoader);
		super.onCreate();
	}



	public boolean isClientStart() {
		return isClientStart;
	}

	public void setClientStart(boolean isClientStart) {
		this.isClientStart = isClientStart;
	}

	public NotificationManager getmNotificationManager() {
		return mNotificationManager;
	}

	public void setmNotificationManager(NotificationManager mNotificationManager) {
		this.mNotificationManager = mNotificationManager;
	}

	public int getNewMsgNum() {
		return newMsgNum;
	}

	public void setNewMsgNum(int newMsgNum) {
		this.newMsgNum = newMsgNum;
	}

	public ArrayList<RecentTalkEntity> getmRecentList() {
		return mRecentList;
	}

	public void setmRecentList(ArrayList<RecentTalkEntity>  mRecentList) {
		this.mRecentList = mRecentList;
	}

	public RecentTalkAdapter getmRecentAdapter() {
		return mRecentAdapter;
	}

	public void setmRecentAdapter(RecentTalkAdapter mRecentAdapter) {
		this.mRecentAdapter = mRecentAdapter;
	}

	public int getRecentNum() {
		return recentNum;
	}

	public void setRecentNum(int recentNum) {
		this.recentNum = recentNum;
	}

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
