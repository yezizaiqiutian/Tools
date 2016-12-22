package com.gh.utils.tools.phone;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * @author: gh
 * @description: 与SMS卡相关信息
 * @date: 2016/10/14 11:55.
 */
public class SMSUtils {
    /**
     * 获取用户手机号
     * 只能获取部分手机
     *
     * @param context
     * @return
     */
    public static String getTelNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    /**
     * 获取智能设备唯一编号
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获得SIM卡的序号
     *
     * @param context
     * @return
     */
    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimSerialNumber();
    }

    /**
     * 得到用户Id
     *
     * @param context
     * @return
     */
    public static String getSubscriberId(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }
}
