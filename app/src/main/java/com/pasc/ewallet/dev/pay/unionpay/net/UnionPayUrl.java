package com.pasc.ewallet.dev.pay.unionpay.net;

import com.pasc.business.ewallet.business.common.CommonUrl;

/**
 * Created by zhuangjiguang on 2021/3/9.
 */
public class UnionPayUrl {
  //绑定的银行卡列表
  public static String bankCardList() {
    return CommonUrl.getHostAndGateWay() + CommonUrl.URL_PREFIX + "/union/listBankCard";
  }

  //查询银行卡是否开通银联支付
  public static String checkOpenStatus() {
    return CommonUrl.getHostAndGateWay() + CommonUrl.URL_PREFIX + "/union/checkOpen";
  }

  //发送短信验证码
  public static String sendSmsVerifyCode() {
    return CommonUrl.getHostAndGateWay() + CommonUrl.URL_PREFIX + "/union/sendMsg";
  }
}
