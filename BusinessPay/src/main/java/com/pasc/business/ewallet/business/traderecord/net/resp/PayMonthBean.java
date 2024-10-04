package com.pasc.business.ewallet.business.traderecord.net.resp;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.common.utils.Util;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
public class PayMonthBean {
    @SerializedName ("yearOfMonth")
    public String yearOfMonth; //年月,例：2019-01
    @SerializedName ("amount")
    public long amount; //支出总金额
    @SerializedName("orderCount")
    public int orderCount;
    public String getFoutqty() {
        return orderCount+"";
    }
    public String getTotalOutlayStr() {
        return Util.doublePoint (amount,2);

    }

    public double getTotalOutlay(){
        double aa = amount / 100.0;
        return aa;
    }

}
