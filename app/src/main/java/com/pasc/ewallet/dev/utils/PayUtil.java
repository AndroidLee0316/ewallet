package com.pasc.ewallet.dev.utils;

/**
 * Created by zhuangjiguang on 2022/3/31.
 */
public class PayUtil {
  /**
   * 支付方式类型
   */
  public interface PayType {
    String SNPAY = "SUNING"; //苏宁支付
    String CMBCHINAPAY = "CMBCHINAPAY"; //招行支付
    String SWIFT = "SWIFT"; //威富通
    String WECHATJSAPI = "WECHATJSAPI"; //绵阳小程序支付
    String UNION_BANK = "UNION_BANK"; //银联无跳转支付
    String UNION_ALIPAYJSAPI = "UNION_ALIPAYJSAPI"; //银联支付宝
    String UNION_WECHATJSAPI = "UNION_WECHATJSAPI"; //银联微信
    String PABCLOUDPAY = "PABCLOUDPAY"; //平安银行云收款，小程序支付
  }

}
