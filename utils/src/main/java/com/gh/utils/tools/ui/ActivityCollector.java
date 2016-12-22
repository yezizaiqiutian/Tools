package com.gh.utils.tools.ui;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: gh
 * @description: 一键退出工具类    推荐在BaseActivity中调用
 * @date: 2016/10/14 11:24.
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();

    /**
     * 在OnCreat中调用
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 在onDestroy中调用
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 点击退出登录时调用
     */
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
