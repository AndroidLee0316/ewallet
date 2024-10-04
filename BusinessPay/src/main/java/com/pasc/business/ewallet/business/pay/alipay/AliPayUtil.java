package com.pasc.business.ewallet.business.pay.alipay;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.pasc.business.ewallet.business.pay.alipay.util.OrderInfoUtil2_0;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.lib.netpay.ApiV2Error;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import java.util.Map;
import java.util.concurrent.TimeUnit;

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
 * 本 Demo 只是为了方便直接向商户展示支付宝的整个支付流程，所以将加签过程直接放在客户端完成
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
public class AliPayUtil {

    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    public static final String APPID = "";

    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    public static final String PID = "";

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。
     */
    public static final String TARGET_ID = "";

    /**
     * pkcs8 格式的商户私钥。
     * <p>
     * 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * RSA2_PRIVATE。
     * <p>
     * 建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE = "";

    public static String orderInfo() {
        if (Util.isEmpty (RSA2_PRIVATE) || (Util.isEmpty (RSA2_PRIVATE) && Util.isEmpty (RSA_PRIVATE))) {
            throw new ApiV2Error ("-1", "支付失败,支付宝appId和私钥不能为空");
        }
        boolean rsa2 = (RSA2_PRIVATE.length () > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap (APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam (params);
        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign (params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;
        return orderInfo;
    }

    /**
     * 支付宝支付业务示例
     */
    public static Disposable payV2(Activity activity, String orderInfo, AliPayListener aliPayListener) {
        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo 的获取必须来自服务端；
         */

        return Single.create (new SingleOnSubscribe<PayResult> () {
            @Override
            public void subscribe(SingleEmitter<PayResult> emitter) {
                PayTask alipay = new PayTask (activity);
                Map<String, String> result = alipay.payV2 (orderInfo, true);
                PayResult payResult = new PayResult (result);
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
                        String memo=payResult.getMemo ();
                        String msg=Util.isEmpty (resultInfo)?memo:resultInfo;
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals (resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            // 支付成功
                            if (aliPayListener != null) {
                                aliPayListener.aliPaySuccess ("支付宝支付成功");
                            }
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            // 支付失败
                            if (aliPayListener != null) {
                                aliPayListener.aliPayError (Util.isEmpty (msg)?"支付宝支付失败":msg,true);
                            }
                        }
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void accept(Throwable throwable) {
                        if (aliPayListener != null) {
                            aliPayListener.aliPayError ("支付宝支付拉起失败",false);
                        }
                    }
                });
    }

    /**
     * 支付宝账户授权业务示例
     */
    public static Disposable authV2(Activity activity) {
        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo 的获取必须来自服务端；
         */
        return Single.create (new SingleOnSubscribe<AuthResult> () {
            @Override
            public void subscribe(SingleEmitter<AuthResult> emitter) {
                if (TextUtils.isEmpty (PID) || TextUtils.isEmpty (APPID)
                        || (TextUtils.isEmpty (RSA2_PRIVATE) && TextUtils.isEmpty (RSA_PRIVATE))
                        || TextUtils.isEmpty (TARGET_ID)) {
                    return;
                }
                boolean rsa2 = (RSA2_PRIVATE.length () > 0);
                Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap (PID, APPID, TARGET_ID, rsa2);
                String info = OrderInfoUtil2_0.buildOrderParam (authInfoMap);

                String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
                String sign = OrderInfoUtil2_0.getSign (authInfoMap, privateKey, rsa2);
                final String authInfo = info + "&" + sign;
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask (activity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2 (authInfo, true);
                AuthResult authResult = new AuthResult (result, true);
                emitter.onSuccess (authResult);
            }
        }).subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ())
                .subscribe (new Consumer<AuthResult> () {
                    @Override
                    public void accept(AuthResult authResult) {
                        String resultStatus = authResult.getResultStatus ();
                        // 判断resultStatus 为“9000”且result_code
                        // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                        if (TextUtils.equals (resultStatus, "9000") && TextUtils.equals (authResult.getResultCode (), "200")) {
                            // 获取alipay_open_id，调支付时作为参数extern_token 的value
                            // 传入，则支付账户为该授权账户
                        } else {
                            // 其他状态值则为授权失败
                        }
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void accept(Throwable throwable) {
                    }
                });
    }

}
