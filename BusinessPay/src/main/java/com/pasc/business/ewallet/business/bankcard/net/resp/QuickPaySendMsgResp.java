package com.pasc.business.ewallet.business.bankcard.net.resp;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.NotProguard;

/**
 * @date 2019/8/20
 * @des
 * @modify
 **/
@NotProguard
public class QuickPaySendMsgResp {
    @SerializedName("unionOrderId")
    public String unionOrderId; //银联订单号，调用确认支付时需要
    @SerializedName("paydate")
    public String paydate; //银联下单时间，调用“充值”、“确认支付”时需要
}
