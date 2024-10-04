package com.pasc.business.ewallet.business.traderecord.net.resp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
public class PayMonthResp {
    @SerializedName ("list")
    public List<PayMonthBean> list;
}
