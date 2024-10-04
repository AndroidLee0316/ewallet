package com.pasc.business.ewallet.business.account.common;


import com.pasc.business.ewallet.business.account.net.resp.CheckPwdHasSetResp;
import com.pasc.business.ewallet.business.account.net.resp.MemberStatusResp;

/**
 * 检查开户状态Listener
 * Created by qinguohuai on 2019/3/8.
 */
public abstract class CheckAccountListener {



    public  void onSuccess(CheckPwdHasSetResp checkPwdHasSetResp){

    }

    public void onSuccess(MemberStatusResp memberStatusResp){

    }

    public abstract void onFail(String code, String msg);
}
