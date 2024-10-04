package com.pasc.business.ewallet.business.pay.net.param;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/7/30
 * @des
 * @modify
 **/
public class PayParam {

    @SerializedName("mchOrderNo")
    public String mchOrderNo; //商户订单号 ,must

    @SerializedName("memberNo")
    public String memberNo; //会员号 ,must

    /****{@link com.pasc.business.ewallet.business.StatusTable.PayType}****/
    @SerializedName("orderType")
    public String orderType; //支付类型 BALANCE-余额支付，WECHAT-微信支付，ALIPAY-支付宝支付 ， UNIONQUICKPAY-银联快捷支付 ,must

    @SerializedName("payPwd")
    public String payPwd; //支付密码，如果orderType为BALANCE必传

    public PayParam(String mchOrderNo, String memberNo, String orderType, String payPwd) {
        this.mchOrderNo = mchOrderNo;
        this.memberNo = memberNo;
        this.orderType = orderType;
        if (!TextUtils.isEmpty (payPwd)) {
            this.payPwd = payPwd;
        }
    }



    @SerializedName("paydate")
    public String paydate; //银联下单时间，支付方式为UNIONQUICKPAY时必传
    @SerializedName("cardKey")
    public String cardKey; //银行卡唯一id，支付方式为UNIONQUICKPAY时必传

    @SerializedName("unionOrderId")
    public String unionOrderId; // 由短信生成， 银联订单号，支付方式为UNIONQUICKPAY时必传
    @SerializedName("unionVerifyCode")
    public String unionVerifyCode; //由短信生成， 银联短信验证码，支付方式为UNIONQUICKPAY时必传

    @SerializedName("source")
    public String source = "APP"; //渠道 APP/MINIPROGRAM
    @SerializedName("bankCardNo")
    public String bankCardNo;
}
