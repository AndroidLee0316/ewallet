package com.pasc.business.ewallet.business.home.view;

import com.pasc.business.ewallet.base.CommonBaseView;
import com.pasc.business.ewallet.business.home.net.QueryBalanceResp;

/**
 * @date 2019/6/25
 * @des
 * @modify
 **/
public interface HomeView extends CommonBaseView {

    void queryBalanceSuccess(QueryBalanceResp balanceResp);
    void queryBalanceError(String code,String msg);


}
