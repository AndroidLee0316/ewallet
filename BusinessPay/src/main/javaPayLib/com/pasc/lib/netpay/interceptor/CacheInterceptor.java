package com.pasc.lib.netpay.interceptor;

import android.content.Context;
import com.pasc.lib.netpay.Utils;
import java.io.IOException;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 缓存数据,支持离线, 由于Single只能发送返回值或者错误两种情况，无法同时发送返回值和错误，这种方案不可取。
 */
public class CacheInterceptor implements Interceptor {

  private Context context;

  public CacheInterceptor(Context context) {
    this.context = context;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    if (Utils.isNetworkConnected(context)) {
      request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();
    } else {
      request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
    }
    return chain.proceed(request);
  }
}
