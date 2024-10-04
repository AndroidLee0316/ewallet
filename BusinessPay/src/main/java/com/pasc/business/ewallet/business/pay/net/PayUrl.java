package com.pasc.business.ewallet.business.pay.net;

import com.pasc.business.ewallet.business.common.CommonUrl;

/**
 * @date 2019-11-06
 * @des
 * @modify
 **/
public class PayUrl {
    //获取支付方式列表
    public static String queryPayTypeList(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/queryPayTypeList";
    }

    //支付 /预先下单
    public static String confirmPaymentOrder(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/confirmPaymentOrder";
    }

    // 支付上下文
    public static String getPayContext(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/getPayContext";
    }

    //商户下单
    public static String createPaymentOrder(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/createPaymentOrder";
    }

    //充值下单
    public static String createRechargeOrder(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/createRechargeOrder";
    }

    // 查询订单
    public static String queryOrderDtl(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/queryOrderDtl";
    }

    //支付方式列表（v1.0）
    public static String pay_list(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/pay_list";
    }

    // 申请签约
    public static String applySign(){
        return CommonUrl.getHostAndGateWay() + CommonUrl.URL_PREFIX + "/papay/applySign";
    }

    // 查询签约状态
    public static String querySignStatus(){
        return CommonUrl.getHostAndGateWay() + CommonUrl.URL_PREFIX + "/papay/queryContractStatus";
    }

}
