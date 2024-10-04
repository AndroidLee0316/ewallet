package com.pasc.business.ewallet.business.traderecord.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.traderecord.net.resp.PayMonthResp;
import com.pasc.business.ewallet.business.traderecord.model.TradeRecordModel;
import com.pasc.business.ewallet.business.traderecord.net.param.PayMonthParam;
import com.pasc.business.ewallet.business.traderecord.view.BillFailRollBackView;
import com.pasc.business.ewallet.business.traderecord.view.BillMonthView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @date 2019/7/10
 * @des
 * @modify
 **/
public class BillMonthPresenter extends EwalletBasePresenter<BillMonthView> {
    private final CompositeDisposable disposables = new CompositeDisposable ();

    public void getMonthlyBills2(String memberNo, String startYearOfMonth,
                                 String endYearOfMonth
            , String preEndYearOfMonth) {
        getView ().showLoading ("");
        PayMonthParam payMonthParam = new PayMonthParam (memberNo, startYearOfMonth, endYearOfMonth);
        Disposable disposable = TradeRecordModel.getMonthlyBills (payMonthParam).subscribe (new Consumer<PayMonthResp> () {
            @Override
            public void accept(PayMonthResp payMonthResp) {
                if (payMonthResp.list == null) {
                    payMonthResp.list = new ArrayList<> ();
                }
                getView ().getMonthBillSuccess (payMonthResp.list);
                getView ().dismissLoading ();

                if (getView () instanceof BillFailRollBackView) {
                    ((BillFailRollBackView) getView ()).rollback ("", endYearOfMonth);
                }
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                getView ().dismissLoading ();
                getView ().getMonthBillError (code,msg);
                if (getView () instanceof BillFailRollBackView) {
                    ((BillFailRollBackView) getView ()).rollback ("", preEndYearOfMonth);
                }

            }
        });
        disposables.add (disposable);

    }

    @Override
    public void onMvpDestroyView() {
        disposables.clear ();
        super.onMvpDestroyView ();
    }
}
