package com.pasc.business.ewallet.business.pay.view;

import com.pasc.business.ewallet.base.CommonBaseView;
import com.pasc.business.ewallet.business.pay.net.resp.ApplySignResp;

/**
 * @date 2019/7/25
 * @des
 * @modify
 **/
public interface ApplySignView extends CommonBaseView {
    void applySignSuccess(ApplySignResp applySignResp);
    void applySignError(String code, String msg);

    void weChatLauncherSuccess(String msg);
    void weChatLauncherError(String msg);

}
