package com.pasc.business.ewallet.openapi;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.pasc.business.ewallet.BuildConfig;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.callback.OnEventListener;
import com.pasc.business.ewallet.callback.OnOpenListener;
import com.pasc.business.ewallet.callback.OnPayListener;
import com.pasc.business.ewallet.callback.OnSignListener;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.FreeSecretSignEvent;
import com.pasc.business.ewallet.common.event.ThirdPayEvent;
import com.pasc.business.ewallet.common.utils.ClickUtils;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.config.PayBuildConfig;
import com.pasc.business.ewallet.inner.Location;
import com.pasc.business.ewallet.inner.PayManager;
import com.pasc.business.ewallet.result.PASCPayResult;
import com.pasc.business.ewallet.result.PASCSignResult;
import com.pasc.business.ewallet.result.PayType;
import com.pasc.lib.pay.common.util.ToastUtils;

class PayManagerProxy implements Pay {

    private PayInfo payInfo;

    PayManagerProxy() {
        super ();
    }

    @Override
    public void init(@NonNull Application application,
                     @NonNull String publicKey,
                     @NonNull String AppId,
                     @NonNull String secretKey, @NonNull String wxPayAppId,
                     @NonNull boolean isDebug) {
       init (application,publicKey,AppId,secretKey,wxPayAppId,isDebug,null);
    }

    @Override
    public void init(@NonNull Application application, @NonNull String publicKey, @NonNull String AppId, @NonNull String secretKey, @NonNull String wxPayAppId, @NonNull boolean isDebug, String hostAndGateWay) {
        PayManager.getInstance ().init (application,publicKey,AppId,secretKey,wxPayAppId,isDebug,hostAndGateWay);
    }

    @Override
    public void setLBS(@NonNull PayLocation payLocation) {
        UserManager.getInstance ().setPayLocation (new Location (payLocation.getLongitude  (),payLocation.getLatitude ()));
    }

    @Override
    public void open(@NonNull Context context, @NonNull String merchantNo, String memberNo, @NonNull OnOpenListener openListener) {
        if (ClickUtils.isDoubleClick ()) {
            return;
        }
        if (Util.isEmpty (memberNo)){
            ToastUtils.toastMsg (R.string.ewallet_toast_member_no_empty);
        }
        if (context == null  /****|| Util.isEmpty (merchantNo)***/ || Util.isEmpty (memberNo)) {
            if (openListener != null) {
                openListener.onOpenResult (PASCPayResult.PASC_PAY_CODE_PARAM_ERROR, PASCPayResult.PASC_PAY_MSG_PARAM_ERROR);
            }
            return;
        }
        PayManager.getInstance ().open (context, merchantNo, memberNo, openListener);
    }

    @Override
    public void sign(@NonNull Context context, @NonNull String memberNo, @NonNull String channel,
        @NonNull String sceneId, OnSignListener listener) {
        if (Util.isEmpty (memberNo) || Util.isEmpty (channel) || Util.isEmpty (sceneId)) {
            if (listener != null) {
                listener.onSignResult(PASCSignResult.PASC_SIGN_CODE_PARAM_ERROR, PASCSignResult.PASC_SIGN_MSG_PARAM_ERROR);
            }
            return;
        }
        PayManager.getInstance().sign(context, memberNo, channel, sceneId, listener);
    }

    @Override
    public void sign(@NonNull Context context, @NonNull String memberNo, @NonNull String merchantNo, @NonNull String channel,
        @NonNull String scheme, @NonNull String sceneId, OnSignListener listener) {
        if (Util.isEmpty(memberNo) || Util.isEmpty(merchantNo) || Util.isEmpty(channel) || Util.isEmpty(sceneId)) {
            if (listener != null) {
                listener.onSignResult(PASCSignResult.PASC_SIGN_CODE_PARAM_ERROR, PASCSignResult.PASC_SIGN_MSG_PARAM_ERROR);
            }
            return;
        }
        PayManager.getInstance().sign(context, memberNo, merchantNo, channel, scheme, sceneId, listener);
    }

