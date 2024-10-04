package com.pasc.business.ewallet.business.pay.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhuangjiguang on 2021/4/21.
 */
public class CreateRechargeOrderParam {
  @SerializedName("mchOrderNo")
  public String mchOrderNo; //商户订单号
  @SerializedName("memberNo")
  public String memberNo; //会员号
  @SerializedName ("amount")
  public long amount = 1;//充值金额，单位为分

}
