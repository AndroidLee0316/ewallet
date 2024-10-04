package com.pasc.business.ewallet.business.pay.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
public class PayContextParam {
    @SerializedName ("merchantNo")
    public String merchantNo; //商户号， 充值为空，支付必须
    @SerializedName ("memberNo")
    public String memberNo; //会员号 必须
    @SerializedName ("mchOrderNo")
    public String mchOrderNo; //订单号 ,充值为空，支付必须

    @SerializedName ("payScene")
    public String payScene; //RECHARGE("充值"), PAY_SB("支付"),必须的

    public PayContextParam(String merchantNo,String memberNo, String mchOrderNo,String payScene) {
        this.merchantNo = merchantNo;
        this.memberNo=memberNo;
        this.mchOrderNo = mchOrderNo;
        this.payScene=payScene;
    }
}
