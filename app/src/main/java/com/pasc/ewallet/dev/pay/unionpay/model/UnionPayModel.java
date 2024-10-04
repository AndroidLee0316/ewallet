package com.pasc.ewallet.dev.pay.unionpay.model;

import com.pasc.business.ewallet.business.common.RespTransformer;
import com.pasc.business.ewallet.business.pay.net.PayApi;
import com.pasc.business.ewallet.business.pay.net.PayUrl;
import com.pasc.business.ewallet.business.pay.net.param.PayParam;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.ewallet.dev.pay.unionpay.model.param.BankCardListParam;
import com.pasc.ewallet.dev.pay.unionpay.model.param.BankCardOpenStatusParam;
import com.pasc.ewallet.dev.pay.unionpay.model.param.SendSmsParam;
import com.pasc.ewallet.dev.pay.unionpay.net.UnionPayApi;
import com.pasc.ewallet.dev.pay.unionpay.net.UnionPayUrl;
import com.pasc.lib.netpay.ApiGenerator;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.VoidObject;
import com.pasc.lib.netpay.transform.RespV2Transformer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhuangjiguang on 2021/3/9.
 */
public class UnionPayModel {
  public static Single<List<BankCard>> queryBankCardList(String memberNo) {
    return ApiGenerator.createApi(UnionPayApi.class)
        .queryBankCardList(UnionPayUrl.bankCardList(), new BaseV2Param<>(new BankCardListParam(memberNo)))
        .compose(RespV2Transformer.newInstance())
        .subscribeOn(Schedulers.io());
  }

  public static Single<BankCardOpenStatus> checkOpenStatus(String memberNo, String mchOrderNo,
      String payType, String cardNo) {
    return ApiGenerator.createApi(UnionPayApi.class)
        .checkOpenStatus(UnionPayUrl.checkOpenStatus(), new BaseV2Param<>(
            new BankCardOpenStatusParam(memberNo, mchOrderNo, payType, cardNo)))
        .compose(RespV2Transformer.newInstance())
        .subscribeOn(Schedulers.io());
  }

  public static Single<VoidObject> sendSmsVerifyCode(SendSmsParam sendSmsParam) {
    return ApiGenerator.createApi(UnionPayApi.class)
        .sendSmsVerifyCode(UnionPayUrl.sendSmsVerifyCode(),
            new BaseV2Param<>(sendSmsParam))
        .compose(RespV2Transformer.newInstance())
        .subscribeOn(Schedulers.io());
  }

  /**
   * 银联无跳转支付
   * @param mchOrderNo
   * @param memberNo
   * @param orderType
   * @param unionOrderId
   * @param paydate
   * @param bankCardNo
   * @param unionVerifyCode
   * @return
   */
  public static Single<PayResp> payUnionCard(String mchOrderNo, String memberNo, String orderType,
      String unionOrderId, String paydate, String bankCardNo, String unionVerifyCode) {
    PayParam payParam = new PayParam (mchOrderNo, memberNo, orderType, "");
    payParam.unionOrderId=unionOrderId;
    payParam.paydate=paydate;
    payParam.bankCardNo=bankCardNo;
    payParam.unionVerifyCode=unionVerifyCode;

    return ApiGenerator.createApi (PayApi.class)
        .pay (PayUrl.confirmPaymentOrder (),new BaseV2Param<> (payParam))
        .timeout (30, TimeUnit.SECONDS)
        .compose (RespTransformer.newInstance ())
        .subscribeOn (Schedulers.io ())
        .observeOn (AndroidSchedulers.mainThread ());
  }

}
