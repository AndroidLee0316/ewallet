package com.pasc.business.ewallet.business.util;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.widget.dialog.OnCloseListener;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;
import com.pasc.business.ewallet.widget.dialog.common.ConfirmDialogFragment;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * @date 2019/7/2
 * @des 密码错误统一提示框
 * @modify
 **/
public class PwdDialogUtil {

    public static boolean pwdErrorIntercept(FragmentActivity activity, String code, String msg) {
        if (activity==null){
            return false;
        }
        String desc = activity.getString (R.string.ewallet_error_pay_pwd);
        if (!TextUtils.isEmpty (msg)) {
            desc = msg;
        }
        MyPwdErrorListener pwdErrorListener = new MyPwdErrorListener (activity);
        if (StatusTable.PassWord.PAY_PWD_ERROR.equals (code) || StatusTable.PassWord.PASSWORD_ERROR.equals (code)) {
            PwdDialogUtil.pwdErrorLessThan3 (activity, desc, pwdErrorListener);
            return true;
        } else if (StatusTable.PassWord.PAY_PWD_ERROR_MT_3.equals (code)) {
            PwdDialogUtil.pwdErrorMoreThan3 (activity, desc, pwdErrorListener);
            return true;
        } else if (StatusTable.PassWord.PAY_PWD_ERROR_MT_5.equals (code)) {
            PwdDialogUtil.pwdErrorMoreThan5 (activity, desc, pwdErrorListener);
            return true;
        }
        return false;
    }

    public static class MyPwdErrorListener implements PwdErrorListener {

        private Activity activity;

        public MyPwdErrorListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void forgetPwd() {
          //跳转忘记密码
            RouterManager.PassWordRouter.gotoForgetPwd (activity);
        }

        @Override
        public void cancelEnter() {

        }
    }


    public interface PwdErrorListener {
        void forgetPwd(); // 忘记密码

        void cancelEnter(); //取消
    }


    /**
     * 忘记密码
     * 重新输入
     * x <3
     **/
    public static void pwdErrorLessThan3(FragmentActivity activity, String msg, PwdErrorListener listener) {
        pwdError (activity, msg,
                R.color.ewallet_color_333333,
                "忘记密码", R.color.ewallet_color_999999,
                "重新输入", R.color.ewallet_primary_btn_enable,
                listener);
    }

    /**
     * 3=< x <5
     *
     * @param activity
     * @param msg
     * @param listener
     */
    public static void pwdErrorMoreThan3(FragmentActivity activity, String msg, PwdErrorListener listener) {
        pwdError (activity, msg,
                R.color.ewallet_color_333333,
                "忘记密码", R.color.ewallet_primary_btn_enable,
                "重新输入", R.color.ewallet_color_999999,
                listener);
    }

    /***
     *  x>=5
     * @param activity
     * @param msg
     * @param listener
     */
    public static void pwdErrorMoreThan5(FragmentActivity activity, String msg, PwdErrorListener listener) {
        pwdError (activity, msg,
                R.color.ewallet_color_333333,
                "忘记密码", R.color.ewallet_primary_btn_enable,
                "取消", R.color.ewallet_color_999999,
                listener);
    }


    public static void pwdError(FragmentActivity activity,
                                String msg, @ColorRes int msgColorRes,
                                String okText, @ColorRes int okColorRes,
                                String cancelText, @ColorRes int cancelColorRes,
                                PwdErrorListener listener) {
        if (1==1){
            ToastUtils.toastMsg (msg);
            return;
        }
        //重新输入,弹出提示
        //提示重新设置密码 ,todo 对话框，文案都反了
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment.Builder ()
                .setDesc (msg)
                .setDescLineSpacingExtra (4)
                .setDescSize (15)
                .setDescColor (activity.getResources ().getColor (msgColorRes))
                .setCloseText (okText)
                .setConfirmTextSize (18)
                .setCloseTextColor (activity.getResources ().getColor (okColorRes))
                .setConfirmText (cancelText)
                .setCloseTextSize (18)
                .setConfirmTextColor (activity.getResources ().getColor (cancelColorRes))
                .setOnConfirmListener (new OnConfirmListener<ConfirmDialogFragment> () {
                    @Override
                    public void onConfirm(ConfirmDialogFragment dialogFragment) {
                        // 重写输入
                        dialogFragment.dismiss ();
                        if (listener != null) {
                            listener.cancelEnter ();
                        }
                    }
                })
                .setOnCloseListener (new OnCloseListener<ConfirmDialogFragment> () {
                    @Override
                    public void onClose(ConfirmDialogFragment dialogFragment) {
                        dialogFragment.dismiss ();

                        if (listener != null) {
                            listener.forgetPwd ();
                        }
                    }
                })
                .build ();
        confirmDialogFragment.show (activity.getSupportFragmentManager (), "reInputOriginPwd");
    }

}
