package com.pasc.business.ewallet.inner;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.pasc.business.ewallet.ActivityManager;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.account.common.CheckAccountListener;
import com.pasc.business.ewallet.business.account.common.CheckAccountStatusService;
import com.pasc.business.ewallet.business.account.model.AccountModel;
import com.pasc.business.ewallet.business.account.net.resp.CheckPwdHasSetResp;
import com.pasc.business.ewallet.business.account.net.resp.MemberStatusResp;
import com.pasc.business.ewallet.business.account.net.resp.QueryMemberResp;
import com.pasc.business.ewallet.business.common.CommonUrl;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.business.pay.wechat.WechatPayUtil;
import com.pasc.business.ewallet.callback.OnActivityLifecycleCallback;
import com.pasc.business.ewallet.callback.OnEventListener;
import com.pasc.business.ewallet.callback.OnOpenListener;
import com.pasc.business.ewallet.callback.OnPayListener;
import com.pasc.business.ewallet.callback.OnPayTypeClickListener;
import com.pasc.business.ewallet.callback.OnSignListener;
import com.pasc.business.ewallet.common.utils.ConvertUtil;
import com.pasc.business.ewallet.common.utils.HeaderUtil;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.config.Constants;
import com.pasc.business.ewallet.result.PASCPayResult;
import com.pasc.lib.netpay.HttpCommonParams;
import com.pasc.lib.netpay.NetConfig;
import com.pasc.lib.netpay.NetManager;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.pay.common.AppProxy;
import com.pasc.lib.pay.common.util.AppUtils;
import com.pasc.lib.pay.common.util.ToastUtils;
import com.pasc.lib.pay.common.util.WatchDogKiller;
import com.pasc.lib.pay.common.util.XposeUtil;
import com.pasc.lib.pay.statistics.StatisticsManager;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.Map;

/**
 * @date 2019-11-27
 * @des
 * @modify
 **/
public class PayManager {
    private Application mApplication;
    /**
     * 支付结果回调监听
     */
    private OnPayListener mOnPayListener;

    /**
     * 签约结果回调监听
     */
    private OnSignListener mOnSignListener;
    /**
     * 打开钱包首页结果回调监听
     */
    private OnOpenListener mOnOpenListener;

    private OnEventListener onEventListener;
    private String currentOrderNo;

    /**
     * 支付类型选择回调监听
     */
    private OnPayTypeClickListener mOnPayTypeClickListener;

    private OnActivityLifecycleCallback mOnActivityLifecycleCallback;

    private PayManager() {
    }
    private Disposable disposable;
    private static class SingletonHolder {
        private static final PayManager MANAGER = new PayManager ();
    }

    public static PayManager getInstance() {
        return SingletonHolder.MANAGER;
    }
    public Application getApplication(){
        return mApplication;
    }
    public void init( Application application,  String publicKey,
                      String AppId,  String secretKey,
                      String wxPayAppId,  boolean isDebug,
                     String hostAndGateWay) {
        this.mApplication=application;
        if (AppUtils.getPIDName (application).equals (application.getPackageName ())) {
            AppProxy.getInstance ().init (application);
            XposeUtil.checkHookTip ();
            WatchDogKiller.stopWatchDog ();
            Constants.SECRET_KEY = secretKey;
            Constants.APP_ID = AppId;
            Constants.PUBLIC_KEY = publicKey;
            Constants.IS_DEBUG = isDebug;
            LogUtil.isDebug = isDebug;
            if (TextUtils.isEmpty (hostAndGateWay)) {
                CommonUrl.HOST_URL = CommonUrl.RELEASE_ENV_HOST_URL;
            } else {
                if (!hostAndGateWay.endsWith ("/")) {
                    hostAndGateWay = hostAndGateWay + "/";
                }
                CommonUrl.HOST_URL = hostAndGateWay;
            }
            initNet (application, Constants.IS_DEBUG, CommonUrl.HOST_URL);
            WechatPayUtil.initWxAppId (wxPayAppId);
            WechatPayUtil.registerWxAppId (application, wxPayAppId);
            ActivityManager.getInstance ().init (application);
            boolean isTest = false;
            String eventAppId = isTest ? Constants.TEND_DATA_APP_ID_TEST : Constants.TEND_DATA_APP_ID;
            StatisticsManager.getInstance ().initStatistics (application, "", eventAppId, isTest);
        }
    }
    private void initNet(Context context,boolean isNetDebug, String host) {
        NetConfig config = new NetConfig.Builder (context)
                .baseUrl (host)
                .headers (HeaderUtil.getHeaders (context,isNetDebug))
                .gson (ConvertUtil.getConvertGson ())
                .isDebug (isNetDebug) // 是否打印NetLog
                .build ();

        NetManager.init (config);

        HttpCommonParams.getInstance ().setInjectHandler (new HttpCommonParams.InjectCommonHeadersHandler () {
            @Override
            public void onInjectCommonHeaders(Map<String, String> headers) {

            }
        });
    }

    private void updateOrderNo(String orderNo){
        this.currentOrderNo=orderNo;
    }
    public String getCurrentOrderNo(){
        return currentOrderNo;
    }

