package com.pasc.ewallet.dev.pay.unionpay.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.business.pay.view.PayView;
import com.pasc.ewallet.dev.R;
import com.pasc.ewallet.dev.TheApplication;
import com.pasc.ewallet.dev.pay.unionpay.model.UnionPayModel;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by zhuangjiguang on 2022/4/1.
 */
public class UnionPayPresenter extends EwalletBasePresenter<PayView> {
  CompositeDisposable compositeDisposable = new CompositeDisposable ();
  public void payUnionCard(String mchOrderNo, String memberNo, String orderType,
      String unionOrderId, String paydate, String bankCardNo, String unionVerifyCode){
    String tip=
        TheApplication.getApplication().getString (R.string.ewallet_pay_waiting_tip);
    getView().showLoading (tip);
    Disposable disposable = UnionPayModel.payUnionCard (mchOrderNo, memberNo, orderType, unionOrderId,
        paydate, bankCardNo, unionVerifyCode
    ).subscribe (new Consumer<PayResp>() {
      @Override
      public void accept(PayResp payResp) {
        getView().paySuccess (orderType, payResp);
        getView().dismissLoading ();
      }
    }, new BaseRespThrowableObserver() {
      @Override
      public void onV2Error(String code, String msg) {
        getView().dismissLoading();
        getView().payError(orderType, code, msg);

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
