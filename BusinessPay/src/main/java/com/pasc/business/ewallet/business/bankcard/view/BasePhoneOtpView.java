package com.pasc.business.ewallet.business.bankcard.view;

import com.pasc.business.ewallet.base.CommonBaseView;

/**
 * @date 2019/7/19
 * @des
 * @modify
 **/
public interface BasePhoneOtpView extends CommonBaseView {
    void showElapseTime(int count);

    void showElapseTimeUp();

    void gotoSetPassWord(String validateCode);
}
