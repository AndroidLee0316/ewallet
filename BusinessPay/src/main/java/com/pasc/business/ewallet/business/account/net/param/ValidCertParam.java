package com.pasc.business.ewallet.business.account.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * 身份校验
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class ValidCertParam {


    /**
     * memberNo : irure Ut
     * certificateNo : voluptate
     * memberName : anim laboris
     */

    @SerializedName("memberNo")
    public String memberNo; //会员号

    @SerializedName("certificateNo")
    public String certificateNo; //身份证号

    @SerializedName("memberName")
    public String memberName; //姓名

    public ValidCertParam(String memberNo, String certificateNo, String memberName) {
        this.memberNo = memberNo;
        this.certificateNo = certificateNo;
        this.memberName = memberName;
    }
}
