package com.pasc.business.ewallet.business.pwd.view;

import com.pasc.business.ewallet.base.CommonBaseView;

/**
 * @date 2019/7/14
 * @des
 * @modify
 **/
public interface VerifyPayPwdView extends CommonBaseView {

    void verifyPwdError(String code, String msg);
    void verifyPwdSuccess(String validateCode);
}
