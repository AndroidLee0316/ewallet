package com.pasc.business.ewallet.business.traderecord.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
public class PayMonthParam {
    @SerializedName ("memberNo")
    public String memberNo; //会员号

    @SerializedName ("startYearOfMonth")
    public String startYearOfMonth; //年月 例：2019-01

    @SerializedName ("endYearOfMonth")
    public String endYearOfMonth; //年月 例：2019-01

    public PayMonthParam(String memberNo, String startYearOfMonth, String endYearOfMonth) {
        this.memberNo = memberNo;
        this.startYearOfMonth = startYearOfMonth;
        this.endYearOfMonth = endYearOfMonth;
    }
}
