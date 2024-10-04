package com.pasc.business.ewallet.business.rechargewithdraw.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * 个人提现（v1.0）
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class PersonWithdrawParam {

    @SerializedName ("memberNo")
    public String memberNo; //会员号

    @SerializedName ("withdrawAmt")
    public long withdrawAmt; //提现金额（单位：分）

    @SerializedName ("password")
    public String password; //支付密码

    public PersonWithdrawParam(String memberNo, long withdrawAmt, String password) {
        this.memberNo = memberNo;
        this.withdrawAmt = withdrawAmt;
        this.password = password;
    }
}
