package com.pasc.business.ewallet.business.account.net.resp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 查询会员信息
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class QueryMemberResp implements Serializable{
    @SerializedName ("memberNo")
    public String memberNo; //会员号

    @SerializedName ("memberName")
    public String memberName; //会员姓名

    @SerializedName ("certificateNo")
    public String certificateNo; //身份证号

    @SerializedName ("phone")
    public String phone; //银行预留手机号

    @SerializedName ("bankAcctNo")
    public String bankAcctNo; //银行卡号

    @SerializedName ("certificationFlag")
    public String certificationFlag; //实名标志 1-未实名， 2-基本信息验证（银行实名），3-人脸收集（钱包实名）
}
