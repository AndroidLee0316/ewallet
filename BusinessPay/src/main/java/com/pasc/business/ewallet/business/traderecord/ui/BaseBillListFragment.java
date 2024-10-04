package com.pasc.business.ewallet.business.traderecord.ui;

import com.pasc.business.ewallet.base.EwalletBaseMvpFragment;
import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.traderecord.view.BillFailRollBackView;

/**
 * @date 2019-08-30
 * @des
 * @modify
 **/
public abstract class BaseBillListFragment<T extends EwalletBasePresenter> extends EwalletBaseMvpFragment<T> {

    public void setBillRollBackView(BillFailRollBackView billRollBackView){}
    public void updateKeyword(String keyword){}
    public void updateType(String orderTypeId){}
    public void updateYearMonth(String payYearMonth){}
    public void updateAll(){}
}
