package org.yanweiran.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.yanweiran.Login.R;
import org.yanweiran.app.Singleton.User;
import org.yanweiran.app.clicklistener.TurnToPersonData;
import org.yanweiran.app.clicklistener.TurnWeekArrange;

/**
 * Created by lenov on 13-12-20.
 */
public class AfterLogin_Student extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afterlogin_student);
        final Button teacher_notice = (Button)findViewById(R.id.teacher_notice);
        final Button week_arrange = (Button)findViewById(R.id.week_arrange);
        final Button week_recipes = (Button)findViewById(R.id.week_recipe);
        final Button personal_data = (Button)findViewById(R.id.person_info);

        /*========================
        * 进入老师通知界面
        * =======================*/
        teacher_notice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                User.getUser().tag="1";
                Intent intent = new Intent();
                intent.setClass(AfterLogin_Student.this,TeacherNotice.class);
                startActivity(intent);
                finish();
            }
        });

        /*===========================
        *  进入一周安排界面
        * ==========================*/
         week_arrange.setOnClickListener(new TurnWeekArrange(this));

        /*============================
            进入一周食谱界面
         =============================*/
        week_recipes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(AfterLogin_Student.this,WeekRecipe.class);
                startActivity(intent);
                finish();
            }
        });
         /*============================
            进入个人信息界面
         =============================*/
        personal_data.setOnClickListener(new TurnToPersonData(this));
    }

}
