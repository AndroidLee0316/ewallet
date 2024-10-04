package com.pasc.business.ewallet.business.traderecord.view;

import com.pasc.business.ewallet.base.EwalletBaseView;
import com.pasc.business.ewallet.business.traderecord.net.resp.AvailBalanceBean;

import java.util.List;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public interface BalanceView extends EwalletBaseView {
    void availBalanceList(List<AvailBalanceBean> balanceList);
    void tradeError(String code,String msg);
}
