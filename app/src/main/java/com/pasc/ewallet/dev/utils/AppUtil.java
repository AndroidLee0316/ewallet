package com.pasc.ewallet.dev.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 应用内部工具类，如版本号，进程名等
 */
public class AppUtil {

    /**
     * 获取版本名
     *
     * @param ctx
     * @return
     */
    public static String getVersionName(Context ctx) {
        String versionName = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取版本code
     */
    public static long getVersionCode(Context context) {
        long versionCode = 0;
        try {
            PackageInfo packageInfo = context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获得进程名字
     */
    public static String getPIDName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    /**
     * 获取格式yyMMdd的日期
     * @return
     */
    public static String getDate(){
        String dateStr = "";
        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd_HH-mm-ss");
        dateStr = format.format(new Date());

        return dateStr;
    }

}
