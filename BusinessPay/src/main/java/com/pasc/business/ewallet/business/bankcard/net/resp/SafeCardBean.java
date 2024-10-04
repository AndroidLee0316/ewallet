package com.pasc.business.ewallet.business.bankcard.net.resp;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.business.bankcard.adapter.IBankCardItem;
import com.pasc.business.ewallet.common.utils.Util;

/**
 * 安全卡信息
 *
 * @date 2019/8/16
 * @des
 * @modify
 **/
public class SafeCardBean implements IBankCardItem {


    @SerializedName("bankName")
    public String bankName; //银行名称

    @SerializedName("bankAcctNo")
    public String bankAcctNo; //银行账号

    @SerializedName("memberBankId")
    public String memberBankId;//银行卡ID

    @SerializedName("memberNo")
    public String memberNo;//会员号

    @SerializedName("bankAcctName")
    public String bankAcctName;//持卡人姓名

    @SerializedName("bankLogo")
    public String bankLogo;//银行图标

    @SerializedName("background")
    public String background;//银行背景

    @SerializedName("bindTime")
    public String bindTime;//绑定时间

    @SerializedName("watermark")
    public String watermark;//水印

    @Override
    public String cardKey() {
        return null;
    }

    @Override
    public boolean isSafeCard() {
        return true;
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
        return "储蓄卡";
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
        return "无限额";
    }

    @Override
    public String singleDayLimit() {
        return "无限额";
    }
}