    @Override
    public void pay(@NonNull Context context, @NonNull String merchantNo, String memberNo,
                    @NonNull String orderNo, @NonNull OnPayListener listener) {
        pay (context,merchantNo,memberNo,orderNo,null,listener);
    }

    @Override
    public void pay(@NonNull Context context,  String merchantNo, String memberNo, String orderNo,String option, OnPayListener listener) {
        if (ClickUtils.isDoubleClick ()) {
            return;
        }
        if (BuildConfig.currentBuildType == PayBuildConfig.jhPayBuildType){
            if (Util.isEmpty (orderNo)) {
                ToastUtils.toastMsg (R.string.ewallet_toast_order_no_empty);
            }
        }else {
            if (Util.isEmpty (orderNo)) {
                ToastUtils.toastMsg (R.string.ewallet_toast_order_no_empty);
            } else if (Util.isEmpty (merchantNo)) {
                ToastUtils.toastMsg (R.string.ewallet_toast_merchant_no_empty);
            }
            //else if (Util.isEmpty (memberNo)) {
            //    ToastUtils.toastMsg (R.string.ewallet_toast_member_no_empty);
            //}
        }
        if (context == null || Util.isEmpty (merchantNo) /*|| Util.isEmpty (memberNo)*/ || Util.isEmpty (orderNo)) {
            if (listener != null) {
                listener.onPayResult (PASCPayResult.PASC_PAY_CODE_PARAM_ERROR, PASCPayResult.PASC_PAY_MSG_PARAM_ERROR);
            }
            return;
        }
        payInfo = new PayInfo ("", orderNo);
        PayManager.getInstance ().pay (context, merchantNo, memberNo, orderNo,option,listener);
    }

    @Override
    public void recharge(@NonNull Context context, @NonNull String memberNo,
        @NonNull String mchOrderNo, long amount, OnPayListener listener) {
        if (Util.isEmpty (memberNo)) {
            ToastUtils.toastMsg (R.string.ewallet_toast_member_no_empty);
        } else if (Util.isEmpty (mchOrderNo)) {
            ToastUtils.toastMsg (R.string.ewallet_toast_order_no_empty);
        }
        if (Util.isEmpty (memberNo) || Util.isEmpty (mchOrderNo)) {
            if (listener != null) {
                listener.onPayResult (PASCPayResult.PASC_PAY_CODE_PARAM_ERROR, PASCPayResult.PASC_PAY_MSG_PARAM_ERROR);
            }
            return;
        }
        PayManager.getInstance().recharge(context, memberNo, mchOrderNo, amount, listener);
    }

    @Override
    public PayInfo getPayInfo() {
        return payInfo;
    }

    @Override
    public void notifyPayResult(String payType, int payResult, String msg) {
        EventBusManager.getDefault().post(new ThirdPayEvent(payType, payResult, msg));
    }

    @Override public void notifySignResult(int signType, int signResult, String msg) {
        switch (signType) {
            case PayType.WECHATPAY:
                EventBusManager.getDefault ().post(new FreeSecretSignEvent(signType, signResult));
                break;
            case PayType.ALIPAY:
                break;

            default:
                LogUtil.loge (" signType is weChatPay or AliPay !");
        }
    }

    @Override
    public void setOnEventListener(OnEventListener onEventListener) {
        PayManager.getInstance ().setOnEventListener (onEventListener);
    }

    @Override
    public OnEventListener getOnEventListener() {
        return  PayManager.getInstance ().getOnEventListener ();
    }

    @Override
    public OnPayListener getOnPayListener() {
        return  PayManager.getInstance ().getOnPayListener ();
    }

    @Override public OnSignListener getOnSignListener() {
        return PayManager.getInstance().getOnSignListener();
    }

    @Override
    public OnOpenListener getOnOpenListener() {
        return  PayManager.getInstance ().getOnOpenListener ();
    }


    @Override
    public void detach(Context context) {

    }

    @Override
    public Application getApplication() {
        return  PayManager.getInstance ().getApplication ();
    }
}
