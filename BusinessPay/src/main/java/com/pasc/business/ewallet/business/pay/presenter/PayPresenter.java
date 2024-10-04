package com.pasc.business.ewallet.business.pay.presenter;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.bankcard.model.CardModel;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickPaySendMsgResp;
import com.pasc.business.ewallet.business.pay.model.PayModel;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.business.pay.view.PayView;
import com.pasc.business.ewallet.business.pwd.model.PwdModel;
import com.pasc.business.ewallet.business.pwd.net.resp.ValidateCodeResp;
import com.pasc.business.ewallet.business.rechargewithdraw.model.RechargeWithDrawModel;
import com.pasc.business.ewallet.inner.PayManager;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @date 2019/8/17
 * @des
 * @modify
 **/
public class PayPresenter extends EwalletBasePresenter<PayView> {
    CompositeDisposable compositeDisposable=new CompositeDisposable ();

    public void pay(String mchOrderNo, String memberNo, String orderType, String payPwd) {
        String tip= PayManager.getInstance ().getApplication ().getString (R.string.ewallet_pay_waiting_tip);
        getView ().showLoading (tip);
        Disposable  disposable = PayModel.pay (mchOrderNo, memberNo, orderType, payPwd).subscribe (new Consumer<PayResp> () {
            @Override
            public void accept(PayResp payResp) {
                getView ().dismissLoading ();
                getView ().paySuccess (orderType, payResp);
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                getView ().dismissLoading ();
                getView ().payError (orderType, code, msg);

            }
        });
        compositeDisposable.add (disposable);

    }

    public  void payQuickCard(String mchOrderNo, String memberNo, String orderType,
                                               String unionOrderId, String paydate, String cardKey, String unionVerifyCode){
        String tip=PayManager.getInstance ().getApplication ().getString (R.string.ewallet_pay_waiting_tip);
        getView ().showLoading (tip);
        Disposable  disposable = PayModel.payQuickCard (mchOrderNo, memberNo, orderType, unionOrderId,
                paydate,cardKey,unionVerifyCode
        ).subscribe (new Consumer<PayResp> () {
            @Override
            public void accept(PayResp payResp) {
                getView ().paySuccess (orderType, payResp);
                //不需要隐藏对话框，后面还需要查询订单，因为查询订单 也需要弹loading,由查询订单去关闭loading
                /****{@link com.pasc.business.ewallet.business.pay.fragment.PaySendVCodeFragment#paySuccess(String, PayResp)}*****/
                //如果dismiss 接着马上 loading 会感觉一闪
//                getView ().dismissLoading ();
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                getView ().dismissLoading ();
                getView ().payError (orderType, code, msg);

            }
        });
        compositeDisposable.add (disposable);
    }
    /*****充值******/
    public void recharge(String memberNo, String payType, long amount, String mchOrderNo) {
        String tip=PayManager.getInstance ().getApplication ().getString (R.string.ewallet_pay_waiting_tip);
        getView ().showLoading (tip);
      Disposable  rechargeDisposable=RechargeWithDrawModel.recharge (memberNo, payType, amount, mchOrderNo)
                .subscribe (new Consumer<PayResp> () {
                    @Override
                    public void accept(PayResp payResp) {
                        getView ().dismissLoading ();
                        getView ().paySuccess (payType, payResp);
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        getView ().dismissLoading ();
                        getView ().payError (payType, code, msg);
                    }
                });
        compositeDisposable.add (rechargeDisposable);
    }

    public void rechargeQuickCard(String memberNo, String payType, long amount,String unionOrderId, String paydate, String cardKey, String unionVerifyCode) {
        String tip=PayManager.getInstance ().getApplication ().getString (R.string.ewallet_pay_waiting_tip);
        getView ().showLoading (tip);
        Disposable  rechargeDisposable= RechargeWithDrawModel.rechargeQuickCard (memberNo, payType, amount,unionOrderId,paydate,cardKey,unionVerifyCode)
                .subscribe (new Consumer<PayResp> () {
                    @Override
                    public void accept(PayResp payResp) {
                        getView ().paySuccess (payType, payResp);
                        //不需要隐藏对话框，后面还需要查询订单，因为查询订单 也需要弹loading ，由查询订单去关闭loading
                        /****{@link com.pasc.business.ewallet.business.pay.fragment.PaySendVCodeFragment#paySuccess(String, PayResp)}*****/
                        //如果dismiss 接着马上 loading 会感觉一闪
//                getView ().dismissLoading ();
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        getView ().dismissLoading ();
                        getView ().payError (payType, code, msg);
                    }
                });
        compositeDisposable.add (rechargeDisposable);
    }

    public void validPwdAndSendMsgCode(String pwd,String cardKey, String mchOrderNo, String tradeType, long amount){
        getView ().showLoading ("");
        compositeDisposable.add (PwdModel.verifyPassword (pwd)
                .observeOn (Schedulers.io ())
                .flatMap (new Function<ValidateCodeResp, SingleSource<QuickPaySendMsgResp>> () {
                    @Override
                    public SingleSource<QuickPaySendMsgResp> apply(ValidateCodeResp validateCodeResp) throws Exception {
                        return CardModel.quickPaySendMsg (cardKey,mchOrderNo,tradeType,amount);
                    }
                }).observeOn (AndroidSchedulers.mainThread ())
                .subscribe (new Consumer<QuickPaySendMsgResp> () {
                    @Override
                    public void accept(QuickPaySendMsgResp quickPaySendMsgResp) throws Exception {
                        getView ().dismissLoading ();
                        getView ().validPwdAndSendMsgCodeSuccess (quickPaySendMsgResp);
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        getView ().dismissLoading ();
                        getView ().validPwdAndSendMsgCodeError (code,msg);
                    }
                }));


    }



    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
