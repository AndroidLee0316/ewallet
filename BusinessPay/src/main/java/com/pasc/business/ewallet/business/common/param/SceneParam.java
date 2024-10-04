package com.pasc.business.ewallet.business.common.param;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/16
 * @des
 * @modify
 **/
public class SceneParam {
    @SerializedName("scene")
    public String scene; //场景码；SET_PSW：设置支付密码 FORGET_PSW：忘记支付密码
}
