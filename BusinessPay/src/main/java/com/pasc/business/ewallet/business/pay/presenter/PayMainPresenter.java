package com.pasc.business.ewallet.business.pay.presenter;

import android.app.Activity;
import android.content.Context;
import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.pay.alipay.AliPayListener;
import com.pasc.business.ewallet.business.pay.alipay.AliPayUtil;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.business.pay.view.PayMainView;
import com.pasc.business.ewallet.business.pay.wechat.WechatPayUtil;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @date 2019/7/30
 * @des
 * @modify
 **/
public class PayMainPresenter extends EwalletBasePresenter<PayMainView> {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable ();

    public void aliPay(Activity activity, String orderInfo) {
        Disposable disposable = AliPayUtil.payV2 (activity, orderInfo, new AliPayListener () {
            @Override
            public void aliPaySuccess(String msg) {
                getView ().aliPaySuccess (msg);
            }

            @Override
            public void aliPayError(String msg, boolean isCancel) {
                getView ().aliPayError (msg, isCancel);

            }
        });
        compositeDisposable.add (disposable);
    }

    public void weChatPay(Context context, PayResp payBean) {
        boolean isSuccess = false;
        try {
            isSuccess = WechatPayUtil.pay (context, payBean);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        if (isSuccess) {
            getView ().weChatLauncherSuccess ("微信支付拉起成功");
        } else {
            getView ().weChatLauncherError ("微信支付拉起失败");

        }
    }



    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }

}
