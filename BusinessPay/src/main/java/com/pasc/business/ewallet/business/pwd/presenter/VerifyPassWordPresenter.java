package com.pasc.business.ewallet.business.pwd.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.pwd.model.PwdModel;
import com.pasc.business.ewallet.business.pwd.net.resp.ValidateCodeResp;
import com.pasc.business.ewallet.business.pwd.view.VerifyPayPwdView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class VerifyPassWordPresenter extends EwalletBasePresenter<VerifyPayPwdView> {

    private CompositeDisposable compositeDisposable=new CompositeDisposable ();

    public void verifyPassword(String confirmPassword,boolean successNeedDisMissDialog) {
        getView ().showLoading ("");
        compositeDisposable.add (PwdModel.verifyPassword (confirmPassword).subscribe (new Consumer<ValidateCodeResp> () {
            @Override
            public void accept(ValidateCodeResp c) {
                if (successNeedDisMissDialog) {
                    getView ().dismissLoading ();
                }
                getView ().verifyPwdSuccess (c.returnCode);
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                getView ().dismissLoading ();
                getView ().verifyPwdError (code,msg);
            }
        }));
    }


    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
