package com.pasc.ewallet.dev.pay.unionpay.view;

import com.pasc.business.ewallet.base.CommonBaseView;

/**
 * @date 2021/03/05
 * @des
 * @modify
 **/
public interface GetVerifyCodeView extends CommonBaseView {
    void getVerifyCodeSuccess();
    void getVerifyCodeError(String code, String msg);

}
