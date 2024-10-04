package com.pasc.business.ewallet.business.pay.net.resp;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.NotProguard;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
@NotProguard
public class CreatePayOrderResp {
    @SerializedName("orderNo")
    public  String orderNo; //订单编号
}
