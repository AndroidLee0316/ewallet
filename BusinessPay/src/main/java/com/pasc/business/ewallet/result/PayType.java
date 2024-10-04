package com.pasc.business.ewallet.result;

import com.pasc.business.ewallet.NotProguard;

/*** 支付方式 */
@NotProguard
public interface PayType {
    /**
     * 微信
     */
    int WECHATPAY = 0;
    /**
     * 支付宝
     */
    int ALIPAY = 1;
}