package com.pasc.lib.pay.common.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.text.TextUtils;

import com.pasc.lib.pay.common.AppProxy;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * <p>
 * 应用内部工具类，如版本号，进程名等
 */
public class AppUtils {

    /**
     * 获取版本名
     *
     */
    public static String getVersionName() {
        String versionName = "";
        try {
            PackageInfo packageInfo = AppProxy.getInstance().getApplication()
                    .getPackageManager()
                    .getPackageInfo(AppProxy.getInstance().getApplication().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
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
     * 跳转应用市场
     *
     * @param activity
     */
    public static void goAppMarkets(Activity activity, String packageName) {
        try {
            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent intent = new Intent(new Intent(Intent.ACTION_VIEW, uri));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception ex) {
            ToastUtils.toastMsg("您的手机还没有安装任何安装安装应用市场");
        }
    }

    public static String getHostName(String urlString) {
        String head = "";
        int index = urlString.indexOf("://");
        if (index != -1) {
            head = urlString.substring(0, index + 3);
            urlString = urlString.substring(index + 3);
        }
        index = urlString.indexOf("/");
        if (index != -1) {
            urlString = urlString.substring(0, index + 1);
        }
        return head + urlString;
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
     * 获取asserts文件的字节数组.
     *
     * @param context  上下文.
     * @param fileName 文件名.
     * @return 文件对应的字节数组.
     */
    public static byte[] getAssertsFile(Context context, String fileName) {
        InputStream inputStream = null;
        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(fileName);
            if (inputStream == null) {
                return null;
            }

            BufferedInputStream bis = null;
            int length;
            try {
                bis = new BufferedInputStream(inputStream);
                length = bis.available();
                byte[] data = new byte[length];
                bis.read(data);

                return data;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 读取assets下json文件数据.
     *
     * @param fileName 文件名.
     * @param context  上下文.
     * @return json字符串.
     */
    public static String getAssetsJson(Context context, String fileName) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 获取模拟数据.
     *
     * @param context      上下文.
     * @param jsonFileName json文件名.
     * @param classType    要转换的数据类型.
     * @param <T>
     * @return json所对应的数据对象.
     */
    public static <T> T getSimulatedData(Context context, String jsonFileName, Class<T> classType) {
        String json = AppUtils.getAssetsJson(context, "simulatedData/" + jsonFileName);
        return JsonUtils.fromJson(json, classType);
    }

    /**
     * 从意图的获取布尔数据.
     *
     * @param intent       意图.
     * @param key          键值.
     * @param defaultValue 默认值.
     * @return 布尔值.
     */
    public static boolean getExtraValueBoolean(Intent intent, String key, boolean defaultValue) {
        Boolean booleanValue = null;
        String valueString = intent.getStringExtra(key);
        if (!TextUtils.isEmpty(valueString)) {
            try {
                booleanValue = Boolean.parseBoolean(valueString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (booleanValue == null) {
            booleanValue = intent.getBooleanExtra(key, defaultValue);
        }
        return booleanValue;
    }

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
