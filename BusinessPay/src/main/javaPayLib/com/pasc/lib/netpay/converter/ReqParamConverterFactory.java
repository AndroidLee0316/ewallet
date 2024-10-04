package com.pasc.lib.netpay.converter;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 请求参数转化器，将非基本、void、String的类型参数转成json字符串
 *
 * Created by duyuan797 on 2017/3/23.
 */
public class ReqParamConverterFactory extends Converter.Factory {
  private final Gson gson;

  public ReqParamConverterFactory(Gson gson) {
    this.gson = gson;
  }

  @Override public Converter<?, String> stringConverter(Type type, Annotation[] annotations,
          Retrofit retrofit) {
    // 若type是Class类型（判断是Class类型是因为Map类型无法转成Class类型，强转会抛异常）且是基本类型、Void或String类型，则返回null交由默认转换器处理
    if (type instanceof Class) {
      Class clazz = (Class) type;
      if (clazz.isPrimitive() || clazz == Void.TYPE || clazz == String.class) {
        return null;
      }
    }
    return new ReqParamConverter(gson);
  }

  private static class ReqParamConverter implements Converter<Object, String> {

    private final Gson gson;

    public ReqParamConverter(Gson gson) {
      this.gson = gson;
    }

    @Override public String convert(Object value) {
      String jsonData = gson.toJson(value);
      //jsonData = getEncryptJsonData(jsonData);//加密算法没统一 暂时移除
      return jsonData;
    }
  }

}
