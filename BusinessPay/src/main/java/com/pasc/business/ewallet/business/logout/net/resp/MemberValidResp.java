package com.pasc.business.ewallet.business.logout.net.resp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @date 2019-11-06
 * @des
 * @modify
 **/
public class MemberValidResp implements Serializable {


    @SerializedName("allowCancel")
    public boolean allowCancel; //是否允许注销；true：允许 false：不允许

    @SerializedName("balance")
    public long balance; //可用余额 ,分

    @SerializedName("withdrawAmt")
    public long withdrawAmt; //可提现余额

//    @SerializedName("isTrading")
    @SerializedName ("trading")
    public boolean isTrading; //存在在途交易；true：存在 false：不存在

    public boolean hasWithdrawAmt(){
        return withdrawAmt>0;
    }
}
