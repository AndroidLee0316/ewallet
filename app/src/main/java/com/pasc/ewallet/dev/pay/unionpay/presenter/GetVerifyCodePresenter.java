package com.pasc.ewallet.dev.pay.unionpay.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.ewallet.dev.pay.unionpay.model.UnionPayModel;
import com.pasc.ewallet.dev.pay.unionpay.model.param.SendSmsParam;
import com.pasc.ewallet.dev.pay.unionpay.view.GetVerifyCodeView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.netpay.resp.VoidObject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class GetVerifyCodePresenter extends EwalletBasePresenter<GetVerifyCodeView> {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable ();
    public void getVerifyCode(SendSmsParam sendSmsParam) {
        getView ().showLoading ("");
        compositeDisposable.add(UnionPayModel.sendSmsVerifyCode(sendSmsParam)
                .subscribe (new Consumer<VoidObject> () {
                    @Override
                    public void accept(VoidObject voidObject) {
                        getView().dismissLoading();
                        getView().getVerifyCodeSuccess();

                    }
                }, new BaseRespThrowableObserver() {
                    @Override
                    public void onV2Error(String code, String msg) {
                        getView().dismissLoading();
                        getView().getVerifyCodeError(code, msg);
                    }
                }));
    }

    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
