package com.pasc.business.ewallet.business.rechargewithdraw.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class CalcWithdrawFeeParam {
    @SerializedName ("memberNo")
    public String memberNo;

    @SerializedName ("withdrawAmt")
    public long withdrawAmt;//提现金额（单位：分）

    public CalcWithdrawFeeParam(String memberNo, long withdrawAmt) {
        this.memberNo = memberNo;
        this.withdrawAmt = withdrawAmt;
    }
}
