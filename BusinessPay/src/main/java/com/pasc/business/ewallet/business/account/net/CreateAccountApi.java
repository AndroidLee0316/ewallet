package com.pasc.business.ewallet.business.account.net;

import com.pasc.business.ewallet.business.account.net.param.SendSmsParam;
import com.pasc.business.ewallet.business.account.net.param.ValidCertParam;
import com.pasc.business.ewallet.business.account.net.param.ValidSmsParam;
import com.pasc.business.ewallet.business.account.net.resp.CheckPwdHasSetResp;
import com.pasc.business.ewallet.business.account.net.resp.MemberStatusResp;
import com.pasc.business.ewallet.business.account.net.resp.QueryMemberResp;
import com.pasc.business.ewallet.business.common.param.MemberNoParam;
import com.pasc.business.ewallet.business.pwd.net.resp.ValidateCodeResp;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.BaseV2Resp;
import com.pasc.lib.netpay.resp.VoidObject;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface CreateAccountApi {

    /***********************见证宝start***************************/

    /**
     * 是否设置支付密码
     * @param param
     * @return
     */
    @POST
    Single<BaseV2Resp<CheckPwdHasSetResp>> checkPwdHasSet(@Url String url, @Body BaseV2Param<MemberNoParam> param);

    /****查询会员信息***/
    @POST
    Single<BaseV2Resp<QueryMemberResp>> queryMemberByMemberNo(@Url String url, @Body BaseV2Param<MemberNoParam> param);

    /****身份校验***/
    @POST
    Single<BaseV2Resp<VoidObject>> validCert(@Url String url, @Body BaseV2Param<ValidCertParam> param);

    /****发送短信验证码***/
    @POST
    Single<BaseV2Resp<VoidObject>> sendSms(@Url String url, @Body BaseV2Param<SendSmsParam> param);

    /****校验短信验证码***/
    @POST
    Single<BaseV2Resp<ValidateCodeResp>> validSms(@Url String url, @Body BaseV2Param<ValidSmsParam> param);

    /***注销账户新加***/
    //会员状态查询（v2.0）
    @POST
    Single<BaseV2Resp<MemberStatusResp>> memberStatus(@Url String url, @Body BaseV2Param<MemberNoParam> param);


    /***********************见证宝end***************************/

}
