package com.pasc.business.ewallet.widget.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;
import com.pasc.business.ewallet.widget.dialog.common.PermissionDialogFragment;
import com.pasc.business.ewallet.widget.dialognt.CommonDialog;

/**
 * Created by ex-lingchun001 on 2018/3/22.
 */

public class PermissionSettingUtils {
    public static final int REQUEST_CODE_APPLICATION_DETAILS_SETTINGS = 123;

    /**
     * 请求开启定位页面弹窗
     */
    public static void gotoLocationSetting(final Context context) {
        new CommonDialog(context)
                .setContent("定位服务未开启，请打开定位")
                .setButton1("取消")
                .setButton2("打开",CommonDialog.Blue_3B68E9)
                .setOnButtonClickListener(new CommonDialog.OnButtonClickListener() {
                    @Override
                    public void button2Click() {
                        gotoApplicationDetails(context);
                    }
                })
                .show();
    }


    /**
     * 打开权限设置页面
     * 特殊手机型号特殊处理
     */
    public static void gotoPermissionSetting (final FragmentActivity fragmentActivity, @NonNull String... permissions) {
        if (permissions.length == 0){
            return;
        }
        String title = "";
        String desc = "";
        @DrawableRes int drawableID = 0;
        if (permissions.length == 1){
            if (Manifest.permission.CAMERA.equals(permissions[0])){
                //拍照权限
                title = "开启相机";
                desc = "为您提供更完善的服务";
                drawableID = R.drawable.ewallet_widget_ic_permission_storage;
            } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0])){
                //sd卡读写权限
                title = "开启存储";
                desc = "为您提供更完善的服务";
                drawableID = R.drawable.ewallet_widget_ic_permission_camera;
            }
        }else {
            title = "开启相关权限";
            for (int i = 0; i < permissions.length; i++){
                if (Manifest.permission.CAMERA.equals(permissions[i])){
                    desc += "相机";
                } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[i])){
                    desc += "存储";
                }
                if (permissions.length - 1 == i){
                    break;
                }
                desc += "、";
            }
            drawableID = R.drawable.ewallet_widget_ic_permission_common;
        }

        if (TextUtils.isEmpty(title)){
            return;
        }
        new PermissionDialogFragment.Builder()
                .setConfirmText("去开启")
                .setDesc(desc)
                .setTitle(title)
                .setImageRes(drawableID)
                .setOnConfirmListener(new OnConfirmListener<PermissionDialogFragment>() {
                    @Override
                    public void onConfirm (PermissionDialogFragment dialogFragment) {
                        gotoApplicationDetails(fragmentActivity);
                        dialogFragment.dismiss();
                    }
                })
                .build()
                .show(fragmentActivity);

    }


    /**
     * 打开权限设置页面
     * 无差别打开应用详情页
     */
    public static void gotoApplicationDetails(Context context) {
        Intent appIntent;
        //魅族(目前发现魅族 M6 Note（Android 7.1.2，Flyme 6.1.4.7A）出现在应用信息页打开权限不管用的情况，必须在管家中打开方可生效，所以魅族手机暂定跳转手机管家)
        appIntent = context.getPackageManager()
                .getLaunchIntentForPackage("com.meizu.safe");
        if (appIntent != null) {
            context.startActivity(appIntent);
            return;
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings",
                    "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }
}
