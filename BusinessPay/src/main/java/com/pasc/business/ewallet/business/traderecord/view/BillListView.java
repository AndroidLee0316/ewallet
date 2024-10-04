package com.pasc.business.ewallet.business.traderecord.view;

import com.pasc.business.ewallet.base.EwalletBaseView;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.business.traderecord.net.resp.BillBean;

import java.util.List;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public interface BillListView extends EwalletBaseView {
    void billList(List<BillBean> billBeans, boolean hasMore);

    void billDetail(QueryOrderResp data);

    void tradeError(String code, String msg);

}
