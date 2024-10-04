package com.pasc.business.ewallet.business.bankcard.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.bankcard.model.CardModel;
import com.pasc.business.ewallet.business.bankcard.view.AddMainCardView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.netpay.resp.VoidObject;
import com.pasc.lib.pay.common.util.ToastUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class AddMainCardPresenter extends EwalletBasePresenter<AddMainCardView> {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable ();
    public void addAndBindCard(String memberNo, String bankAcctNo, String mobile) {
        getView ().showLoading ("");
        compositeDisposable.add (CardModel.bindCard (memberNo, bankAcctNo, mobile)
                .subscribe (new Consumer<VoidObject> () {
                    @Override
                    public void accept(VoidObject voidObject) {
                        getView ().dismissLoading ();
                        getView ().bindCardSuccess ();

                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        getView ().dismissLoading ();
                        getView ().bindCardError (code,msg);
                    }
                }));
    }

    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
