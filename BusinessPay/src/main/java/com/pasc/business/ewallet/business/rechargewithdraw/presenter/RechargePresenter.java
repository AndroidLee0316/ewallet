package com.pasc.business.ewallet.business.rechargewithdraw.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.business.rechargewithdraw.model.RechargeWithDrawModel;
import com.pasc.business.ewallet.business.rechargewithdraw.view.RechargeView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * @date 2019/8/15
 * @des
 * @modify
 **/
public class RechargePresenter extends EwalletBasePresenter<RechargeView> {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable ();

    public void recharge(String memberNo, String payType, long amount) {
        compositeDisposable.add (RechargeWithDrawModel.recharge (memberNo, payType, amount, null).subscribe (new Consumer<PayResp> () {
            @Override
            public void accept(PayResp payResp) {

            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
            }
        }));
    }
    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
