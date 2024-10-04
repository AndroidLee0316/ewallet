package com.pasc.lib.netpay.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by duyuan797 on 17/10/24.
 */

public class BaseParam<T> {
    @SerializedName("accessUserId") public String userId;
    @SerializedName("data") public T data;

    public BaseParam(T data){
        this.data = data;
    }
}
