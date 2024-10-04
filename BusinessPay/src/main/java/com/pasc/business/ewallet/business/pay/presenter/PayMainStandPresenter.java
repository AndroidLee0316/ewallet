package com.pasc.business.ewallet.business.pay.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.pay.model.PayModel;
import com.pasc.business.ewallet.business.pay.net.resp.PayContextResp;
import com.pasc.business.ewallet.business.pay.net.resp.PayTypeBean;
import com.pasc.business.ewallet.business.pay.net.resp.PayTypeResp;
import com.pasc.business.ewallet.business.pay.view.PayMainStandView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @date 2019/7/25
 * @des
 * @modify
 **/
public class PayMainStandPresenter extends EwalletBasePresenter<PayMainStandView> {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable ();

    /**
     * 获取收银台信息
     *
     * @param mchOrderNo
     */
    public void getPayContext(String merchantNo, String memberNo,String mchOrderNo,String payScene,boolean isRefresh) {
        getView ().showLoading ("");
        Disposable disposable = PayModel.getPayContext (merchantNo, memberNo,mchOrderNo,payScene).subscribe (new Consumer<PayContextResp> () {
            @Override
            public void accept(PayContextResp resp) {

                if (StatusTable.enableQuickCard){
                }else {
                    List<PayTypeBean> list=resp.list;
                    List<PayTypeBean> listNew=new ArrayList<> ();
                    if (list!=null){
                        for (PayTypeBean payTypeBean:list){
                            if (!StatusTable.PayType.UNIONQUICKPAY.equals (payTypeBean.payType)){
                                listNew.add (payTypeBean);
                            }
                        }
                    }
                    resp.list=listNew;
                }
                getView ().queryPayContextSuccess (resp,isRefresh);
                getView ().dismissLoading ();
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                getView ().queryPayContextError (code, msg,isRefresh);
                getView ().dismissLoading ();
            }
        });
        compositeDisposable.add (disposable);



    }

    public void queryPayList(long amount,String title,String scene){
        getView ().showLoading ("");
        Disposable disposable =   PayModel.payList (scene).subscribe (new Consumer<PayContextResp> () {
            @Override
            public void accept(PayContextResp resp) {
                resp.orderAmount=amount;
                resp.merchantName=title;
                getView ().queryPayContextSuccess (resp,false);
                getView ().dismissLoading ();
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                getView ().queryPayContextError (code, msg,false);
                getView ().dismissLoading ();
            }
        });
        compositeDisposable.add (disposable);

    }

    public void queryPayTypeList(long amount,String title,String memberNo){
        getView ().showLoading ("");
        Disposable disposable =   PayModel.queryPayTypeList (memberNo).subscribe (new Consumer<PayTypeResp> () {
            @Override
            public void accept(PayTypeResp resp) {
                 List<PayTypeBean> typeBeans=resp.list;
                PayContextResp payContextBean=new PayContextResp ();
                payContextBean.orderAmount=amount;
                payContextBean.merchantName=title;
                payContextBean.list=typeBeans;
                getView ().queryPayContextSuccess (payContextBean,false);
                getView ().dismissLoading ();
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                getView ().queryPayContextError (code, msg,false);
                getView ().dismissLoading ();
            }
        });
        compositeDisposable.add (disposable);

    }
}
