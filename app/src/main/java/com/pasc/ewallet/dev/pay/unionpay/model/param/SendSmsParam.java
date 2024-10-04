package com.pasc.ewallet.dev.pay.unionpay.model.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhuangjiguang on 2021/3/9.
 */
public class SendSmsParam {
  @SerializedName("memberNo")
  public String memberNo;
  @SerializedName("cardNo")
  public String cardNo;
  @SerializedName("payType")
  public String payType;
  @SerializedName("mchOrderNo")
  public String mchOrderNo;
  @SerializedName("orderId")
  public String orderId;
  @SerializedName("orderTime")
  public String orderTime;

  public SendSmsParam(String memberNo, String cardNo, String payType, String mchOrderNo,
      String orderId, String orderTime) {
    this.memberNo = memberNo;
    this.cardNo = cardNo;
    this.payType = payType;
    this.mchOrderNo = mchOrderNo;
    this.orderId = orderId;
    this.orderTime = orderTime;
  }
}
