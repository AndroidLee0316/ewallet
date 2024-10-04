package com.pasc.business.ewallet.business.pay.view;

import com.pasc.business.ewallet.base.CommonBaseView;
import com.pasc.business.ewallet.business.pay.net.resp.PayContextResp;

/**
 * @date 2019/7/25
 * @des
 * @modify
 **/
public interface PayMainStandView extends CommonBaseView {
    void queryPayContextSuccess(PayContextResp typeBeans,boolean isRefresh);

    void queryPayContextError(String code, String msg,boolean isRefresh);

}
