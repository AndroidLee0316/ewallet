package com.pasc.ewallet.dev.pay.unionpay.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhuangjiguang on 2021/3/9.
 */
public class MerchantOrderInfo {
  @SerializedName("orderId")
  public String orderId;
  @SerializedName("orderTime")
  public String orderTime;
}
