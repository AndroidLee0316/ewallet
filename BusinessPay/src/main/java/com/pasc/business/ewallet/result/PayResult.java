package com.pasc.business.ewallet.result;

import com.pasc.business.ewallet.NotProguard;

/**
 * 第三方支付通知支付结果
 * 如，微信、支付宝支付等
 */
@NotProguard
public interface PayResult {
    /**
     * 支付成功
     */
    int PAY_SUCCESS = 0;
    /**
     * 支付取消
     */
    int PAY_CANCEL = 1;
    /**
     * 支付失败
     */
    int PAY_FAILED = 2;
    /**
     * 支付处理中
     */
    int PAY_WAITING = 3;

}