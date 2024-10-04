package com.pasc.business.ewallet.business.pay.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
public class QueryOrderParam {
    @SerializedName("mchOrderNo")
    public String mchOrderNo; //第三方订单号

    @SerializedName("orderNo")
    public String orderNo; //钱充值需要


    @SerializedName("tradeType")
    public String tradeType; //交易类型

    public QueryOrderParam(String mchOrderNo, String orderNo,String tradeType) {
        this.mchOrderNo = mchOrderNo;
        this.orderNo = orderNo;
        this.tradeType=tradeType;
    }
}
