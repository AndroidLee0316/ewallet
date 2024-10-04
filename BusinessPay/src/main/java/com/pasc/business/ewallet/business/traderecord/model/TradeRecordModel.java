package com.pasc.business.ewallet.business.traderecord.model;

import com.pasc.business.ewallet.business.pay.net.PayApi;
import com.pasc.business.ewallet.business.pay.net.param.QueryOrderParam;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.business.traderecord.net.TradeUrl;
import com.pasc.business.ewallet.business.traderecord.net.param.BillListParamYC;
import com.pasc.business.ewallet.business.traderecord.net.resp.AvailBalanceListResp;
import com.pasc.business.ewallet.business.traderecord.net.resp.BillListResp;
import com.pasc.business.ewallet.business.traderecord.net.resp.PayMonthResp;
import com.pasc.business.ewallet.business.traderecord.net.TradeApi;
import com.pasc.business.ewallet.business.traderecord.net.param.AvailBalanceListParam;
import com.pasc.business.ewallet.business.traderecord.net.param.BillListParam;
import com.pasc.business.ewallet.business.traderecord.net.param.PayMonthParam;
import com.pasc.lib.netpay.ApiGenerator;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.transform.RespV2Transformer;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public class TradeRecordModel {
    public static Single<AvailBalanceListResp> getAvailBalanceList(AvailBalanceListParam param) {
        return ApiGenerator.createApi (TradeApi.class)
                .getAvailBalanceList ("http://www.baidu.com",param)
                .subscribeOn (Schedulers.io ())
                .compose (RespV2Transformer.newInstance ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    public static Single<PayMonthResp> getMonthlyBills(PayMonthParam param) {
        return ApiGenerator.createApi (TradeApi.class)
                .billReportByMonth (TradeUrl.billReportByMonth (),new BaseV2Param<> (param))
                .subscribeOn (Schedulers.io ())
                .compose (RespV2Transformer.newInstance ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    public static Single<BillListResp> getBillList(BillListParam param) {
        return ApiGenerator.createApi (TradeApi.class)
                .getBillList (TradeUrl.getBillList (),new BaseV2Param<> (param))
                .subscribeOn (Schedulers.io ())
                .compose (RespV2Transformer.newInstance ());
    }

    public static Single<BillListResp> getBillListYC(BillListParamYC param) {
        return ApiGenerator.createApi (TradeApi.class)
                .getBillListYC (TradeUrl.getBillListYC (),new BaseV2Param<> (param))
                .subscribeOn (Schedulers.io ())
                .compose (RespV2Transformer.newInstance ());
    }

    public static Single<QueryOrderResp> getBillDetail(QueryOrderParam param) {
        return ApiGenerator.createApi (PayApi.class)
                .queryOrderDtl (TradeUrl.queryOrderDtl (),new BaseV2Param<> (param))
                .subscribeOn (Schedulers.io ())
                .compose (RespV2Transformer.newInstance ())
                .observeOn (AndroidSchedulers.mainThread ());
    }
}
