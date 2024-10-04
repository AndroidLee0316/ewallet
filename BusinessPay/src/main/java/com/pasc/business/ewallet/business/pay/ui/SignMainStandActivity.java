package com.pasc.business.ewallet.business.pay.ui;

import android.os.Bundle;
import android.widget.RelativeLayout;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.base.MultiPresenter;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.pay.net.resp.ApplySignResp;
import com.pasc.business.ewallet.business.pay.net.resp.SignStatusResp;
import com.pasc.business.ewallet.business.pay.presenter.ApplySignPresenter;
import com.pasc.business.ewallet.business.pay.presenter.QuerySignStatusPresenter;
import com.pasc.business.ewallet.business.pay.view.ApplySignView;
import com.pasc.business.ewallet.business.pay.view.QuerySignStatusView;
import com.pasc.business.ewallet.business.pay.wechat.WechatPayUtil;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.FreeSecretSignEvent;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.inner.PayManager;
import com.pasc.business.ewallet.result.PASCSignResult;
import com.pasc.business.ewallet.result.PayType;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * 签约主界面
 *
 * @date 2019/12/15
 * @des
 * @modify
 **/
public class SignMainStandActivity extends EwalletBaseMvpActivity<MultiPresenter> implements
    ApplySignView, QuerySignStatusView {

    private RelativeLayout rootView;
    private String memberNo, merchantNo, channel, scheme, sceneId;
    private boolean weChatHasCallBack = false;
    private boolean weChatCallSuccess = false;
    private boolean needQueryResult = false;
    private boolean  isFinishing = false;

    private ApplySignPresenter applySignPresenter;
    private QuerySignStatusPresenter signStatusPresenter;

    @Override
    protected MultiPresenter createPresenter() {
        MultiPresenter multiPresenter = new MultiPresenter ();
        multiPresenter.requestPresenter (applySignPresenter = new ApplySignPresenter());
        multiPresenter.requestPresenter (signStatusPresenter = new QuerySignStatusPresenter());
        return multiPresenter;
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_sign_main_activity;
    }


    @Override
    protected void initView() {
        isFinishing=false;
        rootView = findViewById(R.id.rl_rootview);
    }

    @Override
    protected void statusBarColor() {
    }

    @Override
    protected void initData(Bundle bundleData) {
        weChatHasCallBack = false;
        weChatCallSuccess = false;
        memberNo = bundleData.getString (BundleKey.Pay.key_memberNo);
        merchantNo = bundleData.getString (BundleKey.Pay.key_merchantNo);
        channel = bundleData.getString (BundleKey.Pay.key_channel);
        scheme = bundleData.getString (BundleKey.Pay.key_scheme);
        sceneId = bundleData.getString (BundleKey.Pay.key_sceneId);
        applySign();

    }

    private void applySign() {
        boolean errFlag = false;
        LogUtil.loge (getSimpleName () + " memberNo: " + memberNo + " ,channel: " + channel + " ,sceneId: " + sceneId);
        if (Util.isEmpty (memberNo)) {
            errFlag = true;
            ToastUtils.toastMsg (R.string.ewallet_toast_member_no_empty);
        } else if (Util.isEmpty (channel)) {
            errFlag = true;
            ToastUtils.toastMsg (R.string.ewallet_toast_channel_no_empty);
        } else if (Util.isEmpty (sceneId)) {
            errFlag = true;
            ToastUtils.toastMsg (R.string.ewallet_toast_sceneid_no_empty);
        } else if (Util.isEmpty (merchantNo) && "ALIPAY".equals(channel)) {
            errFlag = true;
            ToastUtils.toastMsg (R.string.ewallet_toast_merchant_no_no_empty);
        }

        if (errFlag) {
            if (PayManager.getInstance ().getOnSignListener () != null) {
                PayManager.getInstance ().getOnSignListener ().onSignResult (PASCSignResult.PASC_SIGN_CODE_PARAM_ERROR, PASCSignResult.PASC_SIGN_MSG_PARAM_ERROR);
            }
            finishSignActivity ();
        } else {
            applySignPresenter.applySign (merchantNo, memberNo, channel, sceneId);
        }
    }

    @Override
    protected EventBusObserver registerEventBus() {
        return new EventBusObserver () {
            @Override
            public void handleMessage(BaseEventType eventType) {
                if (eventType instanceof FreeSecretSignEvent) {
                    FreeSecretSignEvent freeSecretSignEvent = (FreeSecretSignEvent) eventType;
                    if (freeSecretSignEvent.signType == PayType.WECHATPAY) {
                        if (freeSecretSignEvent.signResult == PASCSignResult.PASC_SIGN_CODE_SUCCESS) {
                            weChatCallSuccess = false;
                            weChatHasCallBack = false;
                            querySignStatus(true);
                            return;
                        }
                        //else if (freeSecretSignEvent.signResult == PayResult.PAY_CANCEL) {
                        //} else if (freeSecretSignEvent.signResult == PayResult.PAY_FAILED) {
                        //} else if (freeSecretSignEvent.signResult == PayResult.PAY_WAITING) {
                        //}
                        weChatHasCallBack = true;
                    }
                }
            }
        };

    }

    @Override
    public void onBackPressed() {
        if (!isFinishing){
            isFinishing=true;
            if (PayManager.getInstance ().getOnSignListener () != null) {
                PayManager.getInstance ().getOnSignListener ().onSignResult (PASCSignResult.PASC_SIGN_CODE_CANCELED, PASCSignResult.PASC_SIGN_MSG_CANCELED);
            }
        }
        super.onBackPressed ();
    }

    public void closeSignActivity(boolean needFinishActivity) {
        finishSignActivity ();
    }

    public void closeSignActivityDelay() {
        finish ();
        try {
            //淡出动画 更和谐一些
            overridePendingTransition (0, R.anim.ewallet_anim_fade_out_ac);
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public void finishSignActivity() {
        finish ();
        try {
            overridePendingTransition (0, 0);
        } catch (Exception e) {
            e.printStackTrace ();
        }

    }

    @Override public void applySignSuccess(ApplySignResp applySignResp) {
        if (applySignResp != null) {
            //调起微信免密签约
            if("ALIPAY".equals(channel)){
                applySignPresenter.callaliPaySign(this, applySignResp.preEntrustwebId, scheme);
                needQueryResult = true;
            } else {//调起微信免密签约
                if (!WechatPayUtil.isWechatInstalled(getActivity())) {
                    ToastUtils.toastMsg(getString(R.string.ewallet_toast_wechat_uninstall));
                    finishSignActivity();
                    return;
                }
                applySignPresenter.callweChatSign(this, applySignResp.preEntrustwebId);
            }
        }
    }

    @Override public void applySignError(String code, String msg) {
        ToastUtils.toastMsg (msg);
        if (PayManager.getInstance ().getOnSignListener () != null) {
            PayManager.getInstance ().getOnSignListener ().onSignResult (PASCSignResult.PASC_SIGN_CODE_FAILED, PASCSignResult.PASC_SIGN_MSG_FAILED);
        }
        finishSignActivity();
    }

    @Override public void weChatLauncherSuccess(String msg) {
        LogUtil.loge ("weChatCallSuccess->" + msg);
        weChatCallSuccess = true;
    }

    @Override public void weChatLauncherError(String msg) {
        ToastUtils.toastMsg (msg);
        LogUtil.loge ("weChatCallError->" + msg);
        weChatCallSuccess = false;
        if (PayManager.getInstance ().getOnSignListener () != null) {
            PayManager.getInstance ().getOnSignListener ().onSignResult (PASCSignResult.PASC_SIGN_CODE_FAILED, PASCSignResult.PASC_SIGN_MSG_FAILED);
        }
        finishSignActivity ();
    }

    @Override
    public void onResume() {
        super.onResume ();
        if (weChatHasCallBack) {
            // 微信有回调
            weChatHasCallBack = false;
            weChatCallSuccess = false;
        } else if (weChatCallSuccess) {
            //微信没有回调，但是吊起来了。因为微信直接被杀了，直接回来。不知道有没有签约成功
            weChatHasCallBack = false;
            weChatCallSuccess = false;
            querySignStatus(false);
        }
        if(needQueryResult){
            querySignStatus(false);
            needQueryResult = false;
        }
    }

    private void querySignStatus(boolean isThirdPaySuccess) {
        if (Util.ignoreAnim ()){
            rootView.postDelayed (new Runnable () {
                @Override
                public void run() {
                    signStatusPresenter.querySignStatus (memberNo, channel, sceneId, isThirdPaySuccess, true);
                }
            },200);
            return;
        }
        signStatusPresenter.querySignStatus (memberNo, channel, sceneId, isThirdPaySuccess, true);
    }

    @Override public void querySignStatusSuccess(SignStatusResp signStatusResp) {
        if (signStatusResp != null && signStatusResp.hasSign()) {
            //签约成功
            if (PayManager.getInstance ().getOnSignListener () != null) {
                PayManager.getInstance ().getOnSignListener ().onSignResult (PASCSignResult.PASC_SIGN_CODE_SUCCESS, PASCSignResult.PASC_SIGN_MSG_SUCCESS);
            }
            ToastUtils.toastMsg (R.drawable.ewallet_toast_success, "签约成功");
            closeSignActivity (true);
            return;
        }
        closeSignActivityDelay ();
    }

    @Override public void querySignStatusError(String code, String msg) {
        closeSignActivityDelay();
        if (PayManager.getInstance ().getOnSignListener () != null) {
            PayManager.getInstance ().getOnSignListener ().onSignResult (PASCSignResult.PASC_SIGN_CODE_FAILED, PASCSignResult.PASC_SIGN_MSG_FAILED);
        }
    }

    @Override public void querySignStatusTimeOut() {
        ToastUtils.toastMsg (R.string.ewallet_toast_network_error_and_retry);
        closeSignActivityDelay ();
        if (PayManager.getInstance ().getOnSignListener () != null) {
            PayManager.getInstance ().getOnSignListener ().onSignResult (PASCSignResult.PASC_SIGN_CODE_NET_ERROR, PASCSignResult.PASC_SIGN_MSG_NET_ERROR);
        }

    }

    @Override
    public void queryNoSignStatusError() {
        closeSignActivityDelay();
        if (PayManager.getInstance ().getOnSignListener () != null) {
            PayManager.getInstance ().getOnSignListener ().onSignResult (PASCSignResult.PASC_SIGN_CODE_NOSIGN, PASCSignResult.PASC_SIGN_MSG_NOSIGN);
        }
    }
}
