package com.pasc.business.ewallet.business.home.model;

import com.pasc.business.ewallet.business.common.RespTransformer;
import com.pasc.business.ewallet.business.home.net.HomeApi;
import com.pasc.business.ewallet.business.home.net.HomeUrl;
import com.pasc.business.ewallet.business.home.net.QueryBalanceParam;
import com.pasc.business.ewallet.business.home.net.QueryBalanceResp;
import com.pasc.lib.netpay.ApiGenerator;
import com.pasc.lib.netpay.param.BaseV2Param;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class HomeModel {

    public static Single<QueryBalanceResp> queryBalance(String memberNo, String memberType) {
        QueryBalanceParam param = new QueryBalanceParam ();
        param.memberNo = memberNo;
        param.memberType = memberType;
        return ApiGenerator.createApi (HomeApi.class).queryBalance (HomeUrl.query_balance (),new BaseV2Param<> (param))
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                ;
    }
}
