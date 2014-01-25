package org.yanweiran.app.clicklistener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import org.yanweiran.app.activity.IndividualCenter;
import org.yanweiran.app.activity.MessageBox;
import org.yanweiran.app.activity.RelativeComment;

import org.yanweiran.Login.R;

/**
* Created by lenov on 13-12-28.
*/
public class BottomMenuListener  {


    public void clickTurn(Activity activity)
    {
                /*底部按钮的触发事件*/
        Button indivCenter=(Button)activity.findViewById(R.id.indivcent);
        indivCenter.setOnClickListener(new bottommenu(activity));

        Button relativeComment = (Button)activity.findViewById(R.id.relativecomment);
        relativeComment.setOnClickListener(new bottommenu(activity));

        Button messageBox = (Button)activity.findViewById(R.id.messageboxbtn);
        messageBox.setOnClickListener(new bottommenu(activity));
    }
     class bottommenu implements View.OnClickListener {
        private Activity oldActivity;
        public bottommenu(Activity oldactivity)
        {
            this.oldActivity =oldactivity;
        }
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent();
            switch (v.getId())
            {
                case R.id.indivcent:
                    intent.setClass(oldActivity,IndividualCenter.class);
                    oldActivity.startActivity(intent);
                    oldActivity.finish();
                    break;
                case R.id.relativecomment:
                    intent.setClass(oldActivity, RelativeComment.class);
                    oldActivity.startActivity(intent);
                    oldActivity.finish();
                    break;
                case R.id.messageboxbtn:
                    intent.setClass(oldActivity, MessageBox.class);
                    oldActivity.startActivity(intent);
                    oldActivity.finish();
                    break;
                default:
                    break;
            }
        }
    }
}
