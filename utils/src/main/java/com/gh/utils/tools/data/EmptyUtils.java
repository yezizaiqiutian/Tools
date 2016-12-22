package com.gh.utils.tools.data;

import android.text.TextUtils;

/**
 * @author: gh
 * @description: 参数为空时的处理，防止出现空指针
 * @date: 2016/10/14 11:20.
 */
public class EmptyUtils {
    /**
     * 判断String是否为空
     * 当为null，设置为""
     *
     * @param string
     * @return
     */
    public static String EmptyString(String string) {
        return TextUtils.isEmpty(string) ? "" : string;
    }

    /**
     * 判断String是否为空
     * 当为null，设置为temp
     *
     * @param string
     * @param temp
     * @return
     */
    public static String EmptyString(String string, String temp) {
        return TextUtils.isEmpty(string) ? temp : string;
    }

    /**
     * 判断Object是否为空
     * 当为null，设置为temp
     *
     * @param object
     * @param temp
     * @return
     */
    public static Object EmptObj(Object object, Object temp) {
        return object == null ? temp : object;
    }
}
