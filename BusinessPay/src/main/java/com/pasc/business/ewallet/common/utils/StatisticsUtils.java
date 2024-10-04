package com.pasc.business.ewallet.common.utils;

import com.pasc.lib.pay.statistics.StatisticsManager;

/**
 * 功能：埋点统计
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/4/4
 */
public class StatisticsUtils {


    /**
     * 进入钱包
     */
    public static void qb_main(){
        StatisticsManager.getInstance().onEvent("qb_main");
    }

    /**
     * 钱包页：点击余额
     */
    public static void qb_elementclick_balance(){
        StatisticsManager.getInstance().onEvent("qb_elementclick","余额");
    }
    /**
     * 钱包页：点击提现
     */
    public static void qb_elementclick_withdraw(){
        StatisticsManager.getInstance().onEvent("qb_elementclick","提现");
    }
    /**
     * 钱包页：点击充值
     */
    public static void qb_elementclick_recharge(){
        StatisticsManager.getInstance().onEvent("qb_elementclick","充值");
    }
    /**
     * 钱包页：点击账单
     */
    public static void qb_elementclick_bill(){
        StatisticsManager.getInstance().onEvent("qb_elementclick","账单");
    }
//账单明细	点击任意账单明细
    public static void qb_paymentdetail(){
        StatisticsManager.getInstance().onEvent("qb_paymentdetail");

    }
    /**
     * 钱包页：点击支付管理
     */
    public static void qb_elementclick_payManager(){
        StatisticsManager.getInstance().onEvent("qb_elementclick","支付管理");
    }

    public static void qb_elementclick_back(){
        StatisticsManager.getInstance().onEvent("qb_elementclick","返回");
    }

    public static void qb_setting_modifyPwd(){
        StatisticsManager.getInstance().onEvent("qb_setting","修改支付密码");
    }

    public static void qb_setting_forgetPwd(){
        StatisticsManager.getInstance().onEvent("qb_setting","忘记支付密码");
    }
    //通过短信验证进入密码输入页
    public static void wjmm_smspass(){
        StatisticsManager.getInstance().onEvent("wjmm_smspass");

    }
    /**
     * 钱包页：充值确认：取消
     */
    public static void qb_topupcomfirm_cancle(){
        StatisticsManager.getInstance().onEvent("qb_elementclick","取消");
    }
    /**
     * 钱包页：充值确认：继续充值
     */
    public static void qb_topupcomfirm_continue(){
        StatisticsManager.getInstance().onEvent("qb_elementclick","继续充值");
    }

    /**
     * 余额页：充值
     */
    public static void qb_balance_recharge(){
        StatisticsManager.getInstance().onEvent("qb_balance","充值");
    }

    /**
     * 余额页：提现
     */
    public static void qb_balance_withdraw(){
        StatisticsManager.getInstance().onEvent("qb_balance","提现");
    }

    /**
     * 余额页充值确认：取消
     */
    public static void qb_balancetopupcomfirm_cancle(){
        StatisticsManager.getInstance().onEvent("qb_balancetopupcomfirm","取消");
    }

    /**
     * 充值页：发起充值
     */
    public static void cz_topupintend(){
        StatisticsManager.getInstance().onEvent("cz_topupintend");
    }


    /**
     * 充值页：充值成功
     */
    public static void cz_topupsuccess(){
        StatisticsManager.getInstance().onEvent("cz_topupsuccess");
    }



    /**
     * 发起提现
     */
    public static void cz_withdrawintend(){
        StatisticsManager.getInstance().onEvent("cz_withdrawintend");
    }


    /**
     * 收银台
     */
    public static void cz_withdrawsuccess(){
        StatisticsManager.getInstance().onEvent("cz_withdrawsuccess");
    }

    //收银台密码校验成功
    public static void syt_passwordpass(){
        StatisticsManager.getInstance().onEvent("syt_passwordpass");

    }

    /**
     * 收银台：钱包支付
     */
    public static void syt_main_balance(){
        StatisticsManager.getInstance().onEvent("syt_main", "钱包支付");
    }

    /**
     * 收银台：银行卡支付
     */
    public static void syt_main_bankcard(){
        StatisticsManager.getInstance().onEvent("syt_main", "银行卡支付");
    }


    /**
     * 收银台：微信支付
     */
    public static void syt_main_wechard(){
        StatisticsManager.getInstance().onEvent("syt_main", "微信支付");
    }
    public static void syt_main_ali(){
        StatisticsManager.getInstance().onEvent("syt_main", "支付宝支付");
    }
    public static void syt_main_sn(){
        StatisticsManager.getInstance().onEvent("syt_main", "苏宁支付");
    }


    /**
     * 收银台：钱包支付
     */
    public static void cz_main_balance(){
        StatisticsManager.getInstance().onEvent("cz_main", "钱包支付");
    }

