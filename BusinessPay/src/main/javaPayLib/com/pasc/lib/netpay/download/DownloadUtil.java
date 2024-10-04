package com.pasc.lib.netpay.download;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;

/**
 * Created by YZJ on 2017/6/8: 21.
 * 下载工具类
 */
public class DownloadUtil {
    /***没下载完的后缀名***/
    public static final String loadTmpName = ".tmp";

    /****下载完成后去掉后缀名  .tmp ******/
    public static void removeTmpName(File sourceFile) {
        if (sourceFile != null) {
            String sourcePaht = sourceFile.getAbsolutePath();
            int index = sourcePaht.indexOf(loadTmpName);
            if (index > 0) {
                File desFile = new File(sourcePaht.substring(0, index));
                sourceFile.renameTo(desFile);
            }
        }
    }

    /***下载目录跟路径***/
    public static String getLoadPathRoot(Context context) {
        String pkgName = context.getPackageName();
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            if (Build.VERSION.SDK_INT >= 23) {
                // 运行时权限
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 没sdcard 权限
                    sdDir = context.getCacheDir();
                }else {
                    // 有权限
                    sdDir = new File(Environment.getExternalStorageDirectory(), pkgName);
                }
            }else {
                sdDir = new File(Environment.getExternalStorageDirectory(), pkgName);//获取跟目录

            }
            if (!sdDir.exists()) {
               boolean success= sdDir.mkdirs();
                if (!success){
                   sdDir = context.getCacheDir();
               }
            }
        } else {
            sdDir = context.getCacheDir();
        }
        if (!sdDir.exists()) {
            sdDir.mkdirs();
        }
        return sdDir.toString();
    }
    /**
     *
     * @param context
     * @param filePath
     * @return
     */
    private static boolean apkIsOk(Context context,String filePath) {
        boolean result = false;
        try {
            if (!TextUtils.isEmpty(filePath) && filePath.endsWith(".apk")) {
                PackageManager pm = context.getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
                if (info != null) {
                    result = true;//完整
                }
            }else {
                return true;
            }
        } catch (Exception e) {
            result = false;//不完整
        }
        return result;
    }
    /**
     * 关闭流
     *
     * @param closeable
     */
    public static void closeIo(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /******安装apk*******/
    public static void install(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /******卸载apk*******/
    public static void unInstall(Context context, String pkgName) {
        Uri packageURI = Uri.parse("package:" + pkgName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }

    /******打开apk****/
    public static void openApk(Context context, String pkgName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }


}
