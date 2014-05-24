package org.yanweiran.app.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.PublicNewsImgEntity;

/**
 * Created by lenov on 14-5-4.
 */
public class EditPhotoFragment extends Fragment {
    private PublicNewsImgEntity publicNewsImgEntity;
    public  static EditPhotoFragment newsInstance(PublicNewsImgEntity publicNewsImgEntity){
        EditPhotoFragment editPhotoFragment = new EditPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Image",publicNewsImgEntity);
        editPhotoFragment.setArguments(bundle);
        return editPhotoFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        PublicNewsImgEntity imgEntity = (PublicNewsImgEntity)bundle.getSerializable("Image");
        publicNewsImgEntity = imgEntity!=null?imgEntity:null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.edit_photo_cell,container,false);
        ViewGroup p = (ViewGroup)view.getParent();
        final ImageView imageView = (ImageView)view.findViewById(R.id.photo);
        imageView.setImageBitmap(getBitmap(publicNewsImgEntity.getBigUri()));
        if(p!=null){
            p.removeAllViewsInLayout();
        }
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static Bitmap getBitmap(byte[] data){
        return BitmapFactory.decodeByteArray(data, 0, data.length);//从字节数组解码位图
    }
}
