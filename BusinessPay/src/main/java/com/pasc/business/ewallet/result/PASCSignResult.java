package com.pasc.business.ewallet.result;

import com.pasc.business.ewallet.NotProguard;

/**
 * 回调接入方 签约结果
 * Created by zhuangjiguang on 2019/12/15.
 */
@NotProguard
public final class PASCSignResult {

    /**
     * 未签约
     */
    public static final int PASC_SIGN_CODE_NOSIGN = 1;

    /**
     * 签约成功
     */
    public static final int PASC_SIGN_CODE_SUCCESS = 0;

    /**
     * 用户取消
     */
    public static final int PASC_SIGN_CODE_CANCELED = -3;

    /**
     * 签约失败
     */
    public static final int PASC_SIGN_CODE_FAILED = -4;


    /**
     * 参数为空
     */
    public static final int PASC_SIGN_CODE_PARAM_ERROR = -6;

    /**
     * 网络异常
     */
    public static final int PASC_SIGN_CODE_NET_ERROR = -7;

    /**
     * 签约成功
     */
    public static final String PASC_SIGN_MSG_SUCCESS = "签约成功";

    /**
     * 用户取消
     */
    public static final String PASC_SIGN_MSG_CANCELED = "用户取消";

    /**
     * 签约失败
     */
    public static final String PASC_SIGN_MSG_FAILED = "签约失败";

    /**
     * 参数有误
     */
    public static final String PASC_SIGN_MSG_PARAM_ERROR = "请检查参数";

    /**
     * 网络异常
     */
    public static final String PASC_SIGN_MSG_NET_ERROR = "网络异常";

    /**
     * 未签约
     */
    public static final String PASC_SIGN_MSG_NOSIGN = "未签约";

}