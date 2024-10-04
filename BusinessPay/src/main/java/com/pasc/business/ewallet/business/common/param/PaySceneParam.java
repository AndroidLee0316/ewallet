package com.pasc.business.ewallet.business.common.param;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/19
 * @des
 * @modify
 **/
public class PaySceneParam {
    @SerializedName("payScene")
    public String scene; //支付场景：RECHARGE("充值"), PAY_SB("支付");
}
