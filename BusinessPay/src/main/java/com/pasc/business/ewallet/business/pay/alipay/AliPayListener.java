package com.pasc.business.ewallet.business.pay.alipay;

/**
 * @date 2019/7/30
 * @des
 * @modify
 **/
public interface AliPayListener {

    void aliPaySuccess(String msg);

    void aliPayError(String msg,boolean isCancel);

}
