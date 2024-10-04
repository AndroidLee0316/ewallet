package com.pasc.lib.netpay.resp;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.NotProguard;

/**
 * 后台 code 改成了 string
 */
@NotProguard
public class BaseV2Resp<T> {

    @SerializedName("code") public String code;

    @SerializedName("message") public String msg;

    @SerializedName("data") public T data;

    @Override public String toString() {
        return "BaseResp{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }
}
