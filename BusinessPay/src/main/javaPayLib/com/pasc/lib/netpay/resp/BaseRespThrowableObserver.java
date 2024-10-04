package com.pasc.lib.netpay.resp;

import android.app.Activity;
import android.text.TextUtils;
import com.pasc.business.ewallet.ActivityManager;
import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.TokenInvalidEventType;
import com.pasc.lib.netpay.ApiV2Error;
import com.pasc.lib.netpay.ExceptionHandler;
import io.reactivex.functions.Consumer;
@NotProguard
public abstract class BaseRespThrowableObserver implements Consumer<Throwable> {
    @Override
    public void accept(Throwable throwable) {

        String errorCode = ExceptionHandler.getExceptionV2WithCode (throwable);
        String msg = ExceptionHandler.handleException (throwable);
        String errorCodeStr = ExceptionHandler.getExceptionV2WithCode (throwable);
        if (ExceptionHandler.isSystemError (errorCode)) {
            //服务器错误
            if (!(throwable instanceof ApiV2Error)) {
                msg = ExceptionHandler.SYSTEM_ERROR_EXCEPTION;
            }
        }
        if (TextUtils.isEmpty (msg)) {
            //数据为空
            msg = errorCodeStr;
        }
        //token 失效拦截
        if (StatusTable.Account.USER_TOKEN_INVALID.equals (errorCodeStr)) {
            //弹框
            Activity currentActivity = ActivityManager.getInstance ().getCurrentActivity ();
            if (currentActivity != null) {
                TokenInvalidEventType tokenInvalidEventType = new TokenInvalidEventType ();
                tokenInvalidEventType.mActivity = currentActivity;
                tokenInvalidEventType.msg = msg;
                EventBusManager.getDefault ().post (tokenInvalidEventType);
                return;
            }
        }

        onV2Error (errorCodeStr, msg);
        onV2Error (errorCodeStr, msg, throwable);
    }

    public void onV2Error(String code, String msg) {
    }

    public void onV2Error(String code, String msg, Throwable throwable) {
    }


}
