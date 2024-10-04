package com.pasc.business.ewallet.business.account.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.account.model.AccountModel;
import com.pasc.business.ewallet.business.account.net.resp.QueryMemberResp;
import com.pasc.business.ewallet.business.account.view.CreateAccountView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @date 2019/6/26
 * @des
 * @modify
 **/
public class CreateAccountPresenter extends EwalletBasePresenter<CreateAccountView> {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable ();

    /****获取用户信息**/
    public void queryMemberByMemberNo(String memberNo) {
        //显示loading
        getView ().showLoading ("");
        Disposable disposable = AccountModel.queryMemberByMemberNo (memberNo)
                .subscribe (new Consumer<QueryMemberResp> () {
                    @Override
                    public void accept(QueryMemberResp data) {
                        //关闭loading
                        getView ().dismissLoading ();
                        getView ().queryQueryMemberSuccess (data);
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        //关闭loading
                        getView ().dismissLoading ();
                        getView ().queryQueryMemberError (code, msg);
                    }
                });
        compositeDisposable.add (disposable);
    }


    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
