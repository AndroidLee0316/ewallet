package com.pasc.business.ewallet.business.pay.view;

import com.pasc.business.ewallet.business.bankcard.net.resp.QuickPaySendMsgResp;
import com.pasc.business.ewallet.business.bankcard.view.BasePhoneOtpView;

/**
 * @date 2019/8/22
 * @des
 * @modify
 **/
public interface SendMsgCodeView extends BasePhoneOtpView {
     void validPwdAndSendMsgCodeSuccess(QuickPaySendMsgResp paySendMsgResp);
     void validPwdAndSendMsgCodeError(String code,String msg);
}
