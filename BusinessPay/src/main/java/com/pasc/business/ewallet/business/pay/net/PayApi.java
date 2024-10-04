package com.pasc.business.ewallet.business.pay.net;

import com.pasc.business.ewallet.business.common.param.PaySceneParam;
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
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.BaseV2Resp;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface PayApi {

    /***见证宝 获取支付方式列表****/
    @POST
    Single<BaseV2Resp<PayTypeResp>> queryPayTypeList(@Url String url, @Body BaseV2Param<PayTypeParam> param);

    /***见证包  确认支付********/
    @POST
    Single<BaseV2Resp<PayResp>> pay(@Url String url,@Body BaseV2Param<PayParam> param);

    @POST
    Single<BaseV2Resp<PayContextResp>> getPayContext(@Url String url,@Body BaseV2Param<PayContextParam> param);

    /***见证包  确认支付********/
    @POST
    Single<BaseV2Resp<QueryOrderResp>> queryOrderDtl(@Url String url,@Body BaseV2Param<QueryOrderParam> param);

    //商户下单
    @POST
    Single<BaseV2Resp<CreatePayOrderResp>> createPaymentOrder(@Url String url,@Body BaseV2Param<CreatePayOrderParam> param);

    //充值下单
    @POST
    Single<BaseV2Resp<CreateRechargeOrderResp>> createRechargeOrder(@Url String url,@Body BaseV2Param<CreateRechargeOrderParam> param);

    /****校验支付密码***/
    @POST
    Single<BaseV2Resp<PayContextResp>> payList(@Url String url,@Body BaseV2Param<PaySceneParam> param);

    /****申请签约***/
    @POST
    Single<BaseV2Resp<ApplySignResp>> applySign(@Url String url,@Body BaseV2Param<ApplySignParam> param);

    /****查询签约状态***/
    @POST
    Single<BaseV2Resp<SignStatusResp>> querySignStatus(@Url String url,@Body BaseV2Param<SignStatusParam> param);
}
