package com.pasc.business.ewallet.business.bankcard.net.resp;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.business.bankcard.adapter.IBankCardItem;
import com.pasc.business.ewallet.common.utils.Util;

/**
 * @date 2019/8/16
 * @des
 * @modify
 **/
public class QuickCardBean implements IBankCardItem {

    @SerializedName("bankName")
    public String bankName; //银行名称

    @SerializedName("bankType")
    public String bankType; //银行卡类型：银行卡类型： DEBIT_CARD("01","借记卡"/ 储蓄卡), 	CREDIT_CARD("02","信用卡");
   //

    @SerializedName("bankLogo")
    public String bankLogo; //银行图标url

    @SerializedName("background")
    public String background;//银行背景图片url

    @SerializedName("bankAcctNo")
    public String bankAcctNo; //银行账号

    @SerializedName("bindMobile")
    public String bindMobile; //银行预留手机号，已脱敏

    @SerializedName("openId")
    public String openId; //银联绑定id
    /**
     * cardKey : ab2cc69d4720465591561e9230862646
     * memberNo : 9191900082619
     * cardType : DEBIT_CARD
     * bindTime : 1566895179000
     * bankId : CEB02
     * status : 01
     * isUnbind : false
     * createTime : 1566894451000
     * updateTime : 1566895179000
     * watermark : null
     * payLimitPerOrder : 200000
     * payLimitPerDay : 500000
     */

    @SerializedName("cardKey")
    public String cardKey;
    @SerializedName("memberNo")
    public String memberNo;
    @SerializedName("cardType")
    public String cardType;

    @SerializedName("bankCardType")
    public String bankCardType;

    @SerializedName("bindTime")
    public long bindTime;
    @SerializedName("bankId")
    public String bankId;
    @SerializedName("status")
    public String status;
    @SerializedName("isUnbind")
    public boolean isUnbind;
    @SerializedName("createTime")
    public long createTime;
    @SerializedName("updateTime")
    public long updateTime;
    @SerializedName("watermark")
    public String watermark;

    @SerializedName("payLimitPerOrder")
    public long payLimitPerOrder=-1; // 分
    @SerializedName("payLimitPerDay")
    public long payLimitPerDay=-1; //分

    @SerializedName ("bankAcctName")
    public String bankAcctName;

    @Override
    public String cardKey() {
        return cardKey;
    }

    @Override
    public boolean isSafeCard() {
        return false;
    }

    @Override
    public String logo() {
        return bankLogo;
    }

    @Override
    public String cardNo() {
        return bankAcctNo;
    }

    @Override
    public String bankName() {
        return bankName;
    }

    @Override
    public String getBankNameAndCard() {
        String cardNo = Util.getLastStr (bankAcctNo, 4);
        return bankName + (Util.isEmpty (cardNo) ? "" : " (" + cardNo + ")");
    }

    @Override
    public String cardTypeName() {
        if (!Util.isEmpty (cardType)){
            return getCardTypeNameStr (cardType);
        }
        if (!Util.isEmpty (bankCardType)){
            return getCardTypeNameStr (bankCardType);
        }
        return getCardTypeNameStr (bankType);
    }

    String getCardTypeNameStr(String type){
        return ("01".equals (type) || "DEBIT_CARD".equals (type) ) ? "储蓄卡" : "信用卡";

    }


    @Override
    public String bankBackground() {
        return background;
    }

    @Override
    public String bankWaterMark() {
        return watermark;
    }

    @Override
    public String userName() {
        return bankAcctName;
    }

    @Override
    public String singleLimit() {
        if (payLimitPerOrder>0){
            return "¥" +  Util.doublePoint (payLimitPerOrder,2);
        }
        return "无限额";
    }

    @Override
    public String singleDayLimit() {
        if (payLimitPerDay>0){
            return "¥" + Util.doublePoint (payLimitPerDay,2);

        }
        return "无限额";
    }

    public SafeCardBean convert() {
        SafeCardBean safeCardBean = new SafeCardBean ();
        safeCardBean.bankName = bankName;

        safeCardBean.bankAcctNo = bankAcctNo;

        safeCardBean.memberBankId = openId;


        safeCardBean.memberNo = "";

        safeCardBean.bankAcctName = bankAcctName;

        safeCardBean.bankLogo = bankLogo;


        safeCardBean.background = background;

        safeCardBean.bindTime = "";


        return safeCardBean;
    }





}
