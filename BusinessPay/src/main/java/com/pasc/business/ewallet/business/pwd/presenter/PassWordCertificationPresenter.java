package com.pasc.business.ewallet.business.pwd.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.account.model.AccountModel;
import com.pasc.business.ewallet.business.pwd.view.PassWordCertificationView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.netpay.resp.VoidObject;

import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @date 2019/7/15
 * @des
 * @modify
 **/
public class PassWordCertificationPresenter extends EwalletBasePresenter<PassWordCertificationView> {


    private CompositeDisposable compositeDisposable=new CompositeDisposable ();

    public void validCert(String memberNo, String certificateNo, String memberName,String scene) {
        getView ().showLoading ("");
        compositeDisposable.add( AccountModel.validCert (memberNo, certificateNo, memberName)
                .flatMap (new Function<VoidObject, SingleSource<VoidObject>> () {
                    @Override
                    public SingleSource<VoidObject> apply(VoidObject voidObject) {
                        //发送短信验证码
                        return AccountModel.sendSms (memberNo,scene);
                    }
                })
                .observeOn (AndroidSchedulers.mainThread ()).subscribe (new Consumer<VoidObject> () {
                    @Override
                    public void accept(VoidObject voidObject) {
                        getView ().authenticationSuccess ();
                        getView ().dismissLoading ();

                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String errorCode, String errorMsg) {
                        getView ().authenticationError (errorCode, errorMsg);
                        getView ().dismissLoading ();
                    }
                }));
    }



    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
