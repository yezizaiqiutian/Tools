package com.gh.tools.indexaberecyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.tools.indexaberecyclerview.city.PickCityActivity;
import com.gh.tools.indexaberecyclerview.contact.PickContactActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: gh
 * @description: indexaberecyclerview
 * @date: 2016/12/9 10:00
 * @note:
 */

public class IndexableRVActivity extends BaseActivity {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, IndexableRVActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indexablerv_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.id_btn_pickcity, R.id.id_btn_pickcontact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_pickcity:
                PickCityActivity.actionStart(mContext);
                break;
            case R.id.id_btn_pickcontact:
                PickContactActivity.actionStart(mContext);
                break;
        }
    }
}
