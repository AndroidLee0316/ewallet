package com.pasc.business.ewallet.config;

import com.pasc.business.ewallet.NotProguard;

@NotProguard
public class Constants {

    public static final int PAY_TEST_ENV=0;
    public static final int PAY_STG2_ENV=1;
    public static final int PAY_RELEASE_ENV=2;
    public static  int currentEnv=PAY_RELEASE_ENV;

    public static String PUBLIC_KEY="";
    public static String APP_ID="";
    //app secretKey
    public static String SECRET_KEY = "";
    //环境
    public static boolean IS_DEBUG = true;
    //渠道号
    public static String IN_CHANNEL_ID = "";

//    // 开户协议地址
//    public static String OPENACCOUNT_SERVICE_PROTOCOL = "https://smt-stg.yun.city.pingan.com/basic/stg2/app/feature/protocol/#/payment";
//    // 开户 收不到验证码
//    public static String OPENACCOUNT_UNRECEIVED_CODE = "https://smt-stg.yun.city.pingan.com/basic/stg/app/feature/payment-process/#/verification/";
//    // 无卡支付签约协议
//    public static String PAY_BANKCARD_SIGN_SERVICE_PROTOCOL = "https://smt-stg.yun.city.pingan.com/basic/stg/app/feature/protocol/#/signing";
//    //查看支持的银行卡 ，更多 ，换绑卡和二类户
//    public static String CREATE_ACCOUNT_SUPPORT_BANK_CARD = "http://smt-stg.yun.city.pingan.com/basic/stg/app/feature/bank-list/#/all";
//    //查看支持的支付银行卡
//    public static String CREATE_ACCOUNT_SUPPORT_SIGN_BANK_CARD = "http://smt-stg.yun.city.pingan.com/basic/stg/app/feature/bank-list/#/";


    // 开户协议地址
    public static String OPENACCOUNT_SERVICE_PROTOCOL = "https://m.myyancheng.com.cn/pay/feature/protocol/#/payment";
    // 开户 收不到验证码
    public static String OPENACCOUNT_UNRECEIVED_CODE = "https://m.myyancheng.com.cn/pay/feature/payment-process/#/verification/";
    // 无卡支付签约协议
    public static String PAY_BANKCARD_SIGN_SERVICE_PROTOCOL = "https://m.myyancheng.com.cn/pay/feature/protocol/#/signing";
    //查看支持的银行卡 ，更多 ，换绑卡和二类户
    public static String CREATE_ACCOUNT_SUPPORT_BANK_CARD = "https://m.myyancheng.com.cn/pay/feature/bank-list/#/all";
    //查看支持的支付银行卡
    public static String CREATE_ACCOUNT_SUPPORT_SIGN_BANK_CARD = "https://m.myyancheng.com.cn/pay/feature/bank-list/#/";

    //tenddata初始化配置
    public static final String TEND_DATA_APP_ID = "23C3D8B71AF647E8A161C5A19D697ADF";
    public static final String TEND_DATA_APP_ID_TEST = "64A1DFEF1C884DE0BB6B99EB1271A11B";

    public static String wxCardPuk="";

}
