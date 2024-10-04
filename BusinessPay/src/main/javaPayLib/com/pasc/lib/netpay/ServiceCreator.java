package com.pasc.lib.netpay;

/**
 * Created by huanglihou519 on 2018/9/2.
 */
public interface ServiceCreator {
  <S> S create(Class<S> serviceClass);
}
