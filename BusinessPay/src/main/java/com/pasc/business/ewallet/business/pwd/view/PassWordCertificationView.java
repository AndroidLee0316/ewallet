package com.pasc.business.ewallet.business.pwd.view;

import com.pasc.business.ewallet.base.CommonBaseView;

/**
 * @date 2019/7/15
 * @des
 * @modify
 **/
public interface PassWordCertificationView extends CommonBaseView {

    void authenticationSuccess();


    void authenticationError(String code, String msg);
}
