package com.pasc.business.ewallet.business;

/**
 * @author yangzijian
 * @date 2019/3/22
 * @des
 * @modify
 **/
public interface BundleKey {

    interface Common {
        String key_accessUserId = "accessUserId";
        String key_inChannelId = "inChannelId";
        String key_title="title";
        /*****{@link com.pasc.business.ewallet.business.StatusTable.VerifyTag}*****/
        String key_verify_tag="verify_tag";
        String key_flag_tag="flag_tag";
    }

    interface Trade {
        String key_balanceBean = "balanceBean";
        String key_search_flag = "searchFlag";
        int defaultYear = 2019;
        int defaultMonth = 6;
        String key_month = "month";
        String key_year = "year";

    }


    interface User {
        String key_name = "name";
        String key_validateCode = "validateCode";
        // 手机号
        String key_phoneNum = "phoneNum";
        //身份证好
        String key_idCardNumber = "bindCardNo";
        //银行卡号
        String key_bindCardNo = "bindCardNo";

        //用户已经开通，但是没设置二类户，去开通钱包
        String key_open_second_card_flag = "key_open_second_card_flag";

        /*** 设置密码标记类型 {@link com.pasc.business.ewallet.business.StatusTable.PassWordTag} ***/
        String key_set_pwd_tag = "key_set_pwd_tag";
        String key_flag_bind = "key_flag_bind";

    }

    interface Pay {
        // 订单号
        String key_outTradeNo = "outTradeNo";
        String key_query_order_resp = "queryOrderResp";
        String key_memberNo = "memberNo";
        String key_merchantNo = "merchantNo"; // 商户号
        String key_mchOrderNo = "mchOrderNo"; // 商户订单号
        String key_scheme = "scheme";
        String key_orderNo = "orderNo";
        String key_money = "money";
        String key_pay_mode = "pay_mode";
        String key_balance = "balance";
        String key_withdraw_info = "withdraw_info";
        /*** 设置密码标记类型 {@link com.pasc.business.ewallet.business.StatusTable.Scenes} ***/
        String key_sence = "sence";
        String key_bankCard_info = "bankCard_info";
        String key_tradeType = "tradeType";
        String key_payOption = "payOption"; // 支付选项
        String key_channel = "channel"; // 渠道号
        String key_sceneId = "sceneId"; // 场景号

    }

    interface Web{
        String key_title=Common.key_title;
        String key_url="url";

    }

    interface Logout{
        String key_member_valid_info="member_valid_info";
    }

}
