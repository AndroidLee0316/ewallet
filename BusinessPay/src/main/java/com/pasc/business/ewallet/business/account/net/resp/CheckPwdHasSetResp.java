package com.pasc.business.ewallet.business.account.net.resp;

import com.google.gson.annotations.SerializedName;

/**
 * 检查密码是否设置
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class CheckPwdHasSetResp {
    @SerializedName ("check_pay_password")
    public String check_pay_password; //YES为已设置，NO为未设置

    public boolean hasSetPwd(){
        return "YES".equalsIgnoreCase (check_pay_password);
    }


}
