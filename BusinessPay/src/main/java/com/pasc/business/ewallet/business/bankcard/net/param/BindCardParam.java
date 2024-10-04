package com.pasc.business.ewallet.business.bankcard.net.param;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.business.common.param.MemberNoParam;

/**
 * @date 2019/8/17
 * @des
 * @modify
 **/
public class BindCardParam extends MemberNoParam{

    @SerializedName("bankAcctNo")
    public String bankAcctNo; //银行卡号

    @SerializedName("mobile")
    public String mobile; // 手机号

    public BindCardParam(String memberNo, String bankAcctNo, String mobile) {
        super (memberNo);
        this.bankAcctNo = bankAcctNo;
        this.mobile = mobile;
    }
}
