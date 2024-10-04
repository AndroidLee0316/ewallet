package com.pasc.business.ewallet.common.utils;

/**
 * 防止连续点击
 * Created by qinguohuai on 2019/4/16.
 */
public class ClickUtils {

    // 上次点击时间
    private static long lastClickTime = 0;
    // 时间间隔
    private final static int SPACE_TIME = 1500;


    public static boolean isDoubleClick() {

        boolean isFastClick = false;//是否快速点击
        long currentTime = System.currentTimeMillis();//当前系统时间

        isFastClick = currentTime - lastClickTime <= SPACE_TIME;

        lastClickTime = currentTime;

        return isFastClick;
    }

}
