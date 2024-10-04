package com.pasc.business.ewallet.business.logout.net;

import com.pasc.business.ewallet.business.common.param.MemberNoParam;
import com.pasc.business.ewallet.business.logout.net.param.MemberResetParam;
import com.pasc.business.ewallet.business.logout.net.resp.MemberValidResp;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.BaseV2Resp;
import com.pasc.lib.netpay.resp.VoidObject;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @date 2019-11-06
 * @des
 * @modify
 **/
public interface LogoutApi {
    // 会员注销
    @POST
    Single<BaseV2Resp<VoidObject>> memberCancel(@Url String url, @Body BaseV2Param<MemberNoParam>  param);

    @POST
    Single<BaseV2Resp<VoidObject>> memberReset(@Url String url, @Body BaseV2Param<MemberResetParam>  param);

    //会员重置校验（v2.0）
    @POST
    Single<BaseV2Resp<MemberValidResp>> memberCancelValid(@Url String url, @Body BaseV2Param<MemberNoParam> param);



}
