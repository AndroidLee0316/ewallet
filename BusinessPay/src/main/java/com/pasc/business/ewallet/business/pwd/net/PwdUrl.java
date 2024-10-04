package com.pasc.business.ewallet.business.pwd.net;

import com.pasc.business.ewallet.business.common.CommonUrl;

/**
 * @date 2019-11-06
 * @des
 * @modify
 **/
public class PwdUrl {

    //实名
    public static String add_cert(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/add_cert";
    }
    //设置密码
    public static String set_password(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/set_password";
    }

    //校验支付密码
    public static String valid_pwd(){
        return CommonUrl.getHostAndGateWay ()+ CommonUrl.URL_PREFIX + "/valid_pwd";
    }


}
