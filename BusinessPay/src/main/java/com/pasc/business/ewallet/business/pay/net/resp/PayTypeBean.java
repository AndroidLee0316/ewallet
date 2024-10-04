package com.pasc.business.ewallet.business.pay.net.resp;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.common.utils.Util;

/**
 * 获取支付方式
 * @date 2019/7/30
 * @des
 * @modify
 **/
public class PayTypeBean {
    /***{@link StatusTable.PayType}**/
    @SerializedName("payType")
    public String payType; //支付渠道

    @SerializedName("channel")
    public String channel; //通道类型： 微信-WECHAT、支付宝-ALIPAY

    @SerializedName("icon")
    public String icon; //图标地址

    @SerializedName ("payTypeName")
    public String payTypeName; //支付渠道名称

    public int defaultIcon(){
        if (StatusTable.PayType.BALANCE.equalsIgnoreCase (payType)){
            return R.drawable.ewallet_balance_type_icon;
        } else if (StatusTable.PayType.UNIONQUICKPAY.equalsIgnoreCase (payType)){
            return R.drawable.ewallet_ic_no_bank_card;
        } else if (StatusTable.PayType.WECHAT.equalsIgnoreCase (payType)){
            return R.drawable.ewallet_wechat_type_icon;
        } else if (StatusTable.PayType.ALIPAY.equalsIgnoreCase (payType)){
            return R.drawable.ewallet_ali_type_icon;
        } else if (StatusTable.PayType.SELECT_MORE.equalsIgnoreCase(payType)) {
            return R.drawable.ewallet_more_select_icon;
        } else if (StatusTable.PayType.UNION_BANK.equalsIgnoreCase(payType)) {
            return R.drawable.ewallet_union_pay_icon;
        } else if (StatusTable.PayType.UNION_WECHATJSAPI.equalsIgnoreCase(payType)) {
            return R.drawable.ewallet_wechat_type_icon;
        } else if (StatusTable.PayType.UNION_ALIPAYJSAPI.equalsIgnoreCase (payType)){
            return R.drawable.ewallet_ali_type_icon;
        }
        return R.drawable.ewallet_ic_pay_type_default;
    }



    public String getPayTypeName() {

        if (StatusTable.PayType.BALANCE.equalsIgnoreCase (payType)){
        return     payTypeName +" ( 余额¥"+ Util.doublePoint (balance,2)+" ) ";
        }else if (StatusTable.PayType.UNIONQUICKPAY.equalsIgnoreCase (payType)){
                return bankName+ " ("+Util.getLastStr (bankAcctNo,4)+")";
        }
        return payTypeName;
    }

    @SerializedName ("couponDesc")
    public String couponDesc; //优惠描述，分行用^分割

    @SerializedName ("discountContext")
    public DiscountContextBean discountContext; //支付渠道优惠对象

    @SerializedName ("disable")
    public boolean disable; //true不可用，false可用

    @SerializedName ("balance")
    public long balance; //余额，单位：分，当payType=BALANCE时此字段不为

    /****下面为银行卡的******/
    @SerializedName ("payLimitPerOrder")
    public String payLimitPerOrder; //单笔限额，单位：分，当payType=UNIONQUICKPAY时此字段不为null

    @SerializedName ("payLimitPerDay")
    public String payLimitPerDay; //每日限额，单位：分，当payType=UNIONQUICKPAY时此字段不为null

    @SerializedName ("bankLogo")
    public String bankLogo; //银行卡logo url

    @SerializedName ("bankAcctNo")
    public String bankAcctNo; //,银行卡号，当payType=BALANCE时此字段不为

    @SerializedName ("bankAcctName")
    public String bankAcctName; //持卡人姓名

    @SerializedName ("bindTime")
    public String bindTime; //绑卡时间

    @SerializedName ("bankName")
    public String bankName; //银行名称，当payType=UNIONQUICKPAY时此字段不为null

    @SerializedName ("bankCardType")
    public String bankCardType; //银行卡类型，CREDIT_CARD("01","借记卡"), 	DEBIT_CARD("02","信用卡"); 当payType=UNIONQUICKPAY时此字段不为null

    @SerializedName ("background")
    public String background; //银行卡图片，当payType=UNIONQUICKPAY时此字段不为null

    @SerializedName("cardKey")
    public String cardKey; //银联绑定id，当payType=UNIONQUICKPAY时此字段不为null

    @SerializedName ("watermark")
    public String watermark; //水印url，当payType=UNIONQUICKPAY时此字段不为null

    @SerializedName("bindMobile")
    public String bindMobile;









}
