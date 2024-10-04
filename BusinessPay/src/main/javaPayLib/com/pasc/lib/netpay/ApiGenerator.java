package com.pasc.lib.netpay;

import com.pasc.business.ewallet.NotProguard;

/**
 * 接口生成器
 */
@NotProguard
public final class ApiGenerator {

  public static <S> S createApi(Class<S> apiClass) {
    return NetManager.getInstance().serviceCreator().create(apiClass);
  }

  public static <S> S createApi(String baseUrl, Class<S> apiClass) {
    return NetManager.getInstance().serviceCreator(baseUrl).create(apiClass);
  }
}
