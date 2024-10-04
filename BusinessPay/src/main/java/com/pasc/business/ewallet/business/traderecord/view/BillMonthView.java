package com.pasc.business.ewallet.business.traderecord.view;

import com.pasc.business.ewallet.base.CommonBaseView;
import com.pasc.business.ewallet.business.traderecord.net.resp.PayMonthBean;

import java.util.List;

/**
 * @date 2019/7/10
 * @des
 * @modify
 **/
public interface BillMonthView extends CommonBaseView {
    void getMonthBillSuccess(List<PayMonthBean> payMonthBeans);
    void getMonthBillError(String code,String msg);


}
