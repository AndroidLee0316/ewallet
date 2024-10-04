package com.pasc.business.ewallet.business.bankcard.view;

import com.pasc.business.ewallet.base.CommonBaseView;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickCardBean;
import com.pasc.business.ewallet.business.bankcard.net.resp.SafeCardBean;

import java.util.List;

/**
 * @date 2019/8/16
 * @des
 * @modify
 **/
public interface BankCardListView extends CommonBaseView {

     void queryCardListSuccess(List<SafeCardBean> safeCardBeans, List<QuickCardBean> quickCardBeans);
    void queryCardListError(String code,String msg);

}
