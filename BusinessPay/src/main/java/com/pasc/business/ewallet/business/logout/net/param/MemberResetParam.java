package com.pasc.business.ewallet.business.logout.net.param;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.business.common.param.MemberNoParam;

/**
 * @date 2019-11-14
 * @des
 * @modify
 **/
public class MemberResetParam extends MemberNoParam {
    @SerializedName ("returnCode")
    public String returnCode;
    public MemberResetParam(String memberNo,String returnCode) {
        super (memberNo);
        this.returnCode=returnCode;
    }
}
