package com.pasc.business.ewallet.business.account.net;

import com.pasc.business.ewallet.business.common.CommonUrl;

/**
 * @date 2019-11-06
 * @des
 * @modify
 **/
public class CreateAccountUrl {
    //会员状态查询（v2.0）
    public static String memberStatus(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/member_status";
    }
    //是否设置支付密码
    public static String checkPayPassword(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/check_pay_password";
    }
    //查询会员信息
    public static String queryMemberByMemberNo(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/query_memberByMemberNo";
    }

    //身份校验（v1.0）
    public static String validCert(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/valid_cert";

    }

    //发送短信验证码（v1.0）
    public static String sendSmsMbr(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/send_sms_mbr";

    }

    //校验短信验证码（v1.0）
    public static String validSmsMbr(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/valid_sms_mbr";

    }


}
