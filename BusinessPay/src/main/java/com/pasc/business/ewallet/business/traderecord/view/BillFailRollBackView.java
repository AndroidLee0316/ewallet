package com.pasc.business.ewallet.business.traderecord.view;

/**
 * @date 2019/7/15
 * @des
 * @modify
 **/
public interface BillFailRollBackView {
    void rollback(String orderTypeId, String payYearMonth);
}
