package com.pasc.business.ewallet.business.bankcard.net;

import com.pasc.business.ewallet.business.bankcard.net.param.BindCardParam;
import com.pasc.business.ewallet.business.bankcard.net.param.QuickPaySendMsgParam;
import com.pasc.business.ewallet.business.bankcard.net.param.UnBindQuickCardParam;
import com.pasc.business.ewallet.business.bankcard.net.param.ValidBindCardParam;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickCardBean;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickCardResp;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickPaySendMsgResp;
import com.pasc.business.ewallet.business.bankcard.net.resp.SafeCardBean;
import com.pasc.business.ewallet.business.common.param.MemberNoParam;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.BaseV2Resp;
import com.pasc.lib.netpay.resp.VoidObject;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @date 2019/7/25
 * @des
 * @modify
 **/
public interface CardApi {

    @POST
    Single<BaseV2Resp<List<SafeCardBean>>> listSafeBank(@Url String url, @Body BaseV2Param<MemberNoParam> param);

    @POST
    Single<BaseV2Resp<List<QuickCardBean>>> listQuickBank(@Url String url,@Body BaseV2Param<MemberNoParam> param);

    @POST
    Single<BaseV2Resp<VoidObject>> bindCard(@Url String url,@Body BaseV2Param<BindCardParam> param);

    @POST
    Single<BaseV2Resp<QuickCardResp>> bindCardValid(@Url String url,@Body BaseV2Param<ValidBindCardParam> param);

    @POST
    Single<BaseV2Resp<QuickPaySendMsgResp>> quickPaySendMsg(@Url String url,@Body BaseV2Param<QuickPaySendMsgParam> param);

    @POST
    Single<BaseV2Resp<String>> jumpBindCardPage(@Url String url,@Body BaseV2Param<MemberNoParam> param);

    @POST
    Single<BaseV2Resp<VoidObject>> unbindCard(@Url String url,@Body BaseV2Param<UnBindQuickCardParam> param);

}
