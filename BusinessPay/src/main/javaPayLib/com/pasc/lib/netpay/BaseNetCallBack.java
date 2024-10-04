package com.pasc.lib.netpay;

/**
 * 基础网络请求回调，空实现
 * Created by zhangcan603 on 2017/12/20.
 */

public abstract class BaseNetCallBack<K, T> {
    public void onSuccess(K k) {
    }

    public void onError(T t) {
    }
}
