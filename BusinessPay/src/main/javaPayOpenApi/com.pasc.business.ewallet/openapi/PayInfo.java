package com.pasc.business.ewallet.openapi;

import com.pasc.business.ewallet.NotProguard;

import java.io.Serializable;


/**
 * 发起支付对象
 * Created by qinguohuai on 2019/2/26.
 */
@NotProguard
public class PayInfo implements Serializable {

    private String price;
    private String orderNo;

    public PayInfo() {
    }

    public PayInfo(String price, String orderNo) {
        this.price = price;
        this.orderNo = orderNo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
