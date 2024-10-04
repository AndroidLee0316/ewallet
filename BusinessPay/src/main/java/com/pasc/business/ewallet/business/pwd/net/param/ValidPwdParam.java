package com.pasc.business.ewallet.business.pwd.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class ValidPwdParam {


    /**
     * memberNo : ad Lorem anim fugiat velit
     * payPwd : reprehenderit aliquip
     */

    @SerializedName("memberNo")
    public String memberNo;
    @SerializedName("payPwd")
    public String payPwd;

    public ValidPwdParam(String memberNo, String payPwd) {
        this.memberNo = memberNo;
        this.payPwd = payPwd;
    }
}
