package com.pasc.ewallet.dev.pay.unionpay.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhuangjiguang on 2021/3/9.
 */
public class BankCardOpenStatus {
  @SerializedName("openFlag")
  public int openFlag; //0-未开通 1-已开通
  @SerializedName("openUrl")
  public String openUrl; //开通URL（为0时需要跳转）
  @SerializedName("orderId")
  public String orderId;
  @SerializedName("orderTime")
  public String orderTime;
}
