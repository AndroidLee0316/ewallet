package com.pasc.lib.netpay.interceptor;

import com.pasc.lib.netpay.HttpCommonParams;
import com.pasc.lib.netpay.converter.PascRequestBody;
import com.pasc.lib.sm.SM3Digest;

import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yangwen881 on 17/2/24.
 */

public class HttpRequestInterceptor implements Interceptor {

    private HttpRequestInterceptor() {
    }

    private static class SingletonHolder {
        private static final HttpRequestInterceptor INSTANCE = new HttpRequestInterceptor ();
    }

    public static HttpRequestInterceptor getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //add some common parameters
        Request originalRequest = chain.request ();
        HttpUrl httpUrl = originalRequest.url ();
        HttpUrl.Builder requestBuilder = httpUrl.newBuilder ();
//        String url = httpUrl.url ().toString ();
//        // http://127.0.0.1/user/login?user=yy&userPassword=12345
//        String scheme = httpUrl.scheme ();//  http https
//        String host = httpUrl.host ();//   127.0.0.1
//        String path = httpUrl.encodedPath ();//  /user/login
//        String query = httpUrl.encodedQuery ();//  user=yy&userPassword=12345
//
//        if (Util.isEmpty (UrlManager.URL_PREFIX)&& !Util.isEmpty (CommonUrl.URL_PREFIX_DEV)) {
//            if (!url.contains (UrlManager.URL_PREFIX_DEV)) {
//                requestBuilder.scheme (scheme).host (host).encodedPath (CommonUrl.URL_PREFIX_DEV+path);
//                if (!Util.isEmpty (query)){
//                    requestBuilder.encodedQuery (query);
//                }
//
////                Set<String> queryList = httpUrl.queryParameterNames ();
////                int size = queryList.size ();
////                if (size > 0) {
////                    sb.append ("?");
////                    Iterator<String> iterator = queryList.iterator ();
////                    for (int i = 0; i < size; i++) {
////                        String key = iterator.next ();
////                        sb.append (key).append ("=");
////                        String value = httpUrl.queryParameter (key);
////                        sb.append (value);
////                        if (iterator.hasNext ()) {
////                            sb.append ("&");
////                        }
////                    }
////                }
//
//            }
//        }

        if (HttpCommonParams.getInstance ().getParams () != null) {
            for (Map.Entry<String, String> entry : HttpCommonParams.getInstance ().getParams ().entrySet ()) {
                requestBuilder.addQueryParameter (entry.getKey (), entry.getValue ());
            }
        }


        //add headers
        Request.Builder builder = chain.request ().newBuilder ();
        //针对 json上传形式 添加验签
        if (originalRequest.body () != null && originalRequest.body () instanceof PascRequestBody) {

//            现在header里面添加
//            x-api-sign   签名  String
//            x-api-timestamp  时间戳 String
//            x-sdk-version    sdk版本  String   放初始化里面获取
//            x-auth-channel    渠道号  String   放初始化里面获取

            PascRequestBody requestBody = (PascRequestBody) originalRequest.body ();
            long time = System.currentTimeMillis ();
            builder.header ("x-api-sign", SM3Digest.d (requestBody.jsonValue + time));
            builder.header ("x-api-timestamp", time + "");
        }

        if (HttpCommonParams.getInstance ().getHeaders () != null) {
            for (Map.Entry<String, String> entry : HttpCommonParams.getInstance ().getHeaders ().entrySet ()) {
                builder.addHeader (entry.getKey (), entry.getValue ());
            }
        }
        builder.url (requestBuilder.build ());
        return chain.proceed (builder.build ());
    }
}
