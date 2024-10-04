package com.pasc.business.ewallet.business.bankcard.view;

import com.pasc.business.ewallet.base.CommonBaseView;

/**
 * @date 2019-08-28
 * @des
 * @modify
 **/
public interface BankDetailView extends CommonBaseView {

    void unBindQuickCardSuccess();

    void unBindQuickCardError(String code, String msg);

}
