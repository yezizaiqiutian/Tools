package com.gh.tools.base;

import android.app.Application;
import android.content.Context;

/**
 * @author: gh
 * @description:
 * @date: 2016/10/17 10:10.
 */
public class MyApplication extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
