package org.yanweiran.app.dialog;


import android.app.Dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.yanweiran.Login.R;

/**
 * Created by lenov on 14-3-11.
 */
public class DialogUtil2  {


    public DialogUtil2(){
    }
    public static Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progressbar, null);// 得到加载view
        ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progress);
        TextView tipTextView = (TextView) v.findViewById(R.id.tvNote);// 提示文字
        // 加载动画
        tipTextView.setText(msg);// 设置加载信息
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setContentView(v);// 设置布局
        loadingDialog.setCanceledOnTouchOutside(false);
        return loadingDialog;
    }
}
