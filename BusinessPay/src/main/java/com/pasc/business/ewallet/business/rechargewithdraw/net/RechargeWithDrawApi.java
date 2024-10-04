package com.pasc.business.ewallet.business.rechargewithdraw.net;

import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.business.rechargewithdraw.net.param.CalcWithdrawFeeParam;
import com.pasc.business.ewallet.business.rechargewithdraw.net.param.PersonWithdrawParam;
import com.pasc.business.ewallet.business.rechargewithdraw.net.param.RechargeParam;
import com.pasc.business.ewallet.business.rechargewithdraw.net.resp.CalcWithdrawFeeResp;
import com.pasc.business.ewallet.business.rechargewithdraw.net.resp.WithdrawResp;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.BaseV2Resp;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @date 2019-11-18
 * @des
 * @modify
 **/
public interface RechargeWithDrawApi {
    /****充值***/
    @POST
    Single<BaseV2Resp<PayResp>> recharge(@Url String url, @Body BaseV2Param<RechargeParam> param);

    /****提现***/
    @POST
    Single<BaseV2Resp<WithdrawResp>> personWithdraw(@Url String url, @Body BaseV2Param<PersonWithdrawParam> param);

    /****提现手续费试算（v1.0）***/
    @POST
    Single<BaseV2Resp<CalcWithdrawFeeResp>> calcWithdrawFee(@Url String url, @Body BaseV2Param<CalcWithdrawFeeParam> param);

}
