package com.pasc.business.ewallet.openapi;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.pasc.business.ewallet.callback.OnEventListener;
import com.pasc.business.ewallet.callback.OnOpenListener;
import com.pasc.business.ewallet.callback.OnPayListener;
import com.pasc.business.ewallet.callback.OnSignListener;

/**
 * Created by ex-huangzhiyi001 on 2019/3/26.
 */
public interface Pay {

    /**
     * 初始化
     * @param application
     * @param secretKey
     * @param wxPayAppId
     */
    void init(@NonNull Application application,
              @NonNull String publicKey,
              @NonNull String AppId,
              @NonNull String secretKey,
              @NonNull String wxPayAppId,
              @NonNull boolean isDebug);
    void init(@NonNull Application application,
              @NonNull String publicKey,
              @NonNull String AppId,
              @NonNull String secretKey,
              @NonNull String wxPayAppId,
              @NonNull boolean isDebug,String hostAndGateWay);
    /**
     * 添加位置
     * @param payLocation
     */
    void setLBS (@NonNull PayLocation payLocation);

    /**
     * 待优化
     * 对外的提供进入Api
     * @param context
     * @param merchantNo
     * @param memberNo
     */
    void open(@NonNull Context context,  String merchantNo,
              @NonNull String memberNo,@NonNull OnOpenListener openListener);

    /**
     * 免密签约
     * @param context
     * @param memberNo
     * @param channel
     * @param sceneId
     * @param listener
     */
    void sign(@NonNull Context context, @NonNull String memberNo,
        @NonNull String channel, @NonNull String sceneId, OnSignListener listener);

    void sign(@NonNull Context context, @NonNull String memberNo, @NonNull String merchantNo,
        @NonNull String channel, @NonNull String scheme, @NonNull String sceneId, OnSignListener listener);

    /**
     *
     * @param context
     * @param merchantNo
     * @param memberNo
     * @param listener
     */
    void pay(@NonNull Context context, @NonNull String merchantNo, String memberNo,
             @NonNull String orderNo, OnPayListener listener);

    /***
     *
     * @param context
     * @param merchantNo
     * @param memberNo
     * @param orderNo
     * @param option 支付选项 {@link com.pasc.business.ewallet.business.StatusTable.PayOption}
     * @param listener
     */
    void pay(@NonNull Context context, @NonNull String merchantNo, String memberNo,
             @NonNull String orderNo, String option,OnPayListener listener);

    /**
     * 充值
     * @param context
     * @param memberNo 会员号
     * @param mchOrderNo 商户订单号
     * @param amount 充值金额，单位为分
     */
    void recharge(@NonNull Context context, @NonNull String memberNo, @NonNull String mchOrderNo,
        long amount, OnPayListener listener);

    /**
     * detach方法
     * @param context
     *
     */
    void detach(Context context);

    Application getApplication();

    OnPayListener getOnPayListener();

    OnSignListener getOnSignListener();

    OnOpenListener getOnOpenListener();

    PayInfo getPayInfo();

    /**
     * 第三方支付回调支付结果
     *
     * @param payType
     * @param payResult
     */
    void notifyPayResult(String payType, int payResult, String msg);

    /**
     * 签约回调结果
     *
     * @param signType
     * @param signResult
     */
    void notifySignResult(int signType, int signResult, String msg);

    void setOnEventListener(OnEventListener onEventListener);

    OnEventListener getOnEventListener();

}
