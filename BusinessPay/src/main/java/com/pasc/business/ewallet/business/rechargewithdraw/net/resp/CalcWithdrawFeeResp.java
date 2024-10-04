package com.pasc.business.ewallet.business.rechargewithdraw.net.resp;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class CalcWithdrawFeeResp {


    /**
     * rateType : reprehe
     * rate : et non culpa officia
     * withdrawAmt : elit
     * fee : in ut tempor proident
     */

    @SerializedName("rateType")
    public String rateType; //手续费类型：1-按笔 2-费率
    @SerializedName("rate")
    public String rate; //费率
    @SerializedName("withdrawAmt")
    public long withdrawAmt; //提现金额
    @SerializedName("fee")
    public long fee; //手续费
}
