package com.pasc.business.ewallet.result;

import com.pasc.business.ewallet.NotProguard;

/**
 * 回调接入方 支付结果
 * Created by qinguohuai on 2019/3/27.
 */
@NotProguard
public final class PASCPayResult {

    /** Result Code Start */

    /**
     * 等待支付结果
     */
    public static final int PASC_PAY_CODE_WAITING = 1;

    /**
     * 打开钱包首页，或支付成功
     */
    public static final int PASC_PAY_CODE_SUCCESS = 0;

    /**
     * 初始化失败
     */
    public static final int PASC_PAY_CODE_SDK_INIT_FAILED = -1;

    /**
     * Sdk鉴权未通过
     */
    public static final int PASC_PAY_CODE_SDK_AUTH_FAILED = -2;

    /**
     * 用户取消
     */
    public static final int PASC_PAY_CODE_CANCELED = -3;

    /**
     * 支付失败
     */
    public static final int PASC_PAY_CODE_FAILED = -4;

    /**
     * Token失效
     */
    public static final int PASC_PAY_CODE_TOKEN_INVALID = -5;

    /**
     * 参数为空
     */
    public static final int PASC_PAY_CODE_PARAM_ERROR = -6;

    public static final int PASC_PAY_CODE_NET_ERROR = -7;


    /** Result Code End */



    /** Result Msg Start */

    /**
     * 支付成功
     */
    public static final String PASC_PAY_MSG_SUCCESS = "支付成功";

    /**
     * 等待支付结果
     */
    public static final String PASC_PAY_MSG_WAITING = "等待资金到账";

    /**
     * 初始化失败
     */
    public static final String PASC_PAY_MSG_SDK_INIT_FAILED = "Sdk初始化失败";

    /**
     * Sdk鉴权未通过
     */
    public static final String PASC_PAY_MSG_SDK_AUTH_FAILED = "Sdk鉴权未通过";

    /**
     * 用户取消
     */
    public static final String PASC_PAY_MSG_CANCELED = "用户取消";

    /**
     * 支付失败
     */
    public static final String PASC_PAY_MSG_FAILED = "支付失败";

    /**
     * Token失效
     */
    public static final String PASC_PAY_MSG_TOKEN_INVALID = "Token失效";

    /**
     * 参数有误
     */
    public static final String PASC_PAY_MSG_PARAM_ERROR = "请检查参数";

    public static final String PASC_PAY_MSG_NET_ERROR = "网络异常";


    /** Result Msg End */

    /*****Pay option****/
    public static final String DefaultOp="DefaultOp";
    public static final String WxCardPaNoBindOp="WxCardPaNoBindOp"; // 无锡市民卡没绑定
    public static final  String WxCardPayHasBindOp="WxCardPayHasBindOp";//无锡市民卡已经绑定

}