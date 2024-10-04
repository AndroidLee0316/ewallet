package com.pasc.ewallet.dev.pay.snpay;

/**
 * @date 2020/5/15
 * @des
 * @modify
 **/
public interface SNPayListener {

    void snPaySuccess(String msg);

    void snPayError(String msg, boolean isCancel);

}
