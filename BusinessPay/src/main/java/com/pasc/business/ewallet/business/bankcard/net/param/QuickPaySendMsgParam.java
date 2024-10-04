package com.pasc.business.ewallet.business.bankcard.net.param;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.business.common.param.MemberNoParam;

/**
 * @date 2019/8/20
 * @des
 * @modify
 **/
public class QuickPaySendMsgParam {
    @SerializedName("cardKey")
    public String cardKey; //银行卡唯一标识

    @SerializedName("mchOrderNo")
    public String mchOrderNo; //商户订单号，当tradeType 为PAY时必填

    /*****{@link com.pasc.business.ewallet.business.StatusTable.Trade}*****/
    @SerializedName ("tradeType")
    public String tradeType; //PAY  消费付款,  RECHARGE 钱包充值

    @SerializedName ("amount")
    public long amount; //充值金额，单位：分。不能小于10，当tradeType为RECHARGE时必填

    public QuickPaySendMsgParam(String cardKey, String mchOrderNo, String tradeType, long amount) {
        this.cardKey = cardKey;
        this.mchOrderNo = mchOrderNo;
        this.tradeType = tradeType;
        this.amount = amount;
    }
}
