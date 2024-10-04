package com.pasc.business.ewallet;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.pasc.business.ewallet.business.pay.ui.PayMainStandActivity;
import com.pasc.business.ewallet.business.pay.wechat.WechatPayUtil;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.inner.PayManager;

import java.lang.ref.WeakReference;

public class ActivityManager {

    private static ActivityManager mActivityManager;
    private WeakReference<Activity> mActivityWeakReference;

    private ActivityManager () {
        super();
    }

    public static ActivityManager getInstance(){
        if (mActivityManager == null){
            mActivityManager = new ActivityManager();
        }
        return mActivityManager;
    }

    public void init(Application application){
        registerLifecycle(application);
    }

    public Activity getCurrentActivity(){
        if (mActivityWeakReference.get() != null && !mActivityWeakReference.get().isFinishing()){
            return mActivityWeakReference.get();
        }
        return null;
    }

    //监听所有activity的生命周期
    private void registerLifecycle(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (BuildConfig.DEBUG) {
                    LogUtil.loge ("onActivityResumed: " + activity.getClass ().getSimpleName ());
                }
                mActivityWeakReference = new WeakReference<>(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
//                LogUtil.loge ("onActivityPaused: "+activity.getClass ().getSimpleName ());

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (activity!=null && activity instanceof PayMainStandActivity){
                    PayMainStandActivity standActivity= (PayMainStandActivity) activity;
                    if (standActivity.isPayMode ()) {
                        wxRegisterOrigin ();
                    }
                }
            }
        });
    }

    void wxRegisterOrigin(){
        WechatPayUtil.registerWxAppId (PayManager.getInstance ().getApplication (), WechatPayUtil.originWxAppId ());
    }
}

