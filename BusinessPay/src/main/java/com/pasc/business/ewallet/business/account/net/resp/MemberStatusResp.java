package com.pasc.business.ewallet.business.account.net.resp;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019-11-06
 * @des
 * @modify
 **/
public class MemberStatusResp {

    @SerializedName("memberNo")
    public String memberNo;
    @SerializedName("status")
    public int status=3; //会员状态：1-初始; 2-正常; 3-注销

    public boolean isInit(){
        return status==1;
    }

    public boolean isOpen(){
        return status==2;
    }
    public boolean isCancle(){
        return status==3;
    }

}
