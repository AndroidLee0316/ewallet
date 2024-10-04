package com.pasc.business.ewallet.business.home.net;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class QueryBalanceParam {
    @SerializedName ("memberNo")
    public String memberNo; //会员号

    @SerializedName ("memberType")
    public String memberType="1"; //会员类型：1-会员  2-商户


}
