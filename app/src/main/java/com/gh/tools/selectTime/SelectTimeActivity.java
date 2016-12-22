package com.gh.tools.selectTime;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.utils.views.selectTime.view.TimeSelectView;

/**
 * @author: gh
 * @description: 时间选择
 * @date: 2016/10/18 11:18.
 */

public class SelectTimeActivity extends BaseActivity{

    private TimeSelectView id_tsv;

    /**
     * 启动SelectTimeActivity
     *
     * @param context context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SelectTimeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_time);
        id_tsv = (TimeSelectView) findViewById(R.id.id_tsv);
    }

    @Override
    protected void initListener() {
        findViewById(R.id.id_tv_getTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SelectTimeActivity.this,id_tsv.getSelectTime("yyyy-MM-dd-HH-mm")+id_tsv.getSelectTime(),Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.id_tv_setCurrentTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_tsv.setCurrentTime();
            }
        });
        findViewById(R.id.id_tv_setNorTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_tsv.setNorTime(1472175723000l);
            }
        });
    }
}
