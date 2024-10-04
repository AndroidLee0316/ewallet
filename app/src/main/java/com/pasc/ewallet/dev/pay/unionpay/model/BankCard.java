package com.pasc.ewallet.dev.pay.unionpay.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by zhuangjiguang on 2021/3/9.
 */
public class BankCard implements Serializable {
  @SerializedName("cardNo")
  public String cardNo;
  @SerializedName("bankName")
  public String bankName;
}
