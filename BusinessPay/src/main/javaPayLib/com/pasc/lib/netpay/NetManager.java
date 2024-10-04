package com.pasc.lib.netpay;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.pasc.lib.netpay.interceptor.HttpLoggingInterceptor;
import com.pasc.lib.netpay.interceptor.HttpRequestInterceptor;
import com.pasc.lib.netpay.resp.BaseV2Resp;
import com.pasc.lib.netpay.resp.VoidObject;
import com.pasc.lib.netpay.servicecreator.RetrofitServiceCreatorFactory;
import com.pasc.lib.netpay.servicecreator.ServiceCreatorFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * Created by huanglihou519 on 2018/9/2.
 */
public final class NetManager {
    public static final String TAG = "pascPayNet";
    private static ValueFactory<NetManager> factory;

    private static final int DISK_CACHE_SIZE = 25 * 1024 * 1024;//最大缓存25M
    public OkHttpClient httpClient = null;
    public final NetConfig globalConfig;
    public final static Gson assistGson = new Gson ();
    private HashMap<String, ServiceCreator> urlToCreator;
    private ServiceCreatorFactory serviceCreatorFactory;
    public static void init(NetConfig config) {
        factory = ValueFactory.of (new NetManager (config));
    }

    public static NetManager getInstance() {
        if (factory.get () == null) throw new AssertionError ("必须先调用 NetManager.init() 初始化");
        return factory.get ();
    }

    private NetManager(NetConfig config) {
        this.globalConfig = config;
        this.urlToCreator = new HashMap<> ();
        if (this.globalConfig.headers != null) {
            HttpCommonParams.getInstance ().addHeaders (this.globalConfig.headers);
        }
        initGson ();
        initHttpClient ();
        this.serviceCreatorFactory = new RetrofitServiceCreatorFactory (this);
        initServiceCreator ();
    }

    private void initGson() {
        if (globalConfig.gson == null) {
            // 自定义Gson配置，处理接口List数据不传时，换成emptyList
            globalConfig.gson = getConvertGson ();
        }
    }

    public static Gson getConvertGson() {
        Gson gson = new GsonBuilder ()
                .registerTypeAdapter (BaseV2Resp.class, new JsonDeserializer<BaseV2Resp> () {
                    @Override
                    public BaseV2Resp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        BaseV2Resp baseResp;
                        //主类型
                        Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments ()[0];
                        //泛型
                        Class typeArgument = itemType instanceof Class ? (Class) itemType : (Class) ((ParameterizedType) itemType).getRawType ();
                        if (typeArgument == Void.class) {
                            // 泛型格式如下： new JsonCallback<BaseResp<Void>>(this)
                            baseResp = assistGson.fromJson (json, typeOfT);
                            //noinspection unche return (T) baseResp;
                        } else if (typeArgument == VoidObject.class) {
                            BaseV2Resp tmp = assistGson.fromJson (json, BaseV2Resp.class);
                            baseResp = new BaseV2Resp<VoidObject> ();
                            baseResp.data = VoidObject.getInstance ();
                            baseResp.code = tmp.code;
                            baseResp.msg = tmp.msg;
                            //noinspection unche return (T) baseResp;
                        } else if (typeArgument == String.class) {
                            //需求 BaseResp<String>
                            BaseV2Resp tmp = assistGson.fromJson (json, BaseV2Resp.class);
                            if (tmp.data != null && tmp.data instanceof String) {
                                //data本身就是String
                                baseResp = tmp;
                            } else {
                                //data不是String
                                baseResp = new BaseV2Resp<String> ();
                                baseResp.data = assistGson.toJson (tmp.data);
                                baseResp.code = tmp.code;
                                baseResp.msg = tmp.msg;
                            }
                        } else {
                            // 泛型格式如下： new JsonCallback<BaseResp<内层JavaBean>>(this)
                            baseResp = assistGson.fromJson (json, typeOfT);

                        }
                        /*****data 为空时****/
                        if (baseResp.data == null) {
                            if (typeArgument == List.class) {
                                baseResp.data = Collections.EMPTY_LIST;
                            } else if (typeArgument == Set.class) {
                                baseResp.data = Collections.EMPTY_SET;
                            } else if (typeArgument == VoidObject.class) {
                                baseResp.data = VoidObject.getInstance ();
                            } else {
                                try {
                                    baseResp.data = typeArgument.newInstance ();
                                } catch (InstantiationException e) {
                                    e.printStackTrace ();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace ();
                                }
                            }
                        }
                        return baseResp;
                    }
                })
                .create ();

        return gson;
    }

    private void initHttpClient() {
        final File cacheDir = new File (globalConfig.context.getCacheDir (), "httpCache");
        Cache httpCache = new Cache (cacheDir, DISK_CACHE_SIZE);
        Dispatcher dispatcher = new Dispatcher (Executors.newScheduledThreadPool (3));
        final HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger () {
            @Override
            public void log(String message) {
                if (message.startsWith ("jsonData=")) {
                    try {
                        Log.d (TAG, URLDecoder.decode (message.replace ("jsonData=", ""),
                                "UTF-8"));
                    } catch (UnsupportedEncodingException ignore) {
                    }
                    return;
                }
                Log.d (TAG, message);
            }
        };
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor (logger);
        loggingInterceptor.setLevel (globalConfig.isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        OkHttpClient.Builder builder = new OkHttpClient.Builder ().connectTimeout (20, TimeUnit.SECONDS)
                .readTimeout (30, TimeUnit.SECONDS)
                .writeTimeout (30, TimeUnit.SECONDS)
                .cache (httpCache)
                .dispatcher (dispatcher)
                .addInterceptor (HttpRequestInterceptor.getInstance ());

        if (globalConfig.interceptor!=null){
            builder.addInterceptor (globalConfig.interceptor);
        }
        if (globalConfig.isDebug) {
            builder .addInterceptor (loggingInterceptor);
        }
        //禁止代理抓包
        if (!globalConfig.isDebug){
            builder.proxy(Proxy.NO_PROXY);

        }
        if (globalConfig.sslContext != null
                && globalConfig.trustManager != null
                && globalConfig.hostnameVerifier != null) {
            builder.sslSocketFactory (globalConfig.sslContext.getSocketFactory (),
                    globalConfig.trustManager).hostnameVerifier (globalConfig.hostnameVerifier);
        }
        if (globalConfig.certificatePinner != null) {
            builder.certificatePinner (globalConfig.certificatePinner);
        }
        httpClient = builder.build ();
    }

    private void initServiceCreator() {
        setServiceCreator (serviceCreatorFactory.get (this.globalConfig.baseUrl));
    }

    public void setServiceCreator(ServiceCreator serviceCreator) {
        this.urlToCreator.put (this.globalConfig.baseUrl, serviceCreator);
    }

    public void setServiceCreator(String url, ServiceCreator serviceCreator) {
        this.urlToCreator.put (url, serviceCreator);
    }

    public ServiceCreator serviceCreator() {
        return serviceCreator (this.globalConfig.baseUrl);
    }

    public ServiceCreator serviceCreator(String url) {
        ServiceCreator serviceCreator = this.urlToCreator.get (url);
        if (serviceCreator == null) {
            serviceCreator = serviceCreatorFactory.get (url);
            setServiceCreator (url, serviceCreator);
        }
        return serviceCreator;
    }
}
