package com.pasc.lib.netpay.transform;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.pasc.business.ewallet.NotProguard;
import com.pasc.lib.netpay.ApiV2Error;
import com.pasc.lib.netpay.ExceptionHandler;
import com.pasc.lib.netpay.NetManager;
import com.pasc.lib.netpay.resp.BaseV2Resp;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Resp数据转换器
 * <p>/
 * Created by duyuan797 on 16/11/22.
 */
@NotProguard
public class RespV2Transformer<T> implements SingleTransformer<BaseV2Resp<T>, T> {
    @Override
    public SingleSource<T> apply(Single<BaseV2Resp<T>> upstream) {
        return upstream.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) {
                NetManager netManager = NetManager.getInstance();
                if (!isNetworkAvailable(netManager.globalConfig.context)) {
                    throw new ApiV2Error("-1", ExceptionHandler.CONNECT_EXCEPTION);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<BaseV2Resp<T>, SingleSource<? extends T>>() {
                    @Override
                    public SingleSource<? extends T> apply(@NonNull BaseV2Resp<T> baseResp) {
                        String code = baseResp.code;
                        if ("200".equals(code)) {
                            T t = baseResp.data;
                            return Single.just(t);
                        } else {
                            // 由用户模块去判断token 的东西
                            NetV2ObserverManager.getInstance().notifyObserver(baseResp);
                            //throw new ApiV2Error(code, ExceptionHandler.convertMsg (code,baseResp.msg));
                            throw new ApiV2Error(code, baseResp.msg);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


    public RespV2Transformer() {
    }

    public static <R> RespV2Transformer<R> newInstance() {
        return new RespV2Transformer<>();
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
