package com.pasc.ewallet.dev.pay.unionpay.model.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhuangjiguang on 2021/3/9.
 */
public class BankCardOpenStatusParam {
  @SerializedName("memberNo")
  public String memberNo;
  @SerializedName("mchOrderNo")
  public String mchOrderNo;
  @SerializedName("payType")
  public String payType;
  @SerializedName("cardNo")
  public String cardNo;

  public BankCardOpenStatusParam(String memberNo, String mchOrderNo, String payType,
      String cardNo) {
    this.memberNo = memberNo;
    this.mchOrderNo = mchOrderNo;
    this.payType = payType;
    this.cardNo = cardNo;
  }
}
