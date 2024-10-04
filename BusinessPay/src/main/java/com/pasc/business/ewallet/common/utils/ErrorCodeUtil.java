package com.pasc.business.ewallet.common.utils;

import android.text.TextUtils;

import com.pasc.business.ewallet.business.StatusTable;

/**
 * Created by ex-huangzhiyi001 on 2019/3/19.
 */
public class ErrorCodeUtil {

    private static String[] matchCode = {

    };

    /**
     * 拦截错误码 ,不属于这个范围内的 弹toast
     *
     * @param code
     * @param msg
     * @return
     */
    public static boolean shouldIntercept(String code, String msg) {

        if (!TextUtils.isEmpty (code)) {
            for (String c : matchCode) {
                if (code.equals (c)) {
                    //拦截
                    return true;
                }
            }
        }
        return false;
    }
}
