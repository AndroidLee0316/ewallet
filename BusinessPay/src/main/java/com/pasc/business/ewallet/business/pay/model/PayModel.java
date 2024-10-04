package com.pasc.business.ewallet.business.pay.model;

import android.text.TextUtils;
import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.business.common.RespTransformer;
import com.pasc.business.ewallet.business.common.param.PaySceneParam;
import com.pasc.business.ewallet.business.pay.net.PayApi;
import com.pasc.business.ewallet.business.pay.net.PayUrl;
import com.pasc.business.ewallet.business.pay.net.param.ApplySignParam;
import com.pasc.business.ewallet.business.pay.net.param.CreatePayOrderParam;
import com.pasc.business.ewallet.business.pay.net.param.CreateRechargeOrderParam;
import com.pasc.business.ewallet.business.pay.net.param.PayContextParam;
import com.pasc.business.ewallet.business.pay.net.param.PayParam;
import com.pasc.business.ewallet.business.pay.net.param.PayTypeParam;
import com.pasc.business.ewallet.business.pay.net.param.QueryOrderParam;
import com.pasc.business.ewallet.business.pay.net.param.SignStatusParam;
import com.pasc.business.ewallet.business.pay.net.resp.ApplySignResp;
import com.pasc.business.ewallet.business.pay.net.resp.CreatePayOrderResp;
import com.pasc.business.ewallet.business.pay.net.resp.CreateRechargeOrderResp;
import com.pasc.business.ewallet.business.pay.net.resp.PayContextResp;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.business.pay.net.resp.PayTypeResp;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.business.pay.net.resp.SignStatusResp;
import com.pasc.lib.netpay.ApiGenerator;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.BaseV2Resp;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/7/25
 * @des
 * @modify
 **/
@NotProguard
public class PayModel {



