package com.pasc.business.ewallet.business.pay.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
public class CreatePayOrderParam {

    @SerializedName("memberNo")
    public String memberNo; //会员号

    @SerializedName("mchOrderNo")
    public String mchOrderNo; //商户订单号

    @SerializedName("merchantNo")
    public String merchantNo; //商户号

    @SerializedName("amount")
    public long amount = 1; //订单金额（单位：分）

    @SerializedName("notifyUrl")
    public String notifyUrl = "http://www.baidu.com"; //回调地址

    @SerializedName("goodsName")
    public String goodsName = "测试商品"; //商品信息


    @SerializedName ("appId")
    public String appId;//渠道appId

    @SerializedName ("sceneId")
    public String sceneId;//支付场景id

//    @SerializedName("ssss")
//    public List<String> ssss = new ArrayList<> (); //订单金额（单位：分）
//
//    {
//        ssss.add ("gbgv");
//        ssss.add ("1111");
//        ssss.add ("Adx");
//        ssss.add ("2222");
//
//
//    }
//    @SerializedName("jj")
//    public J jj = new J (); //商品信息
//    public static class J{
//        @SerializedName("ss")
//        public String ss="11";
//    }



}
