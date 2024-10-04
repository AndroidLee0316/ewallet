package com.pasc.business.ewallet.business.pay.net.resp;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/7
 * @des
 * @modify
 **/
public class DiscountContextBean {
    @SerializedName("desc")
    public String desc; //优惠描述，可为null
    @SerializedName ("title")
    public String title; //标题

}
