package com.pasc.business.ewallet.business.account.presenter.otp;

import com.pasc.business.ewallet.business.account.model.AccountModel;
import com.pasc.business.ewallet.business.bankcard.view.BasePhoneOtpView;
import com.pasc.business.ewallet.business.common.presenter.BasePhoneOtpPresenter;
import com.pasc.business.ewallet.business.pwd.net.resp.ValidateCodeResp;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.netpay.resp.VoidObject;
import com.pasc.lib.pay.common.util.ToastUtils;

import io.reactivex.functions.Consumer;

/**
 * 开户otp 和 开户开通钱包
 *
 * @date 2019/7/22
 * @des
 * @modify
 **/
public class CreateAccountPhoneOtpPresenter extends BasePhoneOtpPresenter<BasePhoneOtpView> {

    /***
     * 开户发送验证码
     * @param memberNo
     */
    public void sendSms(String memberNo,String scene) {
        //正常开户重新发送验证码
        getView ().showLoading ("");
        compositeDisposable.add (AccountModel.sendSms (memberNo,scene)
                .subscribe (new Consumer<VoidObject> () {
                    @Override
                    public void accept(VoidObject checkCardResp) {
                        sendMsgCodeSuccess (true);
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        sendMsgCodeError (code, msg);
                    }
                }));

    }

    public void validSms(String memberNo,String messageCode,String scene){
        getView ().showLoading ("");
        compositeDisposable.add (AccountModel.validSms (memberNo,messageCode,scene)
                .subscribe (new Consumer<ValidateCodeResp> () {
                    @Override
                    public void accept(ValidateCodeResp checkCardResp) {
                        getView ().dismissLoading ();
                        getView ().gotoSetPassWord (checkCardResp.returnCode);
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        getView ().dismissLoading ();
                        ToastUtils.toastMsg (msg);

                    }
                }));
    }


}
