package com.pasc.business.ewallet.openapi;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.business.pay.wechat.WechatPayUtil;
import com.pasc.business.ewallet.callback.OnEventListener;
import com.pasc.business.ewallet.callback.OnOpenListener;
import com.pasc.business.ewallet.callback.OnPayListener;
import com.pasc.business.ewallet.callback.OnSignListener;
import com.pasc.business.ewallet.config.Constants;

/**
 * 支付对外Api接口类
 *
 * @date 2019/3/1
 * @update 2019/3/15
 */
@NotProguard
public class PASCPay implements Pay {
    private final Pay pay;

    private PASCPay(Pay pay) {
        super();
        this.pay = pay;
    }

    private static class SingletonHolder {
        private static final PASCPay INSTANCE = new PASCPay(new PayManagerProxy());
    }

    public static PASCPay getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(PayBuilder payBuilder){
        pay.init(payBuilder.application,payBuilder.publicKey,payBuilder.AppId,
                payBuilder.secretKey, payBuilder.wxPayAppId,
                payBuilder.isDebug,payBuilder.hostAndGateWay);

    }
    @Override
    public void init(@NonNull Application application,
                     @NonNull String publicKey,
                     @NonNull String AppId,
                     @NonNull String secretKey,
                     @NonNull String wxPayAppId,
                     @NonNull boolean isDebug) {
        pay.init(application,publicKey,AppId, secretKey, wxPayAppId,isDebug);
    }

    @Override
    public void init(@NonNull Application application, @NonNull String publicKey, @NonNull String AppId, @NonNull String secretKey, @NonNull String wxPayAppId, @NonNull boolean isDebug, String hostAndGateWay) {
        pay.init(application,publicKey,AppId, secretKey, wxPayAppId,isDebug,hostAndGateWay);
    }

    @Override
    public void setLBS (@NonNull PayLocation payLocation) {
        pay.setLBS(payLocation);
    }

    @Override
    public void open(@NonNull Context context, @NonNull String merchantNo,
                     String memberNo, OnOpenListener openListener) {
        pay.open(context, merchantNo, memberNo, openListener);
    }

    @Override
    public void sign(@NonNull Context context, @NonNull String memberNo,
        @NonNull String channel, @NonNull String sceneId, OnSignListener listener) {
        pay.sign(context, memberNo, channel, sceneId, listener);
    }

    @Override
    public void sign(@NonNull Context context, @NonNull String memberNo, @NonNull String merchantNo,
        @NonNull String channel, @NonNull String scheme, @NonNull String sceneId, OnSignListener listener) {
        pay.sign(context, memberNo, merchantNo, channel, scheme, sceneId, listener);
    }

    @Override
    public void pay(@NonNull Context context, @NonNull String merchantNo, String memberNo,
                    @NonNull String orderNo, OnPayListener listener) {
        pay.pay(context, merchantNo, memberNo, orderNo, listener);
    }

    @Override
    public void pay(@NonNull Context context, @NonNull String merchantNo, String memberNo, @NonNull String orderNo, String option, OnPayListener listener) {
        pay.pay(context, merchantNo, memberNo, orderNo,option, listener);

    }

    @Override public void recharge(@NonNull Context context, @NonNull String memberNo,
        @NonNull String mchOrderNo, long amount, OnPayListener listener) {
        pay.recharge(context, memberNo, mchOrderNo, amount, listener);
    }

    @Override
    public void detach(Context context) {

    }

    @Override
    public Application getApplication () {
        return pay.getApplication();
    }

    /**
     * 获取
     *
     * @return
     */
    @Override
    public OnPayListener getOnPayListener() {
        return pay.getOnPayListener();
    }

    @Override
    public OnSignListener getOnSignListener() {
        return pay.getOnSignListener();
    }

    @Override
    public OnOpenListener getOnOpenListener() {
        return pay.getOnOpenListener();
    }

    @Override
    public void notifyPayResult (String payType, int payResult, String msg) {
        pay.notifyPayResult(payType, payResult, msg);
    }

    @Override
    public void notifySignResult (int signType, int signResult, String msg) {
        pay.notifySignResult(signType, signResult, msg);
    }

    @Override
    public void setOnEventListener(OnEventListener onEventListener) {
        pay.setOnEventListener (onEventListener);
    }

    @Override
    public OnEventListener getOnEventListener() {
        return pay.getOnEventListener ();
    }

    @Override
    public PayInfo getPayInfo() {
        return pay.getPayInfo ();
    }

    public String getWechatPayAppID() {
        return WechatPayUtil.currentWxAppId ();
    }

    public void setWxCardPuk(String puk){
        Constants.wxCardPuk=puk;
    }

    /**
     * 设置钱包协议地址
     * @param openAccountServiceProtocol 电子钱包服务协议地址
     * @param openAccountUnreceivedCode 开户 收不到验证码
     * @param payBankCardSignServiceProtocol 无卡支付签约协议
     * @param createAccountSupportBankCard 查看支持的银行卡 ，更多 ，换绑卡和二类户
     * @param createAccountSupportSignBankCard 查看支持的支付银行卡
     */
    public void setWalletProtocalUrl(String openAccountServiceProtocol, String openAccountUnreceivedCode,
        String payBankCardSignServiceProtocol, String createAccountSupportBankCard, String createAccountSupportSignBankCard) {
        if (!TextUtils.isEmpty(openAccountServiceProtocol)) {
           Constants.OPENACCOUNT_SERVICE_PROTOCOL = openAccountServiceProtocol;
        }
        if (!TextUtils.isEmpty(openAccountUnreceivedCode)) {
           Constants.OPENACCOUNT_UNRECEIVED_CODE = openAccountUnreceivedCode;
        }
        if (!TextUtils.isEmpty(payBankCardSignServiceProtocol)) {
           Constants.PAY_BANKCARD_SIGN_SERVICE_PROTOCOL = payBankCardSignServiceProtocol;
        }
        if (!TextUtils.isEmpty(createAccountSupportBankCard)) {
           Constants.CREATE_ACCOUNT_SUPPORT_BANK_CARD = createAccountSupportBankCard;
        }
        if (!TextUtils.isEmpty(createAccountSupportSignBankCard)) {
           Constants.CREATE_ACCOUNT_SUPPORT_SIGN_BANK_CARD = createAccountSupportSignBankCard;
        }
    }
}
