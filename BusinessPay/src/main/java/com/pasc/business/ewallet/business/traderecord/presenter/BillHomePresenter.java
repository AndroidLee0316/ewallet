package com.pasc.business.ewallet.business.traderecord.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.traderecord.net.resp.BillTypeResp;
import com.pasc.business.ewallet.business.traderecord.net.TradeApi;
import com.pasc.business.ewallet.business.traderecord.view.BillHomeView;
import com.pasc.business.ewallet.business.common.RespTransformer;
import com.pasc.lib.netpay.ApiGenerator;
import com.pasc.lib.netpay.ApiV2Error;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.netpay.resp.BaseV2Resp;
import com.pasc.lib.netpay.resp.VoidObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @date 2019/7/10
 * @des
 * @modify
 **/
public class BillHomePresenter extends EwalletBasePresenter<BillHomeView> {
    private final CompositeDisposable disposables=new CompositeDisposable ();
    public void getPayTypeList(final boolean needShow) {
        if (1==1){
            return;
        }
        if (needShow) {
            getView ().showLoading ("");
        }
        RespTransformer<BillTypeResp> transformer = new RespTransformer<BillTypeResp> () {
            @Override
            public boolean interceptReturnData(BaseV2Resp<BillTypeResp> baseResp) {
                if (baseResp.data != null && baseResp.data.payBillTypeList == null) {
                    throw new ApiV2Error (baseResp.code, baseResp.msg);
                }
                return true;
            }
        };
        String url= "http://tcs/pay/getPayBillTypeList";
        Disposable disposable= ApiGenerator.createApi (TradeApi.class).getPayBillTypeList (url,VoidObject.getInstance ())
                .compose (transformer)
                .subscribeOn (Schedulers.io ()).observeOn (AndroidSchedulers.mainThread ()).subscribe (new Consumer<BillTypeResp> () {
            @Override
            public void accept(BillTypeResp billTypeResponse) {
                if (needShow) {
                    getView ().hideLoading ();
                }
                getView ().getPayTypeListSuccess (billTypeResponse.payBillTypeList, needShow);
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String errorCode, String errorMsg) {
                if (needShow) {
                    getView ().hideLoading ();
                }

                getView ().getPayTypeListError (errorCode, errorMsg);
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
