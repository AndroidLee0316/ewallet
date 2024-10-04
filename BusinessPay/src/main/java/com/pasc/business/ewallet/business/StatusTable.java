package com.pasc.business.ewallet.business;

import com.pasc.business.ewallet.NotProguard;

/**
 * @date 2019/6/27
 * @des
 * @modify
 **/
@NotProguard
public class StatusTable {
    //是否开启快捷支付卡
    public static final boolean enableQuickCard = false;
    //是否支持多张安全卡
    public static final boolean enableMultiSafeCard = false;

    public interface Account {
        //token 失效
        String USER_TOKEN_INVALID = "USER_TOKEN_INVALID";
        String VALIDATE_CODE_INVALID="-30021"; //校验码无效
        String VALIDATE_CODE_NOT_MATCH="-30022"; //校验码不匹配
    }

    public interface PassWord {

        //密码错误
//        String PASSWORD_ERROR = "PASSWORD_ERROR"; // 之前的密码错误
//        String PAY_PWD_ERROR = "PAY_PWD_ERROR"; //错误次数小于3次
        String PASSWORD_ERROR = "-10300"; // 支付密码错误
        String PAY_PWD_ERROR = "-103001"; //错误达最大限制，账户已锁定
        String PAY_PWD_ERROR_MT_3 = "PAY_PWD_ERROR_MT_3";  //错误次数超过3次
        String PAY_PWD_ERROR_MT_5 = "PAY_PWD_ERROR_MT_5";  //错误次数超过5次

        String PWD_EQUAL_OLD_PWD="-30008"; //新密码与旧密码相同



        //    200-正常 500-异常,-10300 支付密码错误
        //    -10301 错误达最大限制，账户已锁定
        //      -20029-订单不存在  -20037-订单已支付
       //    -20038-订单已关闭   -20039-订单支付失败
       //    -20019-支付方式不支持    -50010-账户已冻结
       // -20005-会员不存在  -20006-商户会员不存在   -20008-订单号重复   -20029-订单不存在
       // -20011-未实名充值额度未设置    -20010-查询余额异常    -20012-未实名最大允许额度

      // -20034-未通过打款验证      -20030-提现金额不足  -20021-银行卡未绑定   -20036-账户已冻结
        //-30002-金额不合法  -30003-参数为空
    }

    public interface VerifyTag{
        String ADD_QUICK_CARD="ADD_QUICK_CARD"; // 添加银联卡，校验密码

        String LOGOUT_WALLET="LOGOUT_WALLET"; // 注销钱包 校验密码

    }

    @NotProguard
    public interface PayStatus {
        // SUCCESS-成功；FAIL-失败；PROCESSING-进行中；CLOSE-订单关闭，UNPAID-待支付
        String SUCCESS = "SUCCESS";
        String FAIL = "FAIL";
        String PROCESSING = "PROCESSING";
        String CLOSE = "CLOSE";
        String UNPAID = "UNPAID";


    }

    @NotProguard
    public interface PayType {
        //支付渠道 枚举类型 WECHAT：微信支付，ALIPAY：支付宝支付，UNIONQUICKPAY: 银联快捷支付，BALANCE:余额支付
        String BALANCE = "BALANCE";
        String WECHAT = "WECHAT";
        String ALIPAY = "ALIPAY";
        String UNIONQUICKPAY = "UNIONQUICKPAY";
        String WXCITIZEN="WXCITIZEN";//无锡联机账户
        String SELECT_MORE = "SELECT_MORE"; //选择更多支付方式
        String UNION_BANK = "UNION_BANK"; //银联无跳转支付
        String UNION_ALIPAYJSAPI = "UNION_ALIPAYJSAPI"; //银联支付宝
        String UNION_WECHATJSAPI = "UNION_WECHATJSAPI"; //银联微信
    }

    @NotProguard
    public interface Channel {
        //通道类型： 微信-WECHAT、支付宝-ALIPAY
        String WECHAT = "WECHAT";
        String ALIPAY = "ALIPAY";
    }

    public  interface Trade {

        String ALL = "ALL"; //充值
        //交易类型
        String RECHARGE = "RECHARGE"; //充值
        String REFUND = "REFUND"; //退款
        String PAY = "PAY"; //消费
        String WITHDRAW = "WITHDRAW";// 提先

    }

    public interface PassWordTag {
        String fromDefaultPwdTag = "fromDefaultPwdTag"; //默认
        String fromForgetPwdTag = "fromForgetPwdTag"; //来自忘记密码
        String fromNormalCreateAccountTag = "fromNormalCreateAccountTag"; //正常开户
        String fromPayCreateAccountPwdTag = "fromPayCreateAccountPwdTag"; // 来自支付开户
        String fromPayLogoutTag = "fromPayLogoutTag"; // 来自注销账户

    }

    public static String getScenes(String tag){
        if (PassWordTag.fromForgetPwdTag.equalsIgnoreCase (tag)){
            return StatusTable.Scenes.FORGET_PSW;
        }else if (PassWordTag.fromPayLogoutTag.equalsIgnoreCase (tag)){
            return StatusTable.Scenes.MEMBER_CANCEL;
        }else {
            return StatusTable.Scenes.SET_PSW;
        }
    }

    public  interface PayMode {
        String payMode = "PayMode";
        String rechargeMode = "rechargeMode";
    }

    public  interface Scenes {
        //设置支付密码
        String SET_PSW = "SET_PSW";

        //忘记支付密码
        String FORGET_PSW = "FORGET_PSW";

        //注销
        String MEMBER_CANCEL = "MEMBER_CANCEL";


        String RECHARGE = "RECHARGE"; //充值
        String PAY_SB = "PAY_SB";//支付


    }

    public interface PayOption{
        String DefaultOp="DefaultOp";
        String WxCardPaNoBindOp="WxCardPaNoBindOp"; // 没绑定
        String WxCardPayHasBindOp="WxCardPayHasBindOp";//绑定

    }
}
