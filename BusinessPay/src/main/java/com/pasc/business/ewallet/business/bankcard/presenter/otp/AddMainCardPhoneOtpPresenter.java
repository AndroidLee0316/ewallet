package com.pasc.business.ewallet.business.bankcard.presenter.otp;

import com.pasc.business.ewallet.business.bankcard.model.CardModel;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickCardResp;
import com.pasc.business.ewallet.business.bankcard.view.BasePhoneOtpView;
import com.pasc.business.ewallet.business.common.presenter.BasePhoneOtpPresenter;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.SafeCardEvent;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.netpay.resp.VoidObject;
import com.pasc.lib.pay.common.util.ToastUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * @date 2019-08-26
 * @des
 * @modify
 **/
public class AddMainCardPhoneOtpPresenter extends BasePhoneOtpPresenter<BasePhoneOtpView> {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable ();

    public void addAndBindCard(String memberNo, String bankAcctNo, String mobile) {
        getView ().showLoading ("");
        compositeDisposable.add (CardModel.bindCard (memberNo, bankAcctNo, mobile)
                .subscribe (new Consumer<VoidObject> () {
                    @Override
                    public void accept(VoidObject voidObject) {
                        sendMsgCodeSuccess (true);

                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        sendMsgCodeError (code, msg);
                    }
                }));
    }

    public void bindCardValid(String memberNo, String bankAcctNo, String smsCode) {
        getView ().showLoading ("");
        compositeDisposable.add (CardModel.bindCardValid (memberNo, bankAcctNo, smsCode)
                .subscribe (new Consumer<QuickCardResp> () {
                    @Override
                    public void accept(QuickCardResp quickCardResp) throws Exception {
                        EventBusManager.getDefault ().post (new SafeCardEvent (quickCardResp.list));
                        getView ().dismissLoading ();
                        getView ().gotoSetPassWord ("");

                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        getView ().dismissLoading ();
                        ToastUtils.toastMsg (msg);
                    }
                }));
    }


    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
