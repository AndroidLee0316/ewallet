package com.pasc.ewallet.dev.pay.snpay;

import android.app.Activity;
import android.text.TextUtils;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.snpay.sdk.app.PayResultStatus;
import com.snpay.sdk.app.PayTask;
import com.snpay.sdk.model.PayResult;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 重要说明：
 * <p>
 * 本 Demo 只是为了方便直接向商户展示苏宁支付的整个支付流程，所以将加签过程直接放在客户端完成
 * （包括 OrderInfoUtil2_0_HK 和 OrderInfoUtil2_0）。
 * <p>
 * 在真实 App 中，私钥（如 RSA_PRIVATE 等）数据严禁放在客户端，同时加签过程务必要放在服务端完成，
 * 否则可能造成商户私密数据泄露或被盗用，造成不必要的资金损失，面临各种安全风险。
 * <p>
 * Warning:
 * <p>
 * For demonstration purpose, the assembling and signing of the request parameters are done on
 * the client side in this demo application.
 * <p>
 * However, in practice, both assembling and signing must be carried out on the server side.
 */
public class SNPayUtil {

    /**
     * 用于苏宁支付业务的入参 app_id。
     */
    public static final String APPID = "";

    /**
     * 用于苏宁账户登录授权业务的入参 pid。
     */
    public static final String PID = "";

    /**
     * 用于苏宁账户登录授权业务的入参 target_id。
     */
    public static final String TARGET_ID = "";

    public static void snPay(Activity activity, String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask snpay = new PayTask(activity);
                //建议使用loading aidl 首次连接比较耗时
                PayResult result = snpay.pay(orderInfo, true);
                //Log.i("msp", result + "");
                //Message msg = new Message();
                //msg.what = 0;
                //msg.obj = result;
                //mHandler.sendMessage(msg);
            }
        };
        //必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 苏宁支付业务示例
     */
    public static Disposable payV2(Activity activity, String orderInfo, SNPayListener snPayListener) {
        /*
         * 这里只是为了方便直接向商户展示苏宁的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo 的获取必须来自服务端；
         */

        return Single.create (new SingleOnSubscribe<PayResult> () {
            @Override
            public void subscribe(SingleEmitter<PayResult> emitter) {
                PayTask payTask = new PayTask (activity);
                PayResult payResult = payTask.pay(orderInfo, true);
                emitter.onSuccess (payResult);
            }
        }).subscribeOn (Schedulers.io ())
            .observeOn (AndroidSchedulers.mainThread ())
            .subscribe (new Consumer<PayResult> () {
                @Override
                public void accept(PayResult payResult) {
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult ();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus ();
                    // 判断resultStatus 为1001则代表支付成功
                    boolean isSuccess;
                    if (TextUtils.equals(resultStatus, PayResultStatus.REQ_SUCCESS_CODE.getText())) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        isSuccess = true;
                        //跳转到结果页 按商户业务需求 是否需要展示支付结果页
                        if (snPayListener != null) {
                            snPayListener.snPaySuccess ("苏宁支付成功");
                        }
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if (snPayListener != null) {
                            snPayListener.snPayError (Util.isEmpty(resultInfo) ? "苏宁支付失败" : resultInfo,true);
                        }
                    }
                }
            }, new BaseRespThrowableObserver () {
                @Override
                public void accept(Throwable throwable) {
                    if (snPayListener != null) {
                        snPayListener.snPayError ("苏宁支付拉起失败",false);
                    }
                }
            });
    }

}
