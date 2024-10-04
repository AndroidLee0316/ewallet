package com.pasc.business.ewallet.business.pay.net.resp;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.NotProguard;

/**
 *
 * 直接结果
 * @date 2019/7/30
 * @des
 * @modify
 **/
@NotProguard
public class PayResp {
    @SerializedName ("returnValue")
    public  String returnValue; //支付宝：预付款单信息，提供给app调起支付宝

    @SerializedName ("orderno")
    public  String orderno; //微信：商户订单号

    @SerializedName ("appid")
    public  String appid; //微信：商户APPID

    @SerializedName ("partnerid")
    public  String partnerid; //微信：商户号

    @SerializedName ("prepayid")
    public  String prepayid; //微信：预付款单号

    @SerializedName ("packageValue")
    public  String packageValue; //微信：扩展字段

    @SerializedName ("noncestr")
    public  String noncestr; //微信：随机字符串

    @SerializedName ("timestamp")
    public  String timestamp; //微信：时间戳

    @SerializedName ("sign")
    public  String sign; //微信：签名


    @SerializedName ("rechargeOrderNo")
    public String rechargeOrderNo; //充值订单号，与支付渠道无关。

    @SerializedName ("unionPayResponse")
    public UnionPayResponse unionPayResponse; // 银联相关的

    @SerializedName ("suningResponse")
    public String suningResponse; // 苏宁支付

    @SerializedName ("cmbchinaResponse")
    public String cmbchinaResponse; // 招行支付

    @SerializedName ("swiftResponse")
    public String swiftResponse; // 威富通

    @SerializedName ("wechatJsapiResponse")
    public String wechatJsapiResponse; // 微信小程序支付
}
