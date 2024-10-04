package com.pasc.business.ewallet.business.rechargewithdraw.net;

import com.pasc.business.ewallet.business.common.CommonUrl;

/**
 * @date 2019-11-18
 * @des
 * @modify
 **/
public class RechargeWithDrawUrl {
    //充值
    public static String recharge(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/recharge";

    }

    //个人提现
    public static String personWithdraw(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/person_withdraw";

    }


    //提现手续费试算（v1.0）
    public static String calc_withdraw_fee(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/calc_withdraw_fee";

    }
}
