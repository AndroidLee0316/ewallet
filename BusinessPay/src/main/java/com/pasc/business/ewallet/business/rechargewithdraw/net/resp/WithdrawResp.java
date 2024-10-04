package com.pasc.business.ewallet.business.rechargewithdraw.net.resp;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.common.utils.Util;

import java.io.Serializable;

/**
 * @date 2019/8/21
 * @des
 * @modify
 **/
public class WithdrawResp implements Serializable {


    /**
     * rateType : 2
     * rate : 0.73
     * withdrawAmt : 4
     * fee : 0
     * realAmount : 4
     * bankName : 平安银行
     * bankAcctNo : 6230580000000052220
     * accountingDate : 1566361371363
     * applyDate : 1566354171363
     */

    @SerializedName("rateType")
    public String rateType;

    @SerializedName("rate")
    public double rate;

    @SerializedName("withdrawAmt")
    public long withdrawAmt; //实际到账

    @SerializedName("fee")
    public long fee; //手续费

    @SerializedName("realAmount")
    public long realAmount;

    @SerializedName("bankName")
    public String bankName; //银行名称

    @SerializedName("bankAcctNo")
    public String bankAcctNo; // 银行卡号

    @SerializedName("applyDate")
    public long applyDate; //申请时间

    @SerializedName("accountingDate")
    public long accountingDate; //到账时间

    public String getFee() {
//        if (fee==0){
//            return null;
//        }else {
        return Util.doublePoint (fee, 2);
//        }
    }


    public String getBankName() {
        String cardNo = Util.getLastStr (bankAcctNo, 4);
        return bankName + (Util.isEmpty (cardNo) ? "" : " (" + cardNo + ")");
    }

    public String getApplyDate() {
        return Util.getMonth_Day_Hour_Min (applyDate);
    }

    public String getAccountingDate() {
        return Util.getMonth_Day_Hour_Min (accountingDate);
    }

    public String getAmount() {
        return Util.doublePoint (withdrawAmt, 2);
    }

    public String getRealAmount() {
        return Util.doublePoint (realAmount, 2);
    }

}
