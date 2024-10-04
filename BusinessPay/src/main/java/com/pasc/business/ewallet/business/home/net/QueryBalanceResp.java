package com.pasc.business.ewallet.business.home.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class QueryBalanceResp implements Serializable{

    /**
     * balance : -1.0978022957017109E7
     * withdrawAmt : ex incididunt
     */

    @SerializedName("balance")
    public long balance; //可用余额（单位：分）
    @SerializedName("withdrawAmt")
    public long withdrawAmt; //可提现金额（单位：分）

    public double getAvaBalance(){
        return (withdrawAmt) /100.0;
    }
}
