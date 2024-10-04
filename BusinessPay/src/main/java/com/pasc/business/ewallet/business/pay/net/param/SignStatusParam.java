package com.pasc.business.ewallet.business.pay.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/12/15
 * @des
 * @modify
 **/
public class SignStatusParam {
    @SerializedName ("memberNo")
    public String memberNo; //钱包会员号
    @SerializedName ("channel")
    public String channel; //代扣方式 WECHATPA-微信、ALIPAYPA-支付宝
    @SerializedName ("sceneId")
    public String sceneId; //场景号

    public SignStatusParam(String memberNo, String channel, String sceneId) {
        this.memberNo = memberNo;
        this.channel = channel;
        this.sceneId = sceneId;
    }
}
