package com.pasc.business.ewallet.business.home.net;

import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.BaseV2Resp;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface HomeApi {
    /****查询会员钱包余额***/
    @POST
    Single<BaseV2Resp<QueryBalanceResp>> queryBalance(@Url String url, @Body BaseV2Param<QueryBalanceParam> param);

}
