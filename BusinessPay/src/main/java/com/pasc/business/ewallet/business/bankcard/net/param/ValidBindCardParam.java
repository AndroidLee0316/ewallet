package com.pasc.business.ewallet.business.bankcard.net.param;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.business.common.param.MemberNoParam;

/**
 * @date 2019/8/17
 * @des
 * @modify
 **/
public class ValidBindCardParam  extends MemberNoParam {
    @SerializedName ("smsCode")
    public String smsCode; //短信验证码

    @SerializedName ("bankAcctNo")
    public String bankAcctNo; //银行卡号

    public ValidBindCardParam(String memberNo, String bankAcctNo, String smsCode) {
        super (memberNo);
        this.bankAcctNo=bankAcctNo;
        this.smsCode = smsCode;
    }
}
