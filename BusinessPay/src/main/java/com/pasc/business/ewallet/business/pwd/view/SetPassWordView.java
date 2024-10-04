package com.pasc.business.ewallet.business.pwd.view;

import com.pasc.business.ewallet.base.CommonBaseView;

/**
 * @date 2019/7/13
 * @des
 * @modify
 **/
public interface SetPassWordView extends CommonBaseView {

    void passWordNotEqual();
    void resetPassword();
    void setPassWordSuccess();
    void setPassWordError(String code,String msg);

}
