package com.pasc.lib.netpay.resp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yelongfei490 on 16/11/22.
 */
public class BaseResp<T> {

    @SerializedName("code") public int code;

    @SerializedName("errorData") public String errorData;


    @SerializedName("msg") public String msg;

    @SerializedName("data") public T data;

    /**
     * token是否失效
     */
    public boolean isInvalidToken() {
        return code == 103;
    }

    @Override public String toString() {
        return "BaseResp{" + "code=" + code + ", errorData='" + errorData + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }
}
