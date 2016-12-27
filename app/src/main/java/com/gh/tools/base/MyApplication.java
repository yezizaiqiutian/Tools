package com.gh.tools.base;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: gh
 * @description:
 * @date: 2016/10/17 10:10.
 */
public class MyApplication extends Application{

    private static Context mContext;

    /**
     * 利用Application传值
     * 需要注意新建对象
     */
    private Map<Object, Object> innerMap = new HashMap<Object, Object>();

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public void addParam(Object key, Object value) {
        innerMap.put(key, value);
    }

    public Object getParam(Object key) {
        return innerMap.get(key);
    }

    public void reMoveParam(Object key) {
        innerMap.remove(key);
    }

}
