package com.pasc.lib.pay.common.util;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * @date 2019-09-23
 * @des
 * @modify
 **/
public class ToastCompat {
    private static Field sField_TN;
    private static Field sField_TN_Handler;

    static {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            try {
                sField_TN = Toast.class.getDeclaredField ("mTN");
                sField_TN.setAccessible (true);

                sField_TN_Handler = sField_TN.getType ().getDeclaredField ("mHandler");
                sField_TN_Handler.setAccessible (true);
            } catch (Exception e) {
            }
        }
    }

    private static void hook(Toast toast) {
        try {
            Object tn = sField_TN.get (toast);
            if (tn instanceof SafelyHandlerWrapper && tn!=null){
                return;
            }
            Handler preHandler = (Handler) sField_TN_Handler.get (tn);
            sField_TN_Handler.set (tn, new SafelyHandlerWrapper (preHandler));
        } catch (Exception e) {
        }
    }

    public static Toast makeText(Context context, CharSequence cs, int duration) {
        Toast toast = Toast.makeText (context, cs, duration);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            hook (toast);
        }
        return toast;
    }

    public static Toast newToast(Context context){
        Toast toast = new Toast (context);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            hook (toast);
        }
        return toast;
    }

    private static class SafelyHandlerWrapper extends Handler {

        private Handler impl;

        public SafelyHandlerWrapper(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                impl.handleMessage (msg);
            } catch (Exception e) {
                e.printStackTrace ();
            }

        }
    }
}
