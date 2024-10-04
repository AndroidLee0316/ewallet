package com.pasc.business.ewallet.business.pwd.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/16
 * @des
 * @modify
 **/
public class SetPwdParam {

    @SerializedName ("memberNo")
    public String memberNo; //会员号

    @SerializedName ("password")
    public String password;//支付密码

    @SerializedName ("returnCode")
    public String returnCode; //校验码

    public SetPwdParam(String memberNo, String password, String returnCode) {
        this.memberNo = memberNo;
        this.password = password;
        this.returnCode = returnCode;
    }
}