    /**
     * 收银台：银行卡支付
     */
    public static void cz_main_bankcard(){
        StatisticsManager.getInstance().onEvent("cz_main", "银行卡支付");
    }


    /**
     * 收银台：微信支付
     */
    public static void cz_main_wechard(){
        StatisticsManager.getInstance().onEvent("cz_main", "微信支付");
    }
    public static void cz_main_ali(){
        StatisticsManager.getInstance().onEvent("cz_main", "支付宝支付");
    }
    //开户------------------
    /**
     * 开户：点击开通
     */
    public static void kh_create(){
        StatisticsManager.getInstance().onEvent("kh_create");
    }
    /**
     * 开户：点击开通uv
     */
    public static void kh_create_uv(){
        StatisticsManager.getInstance().onEvent("kh_create_uv");
    }
    /**
     * 开户：身份认证页进入,成功进入身份验证页
     */
    public static void kh_authorize(){
        StatisticsManager.getInstance().onEvent("kh_authorize");
    }
    //点击下一步（身份验证页）
    public static void kh_idnextstep(){
        StatisticsManager.getInstance().onEvent("kh_idnextstep");
    }
    //预留手机号验证页进入,成功进入手机号验证页
    public static void kh_phonenumber(){
        StatisticsManager.getInstance().onEvent("kh_phonenumber");

    }
    //点击下一步（手机号验证页）,点击手机号验证页下一步按钮
    public static void kh_pnnextstep(){
        StatisticsManager.getInstance().onEvent("kh_pnnextstep");

    }
    //设置支付密码页进入,成功进入设置支付密码页
    public static void kh_setpassword(){
        StatisticsManager.getInstance().onEvent("kh_setpassword");

    }
    /**
     * 开户：通过身份认证
     */
    public static void kh_authorized(){
        StatisticsManager.getInstance().onEvent("kh_authorized");
    }
    /**
     * 开户：已开户用户开通
     */
    public static void kh_registed(){
        StatisticsManager.getInstance().onEvent("kh_registed");
    }
    /**
     * 开户：通过证照上传
     */
    public static void kh_imagepass(){
        StatisticsManager.getInstance().onEvent("kh_imagepass");
    }
    /**
     * 开户：证照不通过
     */
    public static void kh_imagefail(){
        StatisticsManager.getInstance().onEvent("kh_imagefail");
    }

    /**
     * 查看支持银行卡
     */
    public static void kh_checkcard(){
        StatisticsManager.getInstance().onEvent("kh_checkcard");
    }

    /**
     * 不支持的银行卡
     */
    public static void kh_cardnonsupport(){
        StatisticsManager.getInstance().onEvent("kh_cardnonsupport");
    }

    /**
     * 开户：开户成功
     */
    public static void kh_accountcreated(){
        StatisticsManager.getInstance().onEvent("kh_accountcreated");
    }
    /**
     * 开户：钱包开通成功,设置密码成功
     */
    public static void kh_walletcreated(){
        StatisticsManager.getInstance().onEvent("kh_walletcreated");
    }
    /**
     * 开户：通过身份验证
     */
    public static void wjmm_identitypass(){
        StatisticsManager.getInstance().onEvent("wjmm_identitypass");
    }
    /**
     * 开户：通过银行卡验证
     */
    public static void wjmm_cardpass(){
        StatisticsManager.getInstance().onEvent("wjmm_cardpass");
    }
    /**
     * 开户：成功设置密码
     */
    public static void wjmm_pwresetsuccess(){
        StatisticsManager.getInstance().onEvent("wjmm_pwresetsuccess");
    }
    /**
     * 开户：成功修改密码
     */
    public static void xgmm_pwresetsuccess(){
        StatisticsManager.getInstance().onEvent("xgmm_pwresetsuccess");
    }


    /**
     * 收银台：收银台银行卡支付密码校验成功
     */
    public static void syt_cardpasswordpass(){
        StatisticsManager.getInstance().onEvent("syt_cardpasswordpass");
    }

    /**
     * 收银台：收银台银行卡支付otp校验成功
     */
    public static void syt_cardotppass(){
        StatisticsManager.getInstance().onEvent("syt_cardotppass");
    }


    /**
     * 收银台：支付成功
     */
    public static void syt_paymentsuccess(String text){
        StatisticsManager.getInstance().onEvent("syt_paymentsuccess",text);
    }

    /**
     * 收银台：发起签约
     */
    public static void qy_otppass(){
        StatisticsManager.getInstance().onEvent("qy_otppass");
    }


    /**
     * 收银台：签约成功
     */
    public static void qy_paymentsuccess(){
        StatisticsManager.getInstance().onEvent("qy_paymentsuccess");
    }


    /**
     * 调起钱包
     */
    public static void start_sdk(){
        // 调起钱包
        StatisticsManager.getInstance().onEvent("qb_start");
        // 调起钱包uv
        StatisticsManager.getInstance().onEvent("qb_start_uv");
    }

}
