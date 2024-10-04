package com.pasc.business.ewallet.business.pay.net.resp;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.business.StatusTable;

import java.io.Serializable;

/**
 * @date 2019-08-28
 * @des
 * @modify
 **/
public class UnionPayResponse implements Serializable {


    /**
     * masterId : 2000311146
     * orderId : 20003111462019082800001967
     * currency : RMB
     * amount : 0.10
     * objectName : 测试商品
     * paydate : 20190828160726
     * remark : [{"SFJOrdertype":"0","orderlist":[{"PayModel":"1","SubAccNo":"3617000000001627","TranFee":"0.00"}],"plantCode":"3144","remarktype":"SDS0100000"}]
     * validtime : 0
     * status : 00
     * date :
     * charge :
     * customerId : 9191900082619
     * accNo : 62263301****0000
     * telephone : 181****0005
     */

//    @SerializedName("masterId")
//    public String masterId;
//    @SerializedName("orderId")
//    public String orderId;
//    @SerializedName("currency")
//    public String currency;
    @SerializedName("amount")
    public double amount;
//    @SerializedName("objectName")
//    public String objectName;
//    @SerializedName("paydate")
//    public String paydate;
//    @SerializedName("remark")
//    public String remark;
//    @SerializedName("validtime")
//    public String validtime;

    public long getAmount(){
        return (long) (amount*100);
    }

    @SerializedName("status")
    public String status; //支付响应： 01成功，02失败，00处理中

    public String getPayStatus(){
        if ("00".equals (status)){
            return StatusTable.PayStatus.PROCESSING;
        }else if ("01".equals (status)){
            return StatusTable.PayStatus.SUCCESS;
        }else {
            return StatusTable.PayStatus.FAIL;
        }

    }

//    @SerializedName("date")
//    public String date;
//    @SerializedName("charge")
//    public String charge;
//    @SerializedName("customerId")
//    public String customerId;
//    @SerializedName("accNo")
//    public String accNo;
//    @SerializedName("telephone")
//    public String telephone;
}
