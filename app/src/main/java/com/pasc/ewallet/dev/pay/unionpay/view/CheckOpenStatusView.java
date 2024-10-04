package com.pasc.ewallet.dev.pay.unionpay.view;

import com.pasc.business.ewallet.base.CommonBaseView;
import com.pasc.ewallet.dev.pay.unionpay.model.BankCardOpenStatus;

/**
 * @date 2021/03/04
 * @des
 * @modify
 **/
public interface CheckOpenStatusView extends CommonBaseView {
    void queryOpenStatusSuccess(BankCardOpenStatus bankCardOpenStatus);
    void queryOpenStatusError(String code, String msg);

}
