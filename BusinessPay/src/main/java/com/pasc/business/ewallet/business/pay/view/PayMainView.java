package com.pasc.business.ewallet.business.pay.view;

import com.pasc.business.ewallet.base.CommonBaseView;

/**
 * @date 2019/7/30
 * @des
 * @modify
 **/
public interface PayMainView extends  CommonBaseView {

    void aliPaySuccess(String msg);

    void aliPayError(String msg,boolean isCancel);

    void weChatLauncherSuccess(String msg);

    void weChatLauncherError(String msg);

}
