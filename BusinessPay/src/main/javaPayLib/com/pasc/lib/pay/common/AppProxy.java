package com.pasc.lib.pay.common;

import android.app.Application;
import android.content.Context;

import com.pasc.business.ewallet.config.Constants;

public class AppProxy {
    private static Application mApplication;

    private static boolean sIsDebug = Constants.IS_DEBUG;

    public static AppProxy getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 静态内部类,只有在装载该内部类时才会去创建单例对象
     */
    private static class SingletonHolder {
        private static final AppProxy instance = new AppProxy();
    }

    public AppProxy init(Application application) {
        if (null == application) {
            throw new IllegalArgumentException("Illega application Exception, please check~ !");
        }

        AppProxy.mApplication = application;
        return this;
    }

    public Application getApplication() {
        if (null == mApplication) {
            throw new IllegalAccessError("Please initialize the AppProxy first.");
        }

        return mApplication;
    }

    public boolean isDebug() {
        return sIsDebug;
    }

    public Context getContext() {
        if (null == mApplication) {
            throw new IllegalAccessError("Please initialize the AppProxy first.");
        }

        return mApplication.getApplicationContext();
    }

}
