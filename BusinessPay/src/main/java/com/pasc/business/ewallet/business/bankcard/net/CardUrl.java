package com.pasc.business.ewallet.business.bankcard.net;

import com.pasc.business.ewallet.business.common.CommonUrl;

/**
 * @date 2019-11-06
 * @des
 * @modify
 **/
public class CardUrl {
    //安全卡列表
    public static String list_safe_bank(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/list_bank";
    }

    ////银联卡
    public static String list_quick_bank(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/unionquickpay/getBindCardList";
    }


    // 个人提现绑卡-发送验证码（v1.0）
    public static String bind_card_mbr(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/bind_card_mbr";
    }

    // 个人提现绑卡-校验验证码（v1.0）
    public static String bind_card_valid_mbr(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/bind_card_valid_mbr";
    }


    // 跳转银联绑卡页面（v1.0）
    public static String jumpBindCardPage(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/unionquickpay/jumpBindCardPage";
    }


    //银联快捷支付发送验证码（v1.0）
    public static String quick_pay_sendMsg(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/unionquickpay/sendMsg";
    }

    //银联快捷支付卡解绑（v1.0）
    public static String unbindCard(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/unionquickpay/unbindCard";
    }



}
