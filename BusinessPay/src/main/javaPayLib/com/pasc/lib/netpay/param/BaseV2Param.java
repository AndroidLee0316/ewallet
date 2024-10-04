package com.pasc.lib.netpay.param;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.NotProguard;

/**
 * Created by duyuan797 on 17/10/24.
 */
@NotProguard
public class BaseV2Param<T> {
    @SerializedName("sign") public String sign;
    @SerializedName("appId") public String appId;
    @SerializedName("message") public T message;

    public BaseV2Param(T message){
        this.message = message;
    }
}
