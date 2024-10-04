package com.pasc.lib.pay.statistics.td;

import android.content.Context;

import com.pasc.lib.pay.statistics.IPascStatistics;

/**
 *  Created by huangtebian535 on 2018/09/03.
 *
 *  天眼埋点配置
 */
public class TDStatisticsConfig {
    private String appID;
    private String channel;
    private boolean testOn;
    private boolean logOn;

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isTestOn() {
        return testOn;
    }

    public void setTestOn(boolean testOn) {
        this.testOn = testOn;
    }

    public boolean isLogOn() {
        return logOn;
    }

    public void setLogOn(boolean logOn) {
        this.logOn = logOn;
    }

    public IPascStatistics createPascStats(Context context) {
        if (context != null) {
            TDStatistics tdStatistics = new TDStatistics(context);
            tdStatistics.init(appID, channel, testOn, logOn);
            return tdStatistics;
        }
        return null;
    }
}
