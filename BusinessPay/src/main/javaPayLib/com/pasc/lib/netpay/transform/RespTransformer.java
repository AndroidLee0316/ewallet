//package com.pasc.lib.netpay.transform;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Handler;
//import android.os.Looper;
//
//import com.pasc.lib.netpay.ApiError;
//import com.pasc.lib.netpay.ErrorCode;
//import com.pasc.lib.netpay.NetManager;
//import com.pasc.lib.netpay.resp.BaseResp;
//
//import io.reactivex.Single;
//import io.reactivex.SingleSource;
//import io.reactivex.SingleTransformer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.annotations.NonNull;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Function;
//import io.reactivex.schedulers.Schedulers;
//
//
///**
// * Resp数据转换器
// * <p>/
// * Created by duyuan797 on 16/11/22.
// */
//public class RespTransformer<T> implements SingleTransformer<BaseResp<T>, T> {
//    private Handler handler = new Handler(Looper.getMainLooper());
//
//    @Override
//    public SingleSource<T> apply(Single<BaseResp<T>> upstream) {
//        return upstream.doOnSubscribe(new Consumer<Disposable>() {
//            @Override
//            public void accept(@NonNull Disposable disposable) throws Exception {
//                NetManager netManager = NetManager.getInstance();
//                if (!isNetworkAvailable(netManager.globalConfig.context)) {
//                    throw new ApiError(ErrorCode.ERROR, "当前网络不佳，请稍后重试");
//                }
//            }
//        })
//                .subscribeOn(Schedulers.io())
//                .flatMap(new Function<BaseResp<T>, SingleSource<? extends T>>() {
//                    @Override
//                    public SingleSource<? extends T> apply(@NonNull BaseResp<T> baseResp)
//                            throws Exception {
//                        int code = baseResp.code;
//                        if (code == ErrorCode.SUCCESS) {
//                            T t = baseResp.data;
//                            return Single.just(t);
//                        } else {
//                            // 由用户模块去判断token 的东西
//                            NetObserverManager.getInstance().notifyObserver(baseResp);
//                            throw new ApiError(code, baseResp.msg);
//                        }
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//
//    private RespTransformer() {
//    }
//
//    public static <R> RespTransformer<R> newInstance() {
//        return new RespTransformer<>();
//    }
//
//    private static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//}
