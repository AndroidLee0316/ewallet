package com.pasc.business.ewallet.business.home.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.home.model.HomeModel;
import com.pasc.business.ewallet.business.home.net.QueryBalanceResp;
import com.pasc.business.ewallet.business.home.view.HomeView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class EWalletHomePresenter extends EwalletBasePresenter<HomeView> {
    private CompositeDisposable compositeDisposable = new CompositeDisposable ();

    public void queryBalance(boolean showLoading, String memberNo) {
        if (showLoading) {
            getView ().showLoading ("");
        }
       Disposable checkUserStatusDispose = HomeModel.queryBalance (memberNo, "1")
                .observeOn (AndroidSchedulers.mainThread ())
                .subscribe (new Consumer<QueryBalanceResp> () {
                    @Override
                    public void accept(QueryBalanceResp conditionResp) {
                        if (showLoading) {
                            getView ().dismissLoading ();
                        }
                        getView ().queryBalanceSuccess (conditionResp);
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        if (showLoading) {
                            getView ().dismissLoading ();
                        }
                        getView ().queryBalanceError (code, msg);
                    }
                });
        compositeDisposable.add (checkUserStatusDispose);
    }


    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }


}
