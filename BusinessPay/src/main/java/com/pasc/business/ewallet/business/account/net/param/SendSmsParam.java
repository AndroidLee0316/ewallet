package com.pasc.business.ewallet.business.account.net.param;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.business.common.param.SceneParam;

/**
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class SendSmsParam extends SceneParam{
    @SerializedName ("memberNo")
    public String memberNo; //会员号

}
