package com.pasc.business.ewallet.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;

public class ScreenUtils {

    private ScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static float getScreenDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    public static int getScreenDensityDpi() {
        return Resources.getSystem().getDisplayMetrics().densityDpi;
    }

    public static void setFullScreen(@NonNull Activity activity) {
        activity.requestWindowFeature(1);
        activity.getWindow().setFlags(1024, 1024);
    }

//    @SuppressLint("WrongConstant")
//    public static void setLandscape(@NonNull Activity activity) {
//        activity.setRequestedOrientation(0);
//    }

//    @SuppressLint("WrongConstant")
//    public static void setPortrait(@NonNull Activity activity) {
//        activity.setRequestedOrientation(1);
//    }

    public static boolean isLandscape() {
        return Resources.getSystem().getConfiguration().orientation == 2;
    }

    public static boolean isPortrait() {
        return Resources.getSystem().getConfiguration().orientation == 1;
    }

    public static int getScreenRotation(@NonNull Activity activity) {
        switch(activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case 0:
            default:
                return 0;
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return 270;
        }
    }

    public static Bitmap screenShot(@NonNull Activity activity) {
        return screenShot(activity, false);
    }

    public static Bitmap screenShot(@NonNull Activity activity, boolean isDeleteStatusBar) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret;
        if (isDeleteStatusBar) {
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = resources.getDimensionPixelSize(resourceId);
            ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight);
        } else {
            ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        }

        decorView.destroyDrawingCache();
        return ret;
    }

    public static boolean isScreenLock(Context context) {
        @SuppressLint("WrongConstant") KeyguardManager km = (KeyguardManager)context.getSystemService("keyguard");
        return km.inKeyguardRestrictedInputMode();
    }

    public static void setSleepDuration(Context context, int duration) {
        Settings.System.putInt(context.getContentResolver(), "screen_off_timeout", duration);
    }

    public static int getSleepDuration(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), "screen_off_timeout");
        } catch (Settings.SettingNotFoundException var2) {
            var2.printStackTrace();
            return -123;
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static int getRealHeight(int width, int height) {
        if (width == 0) {
            return 0;
        } else {
            float aspectRatio = (float)width / (float)height;
            return (int)((float)getScreenWidth() / aspectRatio);
        }
    }

    public static int getScreenStatuBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return metrics.heightPixels - rect.height();
    }


}
