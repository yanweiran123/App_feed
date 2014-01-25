package org.yanweiran.app.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

/**
 * Created by lenov on 13-12-17.
 */
public class DialogUtil {
    public static void showDialog(Context source,String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(source)
                .setTitle("家长应用")
                .setMessage(message);
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
