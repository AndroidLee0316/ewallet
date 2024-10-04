package com.pasc.business.ewallet.business.common;

import com.pasc.business.ewallet.BuildConfig;
import com.pasc.business.ewallet.NotProguard;

/**
 * @date 2019-11-06
 * @des
 * @modify
 **/
@NotProguard
public class CommonUrl {
    public static final String TEST_ENV_HOST_URL = BuildConfig.TEST_ENV_HOST_URL;
    public static final String STG2_ENV_HOST_URL = BuildConfig.STG2_ENV_HOST_URL;
    public static final String RELEASE_ENV_HOST_URL = BuildConfig.RELEASE_ENV_HOST_URL;

    //host,后面配置文件会覆盖
    public static String HOST_URL = RELEASE_ENV_HOST_URL;
    public final static String URL_PREFIX = "api";

    public static String getHostAndGateWay(){
        return HOST_URL;
    }
}
