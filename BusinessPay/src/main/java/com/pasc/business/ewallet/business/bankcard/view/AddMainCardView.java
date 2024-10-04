package com.pasc.business.ewallet.business.bankcard.view;

import com.pasc.business.ewallet.base.CommonBaseView;

/**
 * @date 2019/7/14
 * @des
 * @modify
 **/
public interface AddMainCardView extends CommonBaseView {
    void bindCardSuccess();
    void bindCardError(String code,String msg);

}
