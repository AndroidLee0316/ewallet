package com.pasc.business.ewallet.business.pwd.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.pwd.model.PwdModel;
import com.pasc.business.ewallet.business.pwd.view.SetPassWordView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.netpay.resp.VoidObject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @date 2019/7/13
 * @des
 * @modify
 **/
public class SetPassWordPresenter extends EwalletBasePresenter<SetPassWordView> {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable ();

    public void setPassword(String validateCode, String password) {
        //显示loading
        getView ().showLoading ("");
        Disposable subscribe = PwdModel.setPassword (validateCode, password).subscribe (new Consumer<VoidObject> () {
            @Override
            public void accept(VoidObject voidObject) {
                //关闭loading
                getView ().dismissLoading ();
                getView ().setPassWordSuccess ();
                getView ().resetPassword ();

            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                //关闭loading
                getView ().dismissLoading ();
                if (StatusTable.Account.VALIDATE_CODE_INVALID.equals (code) || StatusTable.Account.VALIDATE_CODE_NOT_MATCH.equals (code)){
                    msg="操作超时，请返回重新进入";
                }
                getView ().setPassWordError (code,msg);
                getView ().resetPassword ();
            }
        });
        compositeDisposable.add (subscribe);
    }

    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
