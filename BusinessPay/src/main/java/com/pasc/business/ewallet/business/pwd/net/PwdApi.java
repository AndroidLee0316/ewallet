package com.pasc.business.ewallet.business.pwd.net;

import com.pasc.business.ewallet.business.common.param.JihuoParam;
import com.pasc.business.ewallet.business.pwd.net.param.SetPwdParam;
import com.pasc.business.ewallet.business.pwd.net.param.ValidPwdParam;
import com.pasc.business.ewallet.business.pwd.net.resp.ValidateCodeResp;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.BaseV2Resp;
import com.pasc.lib.netpay.resp.VoidObject;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @date 2019/7/15
 * @des
 * @modify
 **/
public interface PwdApi {
    /**
     * 设置支付密码
     *
     * @param param
     * @return
     */
    @POST
    Single<BaseV2Resp<VoidObject>> setPassWord(@Url String url,@Body BaseV2Param<SetPwdParam> param);

    /****校验支付密码***/
    @POST
    Single<BaseV2Resp<ValidateCodeResp>> validPwd(@Url String url,@Body BaseV2Param<ValidPwdParam> param);

    /****校验支付密码***/
    @POST
    Single<BaseV2Resp<VoidObject>> addCert(@Url String url, @Body BaseV2Param<JihuoParam> param);

}
