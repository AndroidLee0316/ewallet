package com.pasc.business.ewallet.business.pay.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.alipay.sdk.app.OpenAuthTask;
import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.pay.model.PayModel;
import com.pasc.business.ewallet.business.pay.net.resp.ApplySignResp;
import com.pasc.business.ewallet.business.pay.view.ApplySignView;
import com.pasc.business.ewallet.business.pay.wechat.WechatPayUtil;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2019/7/25
 * @des
 * @modify
 **/
public class ApplySignPresenter extends EwalletBasePresenter<ApplySignView> {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable ();

    /**
     * 申请签约
     * 获取预签约id（preEntrustwebId）
     *
     * @param memberNo 钱包会员号
     * @param channel 代扣方式 WECHATPA-微信、ALIPAYPA-支付宝
     * @param sceneId 场景号
     */
    public void applySign(String merchantNo, String memberNo, String channel, String sceneId) {
        getView ().showLoading ("");
        Disposable disposable = PayModel.applySign (merchantNo, memberNo, channel, sceneId)
            .subscribe (new Consumer<ApplySignResp> () {
            @Override
            public void accept(ApplySignResp resp) {
                getView().applySignSuccess(resp);
                getView().dismissLoading();
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                getView().applySignError(code, msg);
                getView().dismissLoading ();
            }
        });
        compositeDisposable.add (disposable);
    }

    public void callaliPaySign(Context context, String preEntrustwebId, String scheme) {
        final OpenAuthTask.Callback openAuthCallback = new OpenAuthTask.Callback() {
            @Override
            public void onResult(int resultCode, String memo, Bundle bundle) {
                if (resultCode == OpenAuthTask.OK) {// 对业务完成的结果做后续处理

                } else {// 对业务失败的结果做后续处理

                }
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("sign_params", preEntrustwebId);
        OpenAuthTask openAuthTask = new OpenAuthTask((Activity) context);
        openAuthTask.execute(scheme, OpenAuthTask.BizType.Deduct, params, openAuthCallback, true);
    }

    public void callweChatSign(Context context, String preEntrustwebId) {
        boolean isSuccess = false;
        try {
            isSuccess = WechatPayUtil.sign(context, preEntrustwebId);
        } catch (Throwable e) {
            e.printStackTrace ();
        }
        if (isSuccess) {
            getView().weChatLauncherSuccess ("微信签约拉起成功");
        } else {
            getView().weChatLauncherError ("微信签约拉起失败");

        }
    }
}
