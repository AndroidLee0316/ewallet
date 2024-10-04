package com.pasc.business.ewallet.business.pay.view;

import com.pasc.business.ewallet.base.CommonBaseView;
import com.pasc.business.ewallet.business.pay.net.resp.SignStatusResp;

/**
 * @date 2019/12/15
 * @des
 * @modify
 **/
public interface QuerySignStatusView extends CommonBaseView {
    void querySignStatusSuccess(SignStatusResp signStatusResp);

    void querySignStatusError(String code, String msg);
    void queryNoSignStatusError();
    void querySignStatusTimeOut();
}
