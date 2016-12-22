package com.gh.tools.clock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;

/**
 * @author: gh
 * @description:
 * @date: 2016/11/10 18:00.
 */

public class ClockActivity extends BaseActivity{

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ClockActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        findViewById(R.id.id_btn).setOnClickListener(v->openClick());
    }


    private void openClick() {
        Intent alarms = new Intent(AlarmClock.ACTION_SET_ALARM);
        startActivity(alarms);
    }
}
