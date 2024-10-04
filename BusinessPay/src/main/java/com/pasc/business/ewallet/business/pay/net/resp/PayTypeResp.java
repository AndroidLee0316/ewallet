package com.pasc.business.ewallet.business.pay.net.resp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
public class PayTypeResp {
    @SerializedName("list")
    public List<PayTypeBean> list;
}
