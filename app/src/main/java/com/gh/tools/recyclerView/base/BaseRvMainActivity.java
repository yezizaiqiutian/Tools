package com.gh.tools.recyclerView.base;

import android.content.Context;
import android.content.Intent;

import com.gh.tools.base.BaseActivity;

/**
 * @author: gh
 * @description: RecyclerView.Adapter优化
 * @date: 2016/12/6 16:59.
 */

public class BaseRvMainActivity extends BaseActivity{

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, BaseRvMainActivity.class);
        context.startActivity(intent);
    }

}
