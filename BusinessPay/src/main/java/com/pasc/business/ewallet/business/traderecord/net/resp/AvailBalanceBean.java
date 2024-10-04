package com.pasc.business.ewallet.business.traderecord.net.resp;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.common.utils.Util;

import java.io.Serializable;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public class AvailBalanceBean implements Serializable{

    public boolean isHeader=false;
    public boolean isLastInIt = false;//本月最后一条消息
    public String headerValue;

//    @SerializedName("orderSerialNo")
//    public String orderSerialNo;
//    @SerializedName("subject")
//    public String subject;
//    @SerializedName("description")
//    public String description;
//    @SerializedName("tradeTime")
//    public String tradeTime;
//    @SerializedName("orderType")
//    public String orderType;
//    @SerializedName("tradeAmount")
//    public String tradeAmount;
//    @SerializedName("remark")
//    public String remark;


    @SerializedName("tranSysNo")
    public String tranSysNo; // 账单流水号
    @SerializedName("remark")
    public String remark; // 备注
    @SerializedName("tranTime")
    public String tranTime;
    @SerializedName("ipFlag")
    public String ipFlag;  // 交易类型 0充值 1 转账
    @SerializedName("tranTimeLong")
    public String tranTimeLong; // 时间戳
    @SerializedName("tranAmt")
    public String tranAmt; //交易金额

    /**
     * 余额明细显示交易金额
     * @return
     */
    public String getTradeAmount(){
        String amount = tranAmt;
        if (!TextUtils.isEmpty(amount)){
            if ("0".equals (ipFlag)) {
                amount = "+" + amount;
            } else if ("1".equals (ipFlag)) {
                amount = "-" + amount;
            }
        }else {
            amount = "";
        }
        return amount;
    }

    public String getYearAndMonth () {
        return Util.stampToYearAndMonth (tranTimeLong);
    }

}