    public void open(Context context, String merchantNo, String memberNo, final  OnOpenListener openListener){
        this.mOnOpenListener=openListener;
        if (openListener != null) {
            openListener.onStart ();
        }
        UserManager.getInstance ().setMemberNo (memberNo);
        UserManager.getInstance ().setMerchantNo (merchantNo);
        CheckAccountStatusService.checkMemberStatus (memberNo, new CheckAccountListener () {
            @Override
            public void onSuccess(CheckPwdHasSetResp checkPwdHasSetResp) {
                if (checkPwdHasSetResp.hasSetPwd ()) {
                    if (openListener != null) {
                        openListener.onOpenResult (PASCPayResult.PASC_PAY_CODE_SUCCESS, "打开钱包首页成功");
                    }
                    RouterManager.gotoHome (context);
                } else {
                    RouterManager.AccountRouter.gotoCreateAccount (context, memberNo);
                }

                if (openListener != null) {
                    openListener.onEnd ();
                }
            }

            @Override
            public void onSuccess(MemberStatusResp memberStatusResp) {

                if (memberStatusResp.isOpen ()) {
                    if (openListener != null) {
                        openListener.onOpenResult (PASCPayResult.PASC_PAY_CODE_SUCCESS, "打开钱包首页成功");
                    }
                    RouterManager.gotoHome (context);
                } else if (memberStatusResp.isCancle ()){
                    ToastUtils.toastMsg ("钱包账户已经被注销");

                } else {
                    RouterManager.AccountRouter.gotoCreateAccount (context, memberNo);
                }
                if (openListener != null) {
                    openListener.onEnd ();
                }
            }

            @Override
            public void onFail(String code, String msg) {
                ToastUtils.toastMsg (msg);
                if (openListener != null) {
                    openListener.onOpenResult (PASCPayResult.PASC_PAY_CODE_NET_ERROR, msg);
                }
                if (openListener != null) {
                    openListener.onEnd ();
                }
            }
        });
        queryMemInfo (memberNo);
    }

    public void sign( Context context, String memberNo,  String channel, String sceneId, OnSignListener listener){
        this.mOnSignListener = listener;
        RouterManager.PayRouter.gotoSign(context, memberNo, channel, sceneId);
    }

    public void sign( Context context, String memberNo, String merchantNo, String channel, String scheme, String sceneId, OnSignListener listener){
        this.mOnSignListener = listener;
        RouterManager.PayRouter.gotoSign(context, memberNo, merchantNo, channel, scheme, sceneId);
    }

    public void pay( Context context,  String merchantNo, String memberNo,  String orderNo,String option,OnPayListener listener){
        this.mOnPayListener=listener;
        updateOrderNo (orderNo);
        UserManager.getInstance ().setMemberNo (memberNo);
        UserManager.getInstance ().setMerchantNo (merchantNo);
        RouterManager.PayRouter.gotoPay (context, memberNo, merchantNo, orderNo,option);
        //queryMemInfo (memberNo);
    }

    /**
     * 充值
     * @param context
     * @param memberNo 会员号
     * @param mchOrderNo 商户订单号
     * @param amount 充值金额，单位为分
     */
    public void recharge( Context context, String memberNo, String mchOrderNo, long amount, OnPayListener listener){
        this.mOnPayListener = listener;
        UserManager.getInstance ().setMemberNo (memberNo);
        RouterManager.PayRouter.gotoRechargePay (context, memberNo, mchOrderNo, amount);
    }

    void queryMemInfo(String memberNo){
        dispose (disposable);
        disposable = AccountModel.queryMemberByMemberNo (memberNo)
                .subscribe (new Consumer<QueryMemberResp> () {
                    @Override
                    public void accept(QueryMemberResp data) {
                        disposable = null;

                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        disposable = null;
                    }
                });
    }

    void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed ()) {
            try {
                disposable.dispose ();
            }catch (Exception e){
                e.printStackTrace ();
            }
        }

    }

    public void setOnEventListener(OnEventListener onEventListener) {
        this.onEventListener=onEventListener;
    }

    public OnEventListener getOnEventListener() {
        return onEventListener;
    }

    public OnPayListener getOnPayListener() {
        return mOnPayListener;
    }

    public OnSignListener getOnSignListener() {
        return mOnSignListener;
    }

    public OnOpenListener getOnOpenListener() {
        return mOnOpenListener;
    }

    public void setOnPayTypeClickListener(OnPayTypeClickListener onPayTypeClickListener) {
        mOnPayTypeClickListener = onPayTypeClickListener;
    }

    public OnPayTypeClickListener getOnPayTypeClickListener() {
        return mOnPayTypeClickListener;
    }

    public void setOnActivityLifecycleCallback(OnActivityLifecycleCallback onActivityLifecycleCallback) {
        mOnActivityLifecycleCallback = onActivityLifecycleCallback;
    }

    public OnActivityLifecycleCallback getOnActivityLifecycleCallback() {
        return mOnActivityLifecycleCallback;
    }
}

