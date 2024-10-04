package com.pasc.business.ewallet.business.pay.presenter;

import com.pasc.business.ewallet.business.bankcard.model.CardModel;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickPaySendMsgResp;
import com.pasc.business.ewallet.business.common.presenter.BasePhoneOtpPresenter;
import com.pasc.business.ewallet.business.pay.view.SendMsgCodeView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import io.reactivex.functions.Consumer;

/**
 * @date 2019/8/22
 * @des
 * @modify
 **/
public class SendMsgCodePresenter extends BasePhoneOtpPresenter<SendMsgCodeView> {

    public void sendMsgCode(String cardKey, String mchOrderNo, String tradeType, long amount){
        compositeDisposable.add (CardModel.quickPaySendMsg (cardKey,mchOrderNo,tradeType,amount)
                .subscribe (new Consumer<QuickPaySendMsgResp> () {
                    @Override
                    public void accept(QuickPaySendMsgResp paySendMsgResp) throws Exception {
                        sendMsgCodeSuccess (true);
                        getView ().validPwdAndSendMsgCodeSuccess (paySendMsgResp);
                        getView ().dismissLoading ();
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        sendMsgCodeError (code,msg);
                        getView ().validPwdAndSendMsgCodeError (code,msg);
                        getView ().dismissLoading ();
                    }
                }));
    }
}
