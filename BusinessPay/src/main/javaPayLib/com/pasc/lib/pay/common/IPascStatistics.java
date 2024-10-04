package com.pasc.lib.pay.common;

import android.content.Context;

import java.util.Map;

/**
 *  Created by huangtebian535 on 2018/09/03.
 *
 *  埋点统计接口，包括事件与页面。
 */
public interface IPascStatistics {

    /**
     *
     * @param eventID 事件标识
     */
    void onEvent(String eventID);

    /**
     *
     * @param eventID   事件标识
     * @param label 标签，可用于对事件标识的补充
     */
    void onEvent(String eventID, String label);

    /**
     *
     * @param eventID   事件标识
     * @param map   事件值
     */
    void onEvent(String eventID, Map<String, String> map);

    /**
     *
     * @param eventID   事件标识
     * @param label 标签，可用于对事件标识的补充
     * @param map   事件值
     */
    void onEvent(String eventID, String label, Map<String, String> map);

    /**
     *
     * @param pageName 页面名称
     */
    void onPageStart(String pageName);

    /**
     *
     * @param pageName  页面名称
     */
    void onPageEnd(String pageName);

    /**
     * 页面统计
     * @param context
     */
    void onResume(Context context);

    /**
     * 页面统计
     * @param context
     */
    void onPause(Context context);
}
