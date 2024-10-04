package com.pasc.business.ewallet.business.pay.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * 获取支付方式
 * @date 2019/7/30
 * @des
 * @modify
 **/
public class PayTypeParam {
    @SerializedName ("memberNo")
    public String memberNo; //会员号

    public PayTypeParam(String memberNo) {
        this.memberNo = memberNo;
    }
}
