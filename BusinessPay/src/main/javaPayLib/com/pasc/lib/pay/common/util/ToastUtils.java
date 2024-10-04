package com.pasc.lib.pay.common.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.lib.pay.common.AppProxy;

/**
 * Created by yintangwen952 on 2018/9/2.
 */
@NotProguard
public class ToastUtils {

    private static Context sCtx;
    private static Toast sToast;

    public static void toastMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        cancel();
        checkContext();
        if (sCtx==null){
            LogUtil.loge ("ToastUtils context==null");
            return;
        }
        sToast = ToastCompat.makeText(sCtx, msg, Toast.LENGTH_SHORT);
        sToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        sToast.show();
    }
    public static void toastLongMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        cancel();
        checkContext();
        if (sCtx==null){
            LogUtil.loge ("ToastUtils context==null");
            return;
        }
        sToast = ToastCompat.makeText(sCtx, msg, Toast.LENGTH_LONG);
        sToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        sToast.show();
    }

    public static void toastMsg(int msgId) {
        checkContext();
        if (sCtx==null){
            LogUtil.loge ("ToastUtils context==null");
            return;
        }
        toastMsg(sCtx.getResources().getString(msgId));
    }

    private static void checkContext() {
        if (sCtx == null) {
            sCtx = AppProxy.getInstance().getApplication();
        }
    }

    /**
     * 自定义toast样式
     */
    public static void toastMsgWithStyle(View toastVieww) {
        if (sToast != null) {
            sToast.cancel();
        }
        checkContext();
        if (sCtx==null){
            LogUtil.loge ("ToastUtils context==null");
            return;
        }
        sToast = ToastCompat.newToast (sCtx);
        sToast.setDuration(Toast.LENGTH_SHORT);
        sToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        sToast.setView(toastVieww);
        sToast.show();
    }

    public static void toastMsg(@DrawableRes int icon, String msg){
        checkContext();
        if (sCtx==null){
            LogUtil.loge ("ToastUtils context==null");
            return;
        }
        View view= LayoutInflater.from (sCtx).inflate (R.layout.ewallet_toast_layout,null);
        ImageView iv=view.findViewById (R.id.ewallet_toast_iv);
        TextView tv=view.findViewById (R.id.ewallet_toast_tv);
        iv.setImageResource (icon);
        tv.setText (msg);
        toastMsgWithStyle (view);

    }

    /**
     * 取消toast
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }
}
