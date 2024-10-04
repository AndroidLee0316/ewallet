package com.pasc.business.ewallet.business.pay.net.resp;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.common.utils.Util;

import java.util.List;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
public class PayContextResp {

    @SerializedName ("list")
    public List<PayTypeBean> list;

    @SerializedName ("orderAmount")
    public long orderAmount; //订单金额，单位：分

    @SerializedName ("merchantName")
    public String merchantName; //商户名称

    @SerializedName ("discountContext")
    public DiscountContextBean discountContext;//商户优惠信息对象

    public String getPayFee(){
        return Util.doublePoint (orderAmount,2);
    }

    //用户是否实名认证标识，true已实名，false未实名
    @SerializedName ("userAuthFlag")
    public boolean userAuthFlag=false;

    @Override
    public String toString() {
        return "PayContextBean{" +
                "list=" + list +
                ", orderAmount=" + orderAmount +
                ", merchantName='" + merchantName + '\'' +
                ", discountContext=" + discountContext +
                '}';
    }
}
