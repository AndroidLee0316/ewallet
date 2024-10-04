package com.pasc.business.ewallet.common.utils;

import android.util.Log;
import com.pasc.business.ewallet.NotProguard;

@NotProguard
public class LogUtil {
    public static boolean isDebug = true;
    private static String TAG="pascPayLog";
    private static boolean isEmpty(String msg) {
        return msg == null || msg.trim ().length () == 0;
    }

    public static void loge(String msg) {
        loge (TAG,msg);
    }

    public static void loge(String tag,String msg) {
        if (isDebug) {
            if (!isEmpty (msg)) {
                Log.e (tag, msg);
            }
        }
    }


    public static void logd(String msg) {
        logd (TAG,msg);

    }
    public static void logd(String tag,String msg) {
        if (isDebug) {
            if (!isEmpty (msg)) {
                Log.d (tag, msg);
            }
        }
    }
}
