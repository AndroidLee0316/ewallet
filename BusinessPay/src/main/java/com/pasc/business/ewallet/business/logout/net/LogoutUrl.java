package com.pasc.business.ewallet.business.logout.net;

import com.pasc.business.ewallet.business.common.CommonUrl;

/**
 * @date 2019-11-06
 * @des
 * @modify
 **/
public class LogoutUrl {

    // 会员重置校验（v2.0）
    public static String memberCancelValid(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/member_cancel_valid";
    }

    //会员重置（v2.0）
    public static String memberReset(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/member_reset";
    }

    //会员注销（v2.0）
    public static String memberCancel(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/member_cancel";
    }
}
