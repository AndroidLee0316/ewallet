package com.pasc.ewallet.dev.pay.unionpay.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.ewallet.dev.pay.unionpay.model.BankCard;
import com.pasc.ewallet.dev.pay.unionpay.model.UnionPayModel;
import com.pasc.ewallet.dev.pay.unionpay.view.QueryBankCardListView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import java.util.List;

/**
 * Created by zhuangjiguang on 2021/3/9.
 */
public class UnionCardListPresenter extends EwalletBasePresenter<QueryBankCardListView> {
  protected CompositeDisposable compositeDisposable = new CompositeDisposable ();
  public void queryBankCardList(String memberNo) {
    getView().showLoading("");
    compositeDisposable.add(UnionPayModel.queryBankCardList(memberNo)
        .subscribe(new Consumer<List<BankCard>>() {
          @Override
          public void accept(List<BankCard> bankCardList) {
            getView().dismissLoading();
            getView().queryBankCardSuccess(bankCardList);
          }
        }, new BaseRespThrowableObserver() {
          @Override
          public void onV2Error(String code, String msg) {
            getView().dismissLoading();
            getView().queryBankCardError(code, msg);
          }
        }));
  }

  @Override
  public void onMvpDetachView(boolean retainInstance) {
    compositeDisposable.clear();
    super.onMvpDetachView(retainInstance);
  }
}
