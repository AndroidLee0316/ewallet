package com.pasc.ewallet.dev.pay.unionpay.model.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhuangjiguang on 2021/3/9.
 */
public class BankCardListParam {
  @SerializedName("memberNo")
  public String memberNo;

  public BankCardListParam(String memberNo) {
    this.memberNo = memberNo;
  }
}
