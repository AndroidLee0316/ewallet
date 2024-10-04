package com.pasc.business.ewallet.business.bankcard.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.bankcard.model.CardModel;
import com.pasc.business.ewallet.business.bankcard.view.BankDetailView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.netpay.resp.VoidObject;
import com.pasc.lib.pay.common.util.ToastUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * @date 2019/8/16
 * @des
 * @modify
 **/
public class BankCardDetailPresenter extends EwalletBasePresenter<BankDetailView> {
    CompositeDisposable compositeDisposable=new CompositeDisposable ();
    public void unBindQuickCard(String cardKey,String memberNo){
        getView ().showLoading ("");
        compositeDisposable.add (  CardModel.unBindQuickCard (cardKey,memberNo).subscribe (new Consumer<VoidObject> () {
            @Override
            public void accept(VoidObject voidObject) throws Exception {
                getView ().dismissLoading ();
                getView ().unBindQuickCardSuccess ();
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                getView ().dismissLoading ();
                getView ().unBindQuickCardError (code,msg);
            }
        }));
    }


    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
