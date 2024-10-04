package com.pasc.business.ewallet.business.traderecord.view;

import com.pasc.business.ewallet.base.EwalletBaseView;
import com.pasc.business.ewallet.business.traderecord.net.resp.BillTypeResp;

import java.util.List;

/**
 * @date 2019/7/10
 * @des
 * @modify
 **/
public interface BillHomeView extends EwalletBaseView {

    void getPayTypeListSuccess(List<BillTypeResp.PayBillTypeBean> payBillTypeList, boolean needShow);

    void getPayTypeListError(String code, String msg);

    void showLoading(String msg);

    void hideLoading();


}
