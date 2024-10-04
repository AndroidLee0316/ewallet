package com.pasc.business.ewallet.business.pay.view;

import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.base.CommonBaseView;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickPaySendMsgResp;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;

/**
 * @date 2019/8/17
 * @des
 * @modify
 **/
@NotProguard
public interface PayView extends CommonBaseView {

    void paySuccess(String payType, PayResp tradePayResp);

    void payError(String payType, String code, String msg);

     void validPwdAndSendMsgCodeSuccess(QuickPaySendMsgResp paySendMsgResp);

     void validPwdAndSendMsgCodeError(String code,String msg);

}
