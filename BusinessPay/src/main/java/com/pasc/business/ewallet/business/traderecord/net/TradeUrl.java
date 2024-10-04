package com.pasc.business.ewallet.business.traderecord.net;

import com.pasc.business.ewallet.business.common.CommonUrl;
import com.pasc.business.ewallet.business.pay.net.PayUrl;

/**
 * @date 2019-11-06
 * @des
 * @modify
 **/
public class TradeUrl {

    //月度账单
    public static String billReportByMonth(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/billReportByMonth";
    }

    // 订单列表
    public static String getBillList(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/bill_list";
    }
    //月度账单
    public static String getBillListYC(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/billListForSaltCity";
    }

    //查询订单
    public static String queryOrderDtl(){
//        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/queryOrderDtl";
        return PayUrl.queryOrderDtl ();
    }
}
