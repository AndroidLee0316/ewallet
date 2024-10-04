package com.pasc.lib.netpay.servicecreator;

import android.text.TextUtils;
import com.pasc.lib.netpay.NetManager;
import com.pasc.lib.netpay.ServiceCreator;
import com.pasc.lib.netpay.annotation.Xml;
import com.pasc.lib.netpay.converter.AnnotatedConverterFactory;
import com.pasc.lib.netpay.converter.EmptyToNullConverter;
import com.pasc.lib.netpay.converter.PascGsonConverterFactory;
import com.pasc.lib.netpay.converter.ReqParamConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by huanglihou519 on 2018/9/2.
 */
public class RetrofitServiceCreatorFactory implements ServiceCreatorFactory {

  private Retrofit defaultRetrofit;
  private final String baseUrl;

  @Override public ServiceCreator get(String url) {
    if (TextUtils.isEmpty(url) || baseUrl.equals(url)) {
      return new RetrofitServiceCreator(defaultRetrofit);
    }
    return new RetrofitServiceCreator(defaultRetrofit.newBuilder().baseUrl(url).build());
  }

  public RetrofitServiceCreatorFactory(NetManager netManager) {
    baseUrl = netManager.globalConfig.baseUrl;
    // addConverter 有顺序依赖关系
    defaultRetrofit = new Retrofit.Builder().baseUrl(baseUrl)
            .client(netManager.httpClient)
            .addConverterFactory(new EmptyToNullConverter ())
            .addConverterFactory(new AnnotatedConverterFactory.Builder ()
//                    .add(Xml.class, SimpleXmlConverterFactory.create())
                    .build())
            .addConverterFactory(new ReqParamConverterFactory (netManager.globalConfig.gson))
            .addConverterFactory(PascGsonConverterFactory.create(netManager.globalConfig.gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
  }

  private class RetrofitServiceCreator implements ServiceCreator {

    private final Retrofit creator;

    private RetrofitServiceCreator(Retrofit creator) {
      this.creator = creator;
    }

    @Override public <S> S create(Class<S> serviceClass) {
      return creator.create(serviceClass);
    }
  }
}
