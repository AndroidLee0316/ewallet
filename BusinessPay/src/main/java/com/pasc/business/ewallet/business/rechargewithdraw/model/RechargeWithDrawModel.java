package com.pasc.business.ewallet.business.rechargewithdraw.model;

import com.pasc.business.ewallet.business.common.RespTransformer;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.business.rechargewithdraw.net.RechargeWithDrawApi;
import com.pasc.business.ewallet.business.rechargewithdraw.net.RechargeWithDrawUrl;
import com.pasc.business.ewallet.business.rechargewithdraw.net.param.CalcWithdrawFeeParam;
import com.pasc.business.ewallet.business.rechargewithdraw.net.param.PersonWithdrawParam;
import com.pasc.business.ewallet.business.rechargewithdraw.net.param.RechargeParam;
import com.pasc.business.ewallet.business.rechargewithdraw.net.resp.CalcWithdrawFeeResp;
import com.pasc.business.ewallet.business.rechargewithdraw.net.resp.WithdrawResp;
import com.pasc.lib.netpay.ApiGenerator;
import com.pasc.lib.netpay.param.BaseV2Param;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @date 2019-11-18
 * @des
 * @modify
 **/
public class RechargeWithDrawModel {

    /***
     * 充值
     * @return
     */
    public static Single<PayResp> recharge(String memberNo, String payType, long amount, String mchOrderNo) {
        RechargeParam param = new RechargeParam (memberNo, payType, amount, mchOrderNo);
        return ApiGenerator.createApi (RechargeWithDrawApi.class)
                .recharge (RechargeWithDrawUrl.recharge (),new BaseV2Param<> (param))
                .compose (RespTransformer.newInstance ())
                .timeout (30, TimeUnit.SECONDS)
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    public static Single<PayResp> rechargeQuickCard(String memberNo, String payType, long amount,String unionOrderId, String paydate, String cardKey, String unionVerifyCode) {
        String mchOrderNo = String.valueOf(System.currentTimeMillis());
        RechargeParam param = new RechargeParam (memberNo, payType, amount, mchOrderNo);
        param.cardKey=cardKey;
        param.unionOrderId=unionOrderId;
        param.paydate=paydate;
        param.unionVerifyCode=unionVerifyCode;
        return ApiGenerator.createApi (RechargeWithDrawApi.class)
                .recharge (RechargeWithDrawUrl.recharge (),new BaseV2Param<> (param))
                .compose (RespTransformer.newInstance ())
                .timeout (30, TimeUnit.SECONDS)
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    /***
     * 个人提现
     * @return
     */
    public static Single<WithdrawResp> personWithdraw(String memberNo, long withdrawAmt, String password) {
        PersonWithdrawParam param = new PersonWithdrawParam (memberNo, withdrawAmt, password);
        return ApiGenerator.createApi (RechargeWithDrawApi.class)
                .personWithdraw (RechargeWithDrawUrl.personWithdraw (),new BaseV2Param<> (param))
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    /***
     * 提现手续费试算（v1.0）
     * @return
     */
    public static Single<CalcWithdrawFeeResp> calcWithdrawFee(String memberNo, long withdrawAmt) {
        CalcWithdrawFeeParam param = new CalcWithdrawFeeParam (memberNo, withdrawAmt);
        return ApiGenerator.createApi (RechargeWithDrawApi.class)
                .calcWithdrawFee (RechargeWithDrawUrl.calc_withdraw_fee (),new BaseV2Param<> (param))
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }
}
