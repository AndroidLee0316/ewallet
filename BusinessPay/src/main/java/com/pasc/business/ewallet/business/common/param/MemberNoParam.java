package com.pasc.business.ewallet.business.common.param;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class MemberNoParam {
    @SerializedName("memberNo")
    public String memberNo;

    public MemberNoParam(String memberNo) {
        this.memberNo = memberNo;
    }
}
