package com.pasc.business.ewallet.business.pay.net.resp;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.NotProguard;

/**
 * Created by zhuangjiguang on 2021/4/21.
 */
@NotProguard
public class CreateRechargeOrderResp {
    @SerializedName("orderNo")
    public  String orderNo; //订单编号
}
