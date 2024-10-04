package com.pasc.business.ewallet.business.rechargewithdraw.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.bankcard.model.CardModel;
import com.pasc.business.ewallet.business.bankcard.net.resp.SafeCardBean;
import com.pasc.business.ewallet.business.home.model.HomeModel;
import com.pasc.business.ewallet.business.home.net.QueryBalanceResp;
import com.pasc.business.ewallet.business.rechargewithdraw.model.RechargeWithDrawModel;
import com.pasc.business.ewallet.business.rechargewithdraw.net.resp.CalcWithdrawFeeResp;
import com.pasc.business.ewallet.business.rechargewithdraw.net.resp.WithdrawResp;
import com.pasc.business.ewallet.business.rechargewithdraw.view.WithdrawView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * @date 2019/8/15
 * @des
 * @modify
 **/
public class WithdrawPresenter extends EwalletBasePresenter<WithdrawView> {
    CompositeDisposable compositeDisposable=new CompositeDisposable ();

    public void queryBalanceFee(String memberNo, long originWithdraw) {
        getView ().showLoading ("");
       Disposable disposable = RechargeWithDrawModel.calcWithdrawFee (memberNo, originWithdraw).subscribe (new Consumer<CalcWithdrawFeeResp> () {
            @Override
            public void accept(CalcWithdrawFeeResp calcWithdrawFeeResp) {
                getView ().queryBalanceFeeSuccess (originWithdraw,calcWithdrawFeeResp);
                getView ().dismissLoading ();
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                getView ().queryBalanceFeeFail (code, msg);
                getView ().dismissLoading ();

            }
        });

        compositeDisposable.add (disposable);

    }

    private static class BankCardAndBalanceBean {
        List<SafeCardBean> safeCardBeans;
        QueryBalanceResp balanceResp;

        public BankCardAndBalanceBean(List<SafeCardBean> safeCardBeans, QueryBalanceResp balanceResp) {
            this.safeCardBeans = safeCardBeans;
            this.balanceResp = balanceResp;
        }
    }

    public void listSafeBankAndBalance(String memberNo) {
       Disposable safeDispose = Single.zip (CardModel.listSafeBank (memberNo)
                , HomeModel.queryBalance (memberNo, "1")
                , new BiFunction<List<SafeCardBean>, QueryBalanceResp, BankCardAndBalanceBean> () {
                    @Override
                    public BankCardAndBalanceBean apply(List<SafeCardBean> safeCardBeans, QueryBalanceResp balanceResp) {
                        return new BankCardAndBalanceBean (safeCardBeans, balanceResp);
                    }
                }).observeOn (AndroidSchedulers.mainThread ()).subscribe (new Consumer<BankCardAndBalanceBean> () {
            @Override
            public void accept(BankCardAndBalanceBean bankCardAndBalanceBean) {
//                if (bankCardAndBalanceBean.safeCardBeans.size ()==0){
//                    bankCardAndBalanceBean.safeCardBeans.add (new SafeCardBean ());
//                    bankCardAndBalanceBean.safeCardBeans.add (new SafeCardBean ());
//                }
                getView ().queryListSafeBankSuccess (bankCardAndBalanceBean.safeCardBeans);
                getView ().queryBalanceSuccess (bankCardAndBalanceBean.balanceResp);

            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                getView ().queryListSafeBankError (code, msg);
            }
        });
        compositeDisposable.add (safeDispose);

    }


    public void personWithdraw(String memberNo, long withdrawAmt, String password,long originWithdraw){
        getView ().showLoading ("");
       Disposable withDrawDispose= RechargeWithDrawModel.personWithdraw (memberNo,withdrawAmt,password).subscribe (new Consumer<WithdrawResp> () {
            @Override
            public void accept(WithdrawResp resp) {
                getView ().dismissLoading ();
                //显示还是需要 原始的金额
                resp.withdrawAmt=originWithdraw;
                getView ().withdrawSuccess (resp,originWithdraw);
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                getView ().dismissLoading ();
                getView ().withdrawError (code,msg);
            }
        });
        compositeDisposable.add (withDrawDispose);
    }


    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
