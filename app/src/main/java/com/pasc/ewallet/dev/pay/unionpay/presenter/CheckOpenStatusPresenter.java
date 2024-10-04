package com.pasc.ewallet.dev.pay.unionpay.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.ewallet.dev.pay.unionpay.model.BankCardOpenStatus;
import com.pasc.ewallet.dev.pay.unionpay.model.UnionPayModel;
import com.pasc.ewallet.dev.pay.unionpay.view.CheckOpenStatusView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class CheckOpenStatusPresenter extends EwalletBasePresenter<CheckOpenStatusView> {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable ();
    public void checkOpenStatus(String memberNo, String mchOrderNo, String payType, String cardNo) {
        getView ().showLoading ("");
        compositeDisposable.add (UnionPayModel.checkOpenStatus(memberNo, mchOrderNo, payType, cardNo)
                .subscribe (new Consumer<BankCardOpenStatus> () {
                    @Override
                    public void accept(BankCardOpenStatus bankCardOpenStatus) {
                        getView().dismissLoading();
                        getView().queryOpenStatusSuccess(bankCardOpenStatus);

                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        getView().dismissLoading();
                        getView().queryOpenStatusError(code, msg);
                    }
                }));
    }

    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
