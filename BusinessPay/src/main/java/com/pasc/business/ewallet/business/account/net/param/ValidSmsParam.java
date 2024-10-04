package com.pasc.business.ewallet.business.account.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class ValidSmsParam extends SendSmsParam {
    @SerializedName("smsCode")
    public String smsCode; //短信验证码
}
