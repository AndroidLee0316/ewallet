package com.pasc.lib.pay.statistics;

import android.app.Application;
import android.content.Context;

import com.pasc.lib.pay.statistics.td.TDStatisticsConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huangtebian535 on 2018/09/03.
 *
 * 运营埋点与异常统计
 */
public class StatisticsManager implements IPascStatistics {

    /**
     * 天眼统计初始化
     * @param application
     * @param channelId
     * @param isTest
     */
    public static void initStatistics(Application application, String channelId, String appId, boolean isTest){
        TDStatisticsConfig config = new TDStatisticsConfig();
        config.setAppID(appId);
        config.setChannel(channelId);
        // 天眼测试环境开关
        config.setTestOn(isTest);
        // 用于开发阶段验证
        config.setLogOn(isTest);

        StatisticsManager.getInstance().addStatistics(config.createPascStats(application));
    }

//    private List<IPascStatistics> pascStatisticsList = new ArrayList<>();
    private IPascStatistics stat;
    private static class SingletonHolder {
        private static final StatisticsManager INSTANCE = new StatisticsManager();
    }
    /**
     * 获取实例对象
     */
    public static StatisticsManager getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 启动埋点统计
     *
     **/
    public void addStatistics(IPascStatistics statistics) {

        if (statistics != null) {
            this.stat=statistics;
//            pascStatisticsList.add(statistics);
        }
    }

    @Override
    public void onEvent(String eventID) {
//        for (IPascStatistics stat : pascStatisticsList) {
//            stat.onEvent(eventID);
//        }
        if (stat!=null){
            stat.onEvent (eventID);
        }
    }

    @Override
    public void onEvent(String eventID, String label) {
//        for (IPascStatistics stat : pascStatisticsList) {
//            stat.onEvent(eventID, label);
//        }
        if (stat!=null){
            stat.onEvent (eventID,label);
        }
    }

    @Override
    public void onEvent(String eventID, Map<String, String> map) {
//        for (IPascStatistics stat : pascStatisticsList) {
//            stat.onEvent(eventID, map);
//        }
        if (stat!=null){
            stat.onEvent (eventID,map);
        }
    }

    @Override
    public void onEvent(String eventID, String label, Map<String, String> map) {
//        for (IPascStatistics stat : pascStatisticsList) {
//            stat.onEvent(eventID, label, map);
//        }
        if (stat!=null){
            stat.onEvent (eventID,label,map);
        }
    }

    @Override
    public void onPageStart(String pageName) {
//        for (IPascStatistics stat : pascStatisticsList) {
//            stat.onPageStart(pageName);
//        }
        if (stat!=null){
            stat.onPageStart (pageName);
        }
    }

    @Override
    public void onPageEnd(String pageName) {
//        for (IPascStatistics stat : pascStatisticsList) {
////            stat.onPageEnd(pageName);
////        }
        if (stat!=null){
            stat.onPageEnd (pageName);
        }
    }

    @Override
    public void onPause(Context context) {
//        for (IPascStatistics stat : pascStatisticsList) {
//            stat.onPause(context);
//        }
        if (stat!=null){
            stat.onPause (context);
        }
    }

    @Override
    public void onResume(Context context) {
//        for (IPascStatistics stat : pascStatisticsList) {
//            stat.onResume(context);
//        }
        if (stat!=null){
            stat.onResume (context);
        }
    }
}