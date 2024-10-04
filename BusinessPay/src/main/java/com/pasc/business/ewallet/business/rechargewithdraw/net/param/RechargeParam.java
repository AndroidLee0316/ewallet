package com.pasc.business.ewallet.business.rechargewithdraw.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class RechargeParam {
    @SerializedName ("memberNo")
    public String memberNo; //会员号

    @SerializedName ("payType")
    public String payType; //支付方式:WECHAT-微信支付,ALIPAY-支付宝支付

    @SerializedName ("amount")
    public long amount; //金额（单位：分）

    @SerializedName ("mchOrderNo")
    public String mchOrderNo; //商户订单号


    public RechargeParam(String memberNo, String payType, long amount, String mchOrderNo) {
        this.memberNo = memberNo;
        this.payType = payType;
        this.amount = amount;
        this.mchOrderNo = mchOrderNo;
    }

    @SerializedName ("cardKey")
    public String cardKey; //银行卡唯一id, 当支付方式为UNIONQUICKPAY时必传

    @SerializedName ("unionOrderId")
    public String unionOrderId; //银联单号，当支付方式为UNIONQUICKPAY时必传，调用银联发送验证码接口会返回此字段

    @SerializedName ("paydate")
    public String paydate; //银联下单时间，当支付方式为UNIONQUICKPAY时必传，调用银联发送验证码接口会返回此字段

    @SerializedName ("unionVerifyCode")
    public String unionVerifyCode; //验证码
}