    /**
     * @param memberNo
     * @return
     */
    public static Single<PayTypeResp> queryPayTypeList(String memberNo) {
        PayTypeParam payTypeParam = new PayTypeParam (memberNo);
        return ApiGenerator.createApi (PayApi.class)
                .queryPayTypeList (PayUrl.queryPayTypeList (),new BaseV2Param<> (payTypeParam))
                .timeout (30, TimeUnit.SECONDS)
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    public static Single<PayResp> pay(String mchOrderNo, String memberNo, String orderType, String payPwd) {
        PayParam payParam = new PayParam (mchOrderNo, memberNo, orderType, payPwd);
        return ApiGenerator.createApi (PayApi.class)
                .pay (PayUrl.confirmPaymentOrder (),new BaseV2Param<> (payParam))
                .timeout (30, TimeUnit.SECONDS)
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    /***
     * 银联支付
     * @param mchOrderNo
     * @param memberNo
     * @param orderType
     * @param unionOrderId
     * @param paydate
     * @param cardKey
     * @param unionVerifyCode
     * @return
     */
    public static Single<PayResp> payQuickCard(String mchOrderNo, String memberNo, String orderType,
                                      String unionOrderId,String paydate,String cardKey,String unionVerifyCode) {
        PayParam payParam = new PayParam (mchOrderNo, memberNo, orderType, "");
        payParam.unionOrderId=unionOrderId;
        payParam.paydate=paydate;
        payParam.cardKey=cardKey;
        payParam.unionVerifyCode=unionVerifyCode;

        return ApiGenerator.createApi (PayApi.class)
                .pay (PayUrl.confirmPaymentOrder (),new BaseV2Param<> (payParam))
                .timeout (30, TimeUnit.SECONDS)
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }


    public static Single<PayContextResp> getPayContext(String merchantNo, String memberNo, String mchOrderNo, String payScene) {
        PayContextParam payTypeParamParam = new PayContextParam (merchantNo, memberNo, mchOrderNo, payScene);
        return ApiGenerator.createApi (PayApi.class)
                .getPayContext (PayUrl.getPayContext (),new BaseV2Param<> (payTypeParamParam))
                .timeout (30, TimeUnit.SECONDS)
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    /**
     * 支付下单
     * @param merchantNo
     * @param mchOrderNo
     * @param memberNo
     * @param amount
     * @param sceneId
     * @return
     */
    public static Single<CreatePayOrderResp> createPaymentOrder(String merchantNo, String mchOrderNo,
        String memberNo, long amount, String appId, String sceneId) {
        CreatePayOrderParam payOrderParam = new CreatePayOrderParam ();
        payOrderParam.merchantNo = merchantNo;
        payOrderParam.memberNo = memberNo;
        payOrderParam.mchOrderNo = mchOrderNo;
        payOrderParam.amount=amount;
        payOrderParam.appId= appId;
        if (!TextUtils.isEmpty(sceneId)) {
            payOrderParam.sceneId = sceneId;
        }
        return ApiGenerator.createApi (PayApi.class)
                .createPaymentOrder (PayUrl.createPaymentOrder (),new BaseV2Param<> (payOrderParam))
                .timeout (30, TimeUnit.SECONDS)
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    /**
     * 充值下单
     * @param mchOrderNo
     * @param memberNo
     * @param amount
     * @return
     */
    public static Single<CreateRechargeOrderResp> createRechargeOrder(String mchOrderNo,
        String memberNo, long amount) {
        CreateRechargeOrderParam rechargeOrderParam = new CreateRechargeOrderParam ();
        rechargeOrderParam.mchOrderNo = mchOrderNo;
        rechargeOrderParam.memberNo = memberNo;
        rechargeOrderParam.amount = amount;
        return ApiGenerator.createApi (PayApi.class)
            .createRechargeOrder (PayUrl.createRechargeOrder (),new BaseV2Param<> (rechargeOrderParam))
            .timeout (30, TimeUnit.SECONDS)
            .compose (RespTransformer.newInstance ())
            .subscribeOn (Schedulers.io ())
            .observeOn (AndroidSchedulers.mainThread ());
    }

    public static Single<BaseV2Resp<QueryOrderResp>> queryOrderDtl(String mchOrderNo, String orderNo,String tradeType, long timeOutMillSeconds) {
        QueryOrderParam param = new QueryOrderParam (mchOrderNo, orderNo,tradeType);
        return ApiGenerator.createApi (PayApi.class)
                .queryOrderDtl (PayUrl.queryOrderDtl (),new BaseV2Param<> (param))
                .timeout (timeOutMillSeconds, TimeUnit.MILLISECONDS);

    }

    public static Single<PayContextResp> payList(String scene) {
        PaySceneParam sceneParam = new PaySceneParam ();
        sceneParam.scene = scene;
        return ApiGenerator.createApi (PayApi.class)
                .payList (PayUrl.pay_list (),new BaseV2Param<> (sceneParam))
                .timeout (30, TimeUnit.SECONDS)
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    public static Single<ApplySignResp> applySign(String memberNo, String channel, String sceneId) {
        return applySign("",memberNo,channel,sceneId);
    }
    
    public static Single<ApplySignResp> applySign(String merchantNo, String memberNo, String channel, String sceneId) {
        ApplySignParam applySignParam;
        if (TextUtils.isEmpty(merchantNo)) {
            applySignParam = new ApplySignParam(memberNo, channel, sceneId);
        } else {
            applySignParam = new ApplySignParam(merchantNo, memberNo, channel, sceneId);
        }
        return ApiGenerator.createApi (PayApi.class)
            .applySign (PayUrl.applySign(), new BaseV2Param<>(applySignParam))
            .timeout (30, TimeUnit.SECONDS)
            .compose (RespTransformer.newInstance ())
            .subscribeOn (Schedulers.io ())
            .observeOn (AndroidSchedulers.mainThread ());
    }

    public static Single<SignStatusResp> querySignStatus(String memberNo, String channel, String sceneId, long timeOutMillSeconds) {
        SignStatusParam param = new SignStatusParam(memberNo, channel, sceneId);
        return ApiGenerator.createApi (PayApi.class)
            .querySignStatus (PayUrl.querySignStatus(), new BaseV2Param<>(param))
            .timeout (timeOutMillSeconds, TimeUnit.SECONDS)
            .compose (RespTransformer.newInstance ())
            .subscribeOn (Schedulers.io ())
            .observeOn (AndroidSchedulers.mainThread ());
    }

}
