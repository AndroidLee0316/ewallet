package com.pasc.business.ewallet.business.pay.view;

import com.pasc.business.ewallet.base.CommonBaseView;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;

/**
 * @date 2019-09-03
 * @des
 * @modify
 **/
public interface QueryOrderView extends CommonBaseView {
    void queryOrderStatusSuccess(QueryOrderResp orderResp);

    void queryOrderStatusError(String code,String msg);

    void queryOrderStatusTimeOut();
}
