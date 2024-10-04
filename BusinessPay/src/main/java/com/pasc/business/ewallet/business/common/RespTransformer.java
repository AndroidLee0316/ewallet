package com.pasc.business.ewallet.business.common;

import com.pasc.business.ewallet.NotProguard;
import com.pasc.lib.netpay.ApiV2Error;
import com.pasc.lib.netpay.ErrorCode;
import com.pasc.lib.netpay.ExceptionHandler;
import com.pasc.lib.netpay.resp.BaseV2Resp;
import com.pasc.lib.pay.common.util.NetworkUtils;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
@NotProguard
public class RespTransformer<T> implements SingleTransformer<BaseV2Resp<T>, T> {
    @Override
    public SingleSource<T> apply (Single<BaseV2Resp<T>> upstream) {
        return upstream.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept (@NonNull Disposable disposable) {
                if (!NetworkUtils.isNetworkAvailable()){
                    throw new ApiV2Error(ErrorCode.ERROR+"", ExceptionHandler.CONNECT_EXCEPTION);
                }
            }
        })
//                .subscribeOn(Schedulers.io())
                .flatMap(new Function<BaseV2Resp<T>, SingleSource<? extends T>>() {
                    @Override
                    public SingleSource<? extends T> apply (@NonNull BaseV2Resp<T> baseResp) {
                        interceptReturnData (baseResp);
                        String code = baseResp.code;
                        if ((ErrorCode.SUCCESS + "").equals(code)){
                            T t = baseResp.data;
                            return Single.just(t);
                        } else{
                            //throw new ApiV2Error(code, ExceptionHandler.convertMsg (code,baseResp.msg));
                            throw new ApiV2Error(code, baseResp.msg);
                        }
                    }
                });
//                .observeOn(AndroidSchedulers.mainThread());
    }


    public boolean interceptReturnData(@NonNull BaseV2Resp<T> baseResp){
        return false;
   }

    public RespTransformer () {
    }

    public static <R> RespTransformer<R> newInstance () {
        return new RespTransformer<>();
    }
}
