package com.pasc.ewallet.dev;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.pasc.business.ewallet.business.common.CommonUrl;
import com.pasc.business.ewallet.callback.OnEventListener;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.config.Constants;
import com.pasc.business.ewallet.config.PayTypeConfig;
import com.pasc.business.ewallet.openapi.PASCPay;
import com.pasc.business.ewallet.openapi.PayBuilder;
import com.pasc.ewallet.dev.behavior.CmbPayBehaviorHandler;
import com.pasc.ewallet.dev.behavior.MiniProgramPayBehaviorHandler;
import com.pasc.ewallet.dev.behavior.SnPayBehaviorHandler;
import com.pasc.ewallet.dev.behavior.UnionAliPayBehaviorHandler;
import com.pasc.ewallet.dev.behavior.UnionBankPayBehaviorHandler;
import com.pasc.ewallet.dev.behavior.UnionWechatPayBehaviorHandler;
import com.pasc.ewallet.dev.behavior.WftPayBehaviorHandler;
import com.pasc.ewallet.dev.utils.AppConstant;
import com.pasc.ewallet.dev.utils.AppUtil;
import com.pasc.ewallet.dev.utils.PayUtil;
import com.pasc.ewallet.dev.utils.SharePreUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

//import com.tencent.bugly.crashreport.CrashReport;


public class TheApplication extends Application {

    //测试环境的
    private static Context applicationContext;
    private static String AppIdDebug = "eb9cd984905a5b1891a9bdb0a50a0e44";
    private static String publicKeyDebug = "046F160C753F66D14EC40E136D2317EFAF027D64D022C68D1FDA2189F8F8AD95916514191F22BDB7B829B55F634634E6825C303B245E81661039F5DE6EFB96E932";
    private static String secretKeyDebug = "b0ec98cd545f566a933b9ac781f1c00d";


    private static String AppIdStg2 = "eb9cd984905a5b1891a9bdb0a50a0e44";
    private static String publicKeyStg2 = "046F160C753F66D14EC40E136D2317EFAF027D64D022C68D1FDA2189F8F8AD95916514191F22BDB7B829B55F634634E6825C303B245E81661039F5DE6EFB96E932";
    private static String secretKeyStg2 = "b0ec98cd545f566a933b9ac781f1c00d";

    //盐城生成环境
    //private static String AppIdRelease = "caa8d3dadaae11e98a342a2ae2dbcce4";
    //private static String publicKeyRelease = "0406329012CD0A7A20CF1EA5A55628B13C92A6AF1C848E1EF03B59E0477BEE055592EDD00F137169BF8F68C8C63D54CF7FFAF9EF9AA9DAA3CF54A1465BD2654DEE";
    //private static String secretKeyRelease = "c1c22d3d-baf3-2b9f-70bf-4897cdaf2945e77a8dc5-6d89d1e2";
    private static String AppIdRelease = "eb9cd984905a5b1891a9bdb0a50a0e44";
    private static String publicKeyRelease = "046F160C753F66D14EC40E136D2317EFAF027D64D022C68D1FDA2189F8F8AD95916514191F22BDB7B829B55F634634E6825C303B245E81661039F5DE6EFB96E932";
    private static String secretKeyRelease = "b0ec98cd545f566a933b9ac781f1c00d";

    private static String wxPayAppId = BuildConfig.WX_APP_ID;

    @Override
    public void onCreate() {
        super.onCreate ();
        applicationContext = getApplicationContext ();
        if (AppUtil.getPIDName (applicationContext).equals (applicationContext.getPackageName ())) {
            int env = SharePreUtil.getInt (AppConstant.key_pre_env, Constants.PAY_STG2_ENV);
            Constants.currentEnv = env;
            if (Constants.currentEnv == Constants.PAY_RELEASE_ENV) {
                PASCPay.getInstance ().init (
                        new PayBuilder.Build (this)
                                .publicKey (publicKeyRelease)
                                .appId (AppIdRelease)
                                .secretKey (secretKeyRelease)
                                .wxPayAppId (wxPayAppId)
                                .isDebug (false)
                                .hostAndGateWay (CommonUrl.RELEASE_ENV_HOST_URL)
                                .build ());
//                PASCPay.getInstance ().init (this, publicKeyRelease, AppIdRelease, secretKeyRelease, wxPayAppId, true, CommonUrl.RELEASE_ENV_HOST_URL);
            } else if (Constants.currentEnv == Constants.PAY_STG2_ENV) {
                PASCPay.getInstance ().init (this, publicKeyStg2, AppIdStg2, secretKeyStg2, wxPayAppId, true, CommonUrl.STG2_ENV_HOST_URL);
            } else {
                PASCPay.getInstance ().init (this, publicKeyDebug, AppIdDebug, secretKeyDebug, wxPayAppId, true, CommonUrl.TEST_ENV_HOST_URL);
            }
            PayTypeConfig.getInstance()
                .addCustomPayType(PayUtil.PayType.SNPAY, new SnPayBehaviorHandler())
                .addCustomPayType(PayUtil.PayType.CMBCHINAPAY, new CmbPayBehaviorHandler())
                .addCustomPayType(PayUtil.PayType.SWIFT, new WftPayBehaviorHandler())
                .addCustomPayType(PayUtil.PayType.WECHATJSAPI, new MiniProgramPayBehaviorHandler())
                .addCustomPayType(PayUtil.PayType.UNION_ALIPAYJSAPI, new UnionAliPayBehaviorHandler())
                .addCustomPayType(PayUtil.PayType.UNION_WECHATJSAPI, new UnionWechatPayBehaviorHandler())
                .addCustomPayType(PayUtil.PayType.UNION_BANK, new UnionBankPayBehaviorHandler())
                .addCustomPayType(PayUtil.PayType.PABCLOUDPAY, new MiniProgramPayBehaviorHandler());

            PASCPay.getInstance ().setOnEventListener (new OnEventListener () {
                @Override
                public void onEvent(String eventID, String label, Map<String, String> map) {
                    LogUtil.logd ("pascPayEvent", "eventID: " + eventID + " , label: " + label + " , map: " + map);
                }
//                @Override
//                public void onPause(Context context) {
//                    LogUtil.logd ("pascPayEvent","onPause: "+context.getClass ().getName ());
//                }
//                @Override
//                public void onResume(Context context) {
//                    LogUtil.logd ("pascPayEvent","onResume: "+context.getClass ().getName ());
//                }
            });
            // Android P 不显示Api提醒窗口
            closeAndroidPDialog ();
        }


    }

    private void initBugly() {

//        CrashReport.initCrashReport(applicationContext, AppConstant.BUGLY_APPID, true);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext (base);
        MultiDex.install (this);
    }

    public static Context getApplication() {
        return applicationContext;
    }


    /**
     * Android P 后谷歌限制了开发者调用非官方公开API 方法或接口，
     * 你用反射直接调用源码就会有提示弹窗出现
     * <p>
     * 用反射干掉这个 每次启动都会弹出的提醒窗口
     */
    private void closeAndroidPDialog() {
        try {
            Class cls = Class.forName ("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod ("currentActivityThread");
            declaredMethod.setAccessible (true);
            Object activityThread = declaredMethod.invoke (null);
            Field mHiddenApiWarningShown = cls.getDeclaredField ("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible (true);
            mHiddenApiWarningShown.setBoolean (activityThread, true);
        } catch (Exception e) {
//            e.printStackTrace ();
        }
    }

}

