package org.yanweiran.app.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.PhotoListEntity;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.dialog.DialogUtil;
import org.yanweiran.app.dialog.DialogUtil2;
import org.yanweiran.app.dialog.DialogUtil3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lenov on 14-4-19.
 */
public class TSinglePhotoFragment extends Fragment {
    private Bitmap mBitmap;
    private PhotoListEntity photoListEntity;
    private ImageLoader imageLoader;
    private  ImageLoader imageLoader2;
    private DisplayImageOptions mDisplayImageOptions;
    public Dialog dialog;
    private  ImageView imageView;
    private ProgressBar progressBar;
    public static TSinglePhotoFragment newsInstance(PhotoListEntity photoListEntity){
            TSinglePhotoFragment newFragment = new TSinglePhotoFragment();
            Bundle  bundle = new Bundle();
            bundle.putSerializable("photo",photoListEntity);
            newFragment.setArguments(bundle);
            return  newFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        imageLoader = ImageLoader.getInstance();
        PhotoListEntity photoEntity = (PhotoListEntity)bundle.getSerializable("photo");
        photoListEntity = photoEntity!=null?photoEntity:null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        mDisplayImageOptions = new DisplayImageOptions.Builder().
                cacheOnDisc(true).
                cacheInMemory(true)
                .build();
        View view  = inflater.inflate(R.layout.tweet_bigphoto_cell,container,false);
        ViewGroup p = (ViewGroup)view.getParent();
        dialog = DialogUtil2.createLoadingDialog(getActivity(),"加载中...");
        final TextView tvName = (TextView)view.findViewById(R.id.tvName);
        final TextView tvTime = (TextView)view.findViewById(R.id.tvTime);
        imageView = (ImageView)view.findViewById(R.id.icon);
        final ImageButton saveImg = (ImageButton)view.findViewById(R.id.savedImg);
        saveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "图片成功保存在(/sdcard/jyt/myImage)中",
                        Toast.LENGTH_SHORT).show();
                new Thread(saveFile).start();
            }
        });
        tvName.setText(photoListEntity.getName());
        tvTime.setText(photoListEntity.getTime());
        imageLoader.displayImage(
                photoListEntity.getbPhotoUrl(),
                imageView,
                mDisplayImageOptions,imageLoadingListener,imageLoadingProgressListener);
        if(p!=null){
            p.removeAllViewsInLayout();
        }
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String s, View view) {
            dialog.show();
        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {

        }
        @Override
        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
              dialog.dismiss();
        }

        @Override
        public void onLoadingCancelled(String s, View view) {

        }
    };
    ImageLoadingProgressListener imageLoadingProgressListener = new ImageLoadingProgressListener() {
        @Override
        public void onProgressUpdate(String s, View view, int i, int i2) {
        }
    };

    private Runnable saveFile = new Runnable() {
        @Override
        public void run() {
            try{
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                    return;
                }
                URL url = new URL(photoListEntity.getbPhotoUrl());
                InputStream is = url.openStream();
                mBitmap = BitmapFactory.decodeStream(is);
                String str = null;
                Date date = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
                date = new Date();
                str = format.format(date);
                String fileName = "/sdcard/jyt/myImage/" + str + ".jpg";
                FileOutputStream b = null;
                File file = new File("/sdcard/jyt/myImage/");
                file.mkdirs();// 创建文件夹，名称为myimage
                b = new FileOutputStream(fileName);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

            }catch (Exception e){

            }
        }
    };
}
