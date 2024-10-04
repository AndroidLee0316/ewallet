package com.pasc.business.ewallet.business.traderecord.net;

import com.pasc.business.ewallet.business.traderecord.net.param.AvailBalanceListParam;
import com.pasc.business.ewallet.business.traderecord.net.param.BillListParam;
import com.pasc.business.ewallet.business.traderecord.net.param.BillListParamYC;
import com.pasc.business.ewallet.business.traderecord.net.param.PayMonthParam;
import com.pasc.business.ewallet.business.traderecord.net.resp.AvailBalanceListResp;
import com.pasc.business.ewallet.business.traderecord.net.resp.BillListResp;
import com.pasc.business.ewallet.business.traderecord.net.resp.BillTypeResp;
import com.pasc.business.ewallet.business.traderecord.net.resp.PayMonthResp;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.BaseV2Resp;
import com.pasc.lib.netpay.resp.VoidObject;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public interface TradeApi {
    /**
     * 余额明细
     **/
    @POST
    Single<BaseV2Resp<AvailBalanceListResp>> getAvailBalanceList(@Url String url, @Body AvailBalanceListParam param);
    /**
     * 获取账单类型列表
     **/
    @POST
    Single<BaseV2Resp<BillTypeResp>> getPayBillTypeList(@Url String url,@Body VoidObject param);


    @POST
    Single<BaseV2Resp<PayMonthResp>> billReportByMonth(@Url String url,@Body BaseV2Param<PayMonthParam> param);


    @POST
    Single<BaseV2Resp<BillListResp>> getBillList(@Url String url,@Body BaseV2Param<BillListParam> param);

    @POST
    Single<BaseV2Resp<BillListResp>> getBillListYC(@Url String url,@Body BaseV2Param<BillListParamYC> param);

}
