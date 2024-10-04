package com.pasc.business.ewallet.business.common;

import com.pasc.business.ewallet.config.Constants;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @author yangzijian
 * @date 2019/3/19
 * @des
 * @modify
 **/
public class SecureBiz {
    public static Single<String> publicKey() {
        return Single.create (new SingleOnSubscribe<String> () {
            @Override
            public void subscribe(SingleEmitter<String> emitter) {
                emitter.onSuccess (Constants.PUBLIC_KEY);
            }
        }).subscribeOn (Schedulers.io ());
    }
}
