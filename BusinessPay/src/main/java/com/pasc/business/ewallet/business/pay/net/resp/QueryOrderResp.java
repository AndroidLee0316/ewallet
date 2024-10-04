package com.pasc.business.ewallet.business.pay.net.resp;

import android.text.Html;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.common.utils.Util;

import java.io.Serializable;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
public class QueryOrderResp implements Serializable{


    public boolean needCallback=true;


    @SerializedName("orderNo")
    public String orderNo; // 交易订单号
    @SerializedName("tradeTime")
    public String tradeTime; //交易时间

    //详情需要
    /***** {@link com.pasc.business.ewallet.business.StatusTable.PayStatus} ****/
    @SerializedName("status")
    public String status; // 状态， SUCCESS-成功；FAIL-失败；PROCESSING-进行中；CLOSE-订单关闭，UNPAID-待支付

    //详情需要
    @SerializedName("channel")
    /***** {@link com.pasc.business.ewallet.business.StatusTable.PayType} ****/
    public String channel; // WECHAT-微信，ALIPAY-支付宝，BALANCE-余额

    //详情需要
    @SerializedName("channelDesc")
    public String channelDesc; //支付方式名称

    //详情需要
    @SerializedName("merchantName")
    public String merchantName; //商户名称

    //详情需要
    @SerializedName("amount")
    public long amount; //订单金额，单位：分

    //错误详情需要
    @SerializedName("statusDesc")
    public String statusDesc; //订单状态描述
    @SerializedName("goodsName")
    public String goodsName; //商品名称
    @SerializedName("merchantIcon")
    public String merchantIcon; //商户图标
    @SerializedName("payOrderNo")
    public String payOrderNo; //支付订单号/交易流水号



    @SerializedName("tradeType")
    /***** {@link com.pasc.business.ewallet.business.StatusTable.Trade} ****/
    public String tradeType; // 状态， SUCCESS-成功；FAIL-失败；PROCESSING-进行中；CLOSE-订单关闭，UNPAID-待支付



    public int getDefaultIcon() {
        if (StatusTable.Trade.PAY.equals (tradeType)) {
            return R.drawable.ewallet_pay_default_icon;
        } else if (StatusTable.Trade.RECHARGE.equals (tradeType)) {
            return R.drawable.ewallet_bill_recharge_icon;
        } else if (StatusTable.Trade.REFUND.equals (tradeType)) {
            return R.drawable.ewallet_bill_refund_icon;
        } else if (StatusTable.Trade.WITHDRAW.equals (tradeType)) {
            return R.drawable.ewallet_bill_withdraw_icon;
        }
        return R.drawable.ewallet_pay_default_icon;

    }

    public CharSequence getPayAmount() {
        String money = Util.doublePoint (amount, 2);

        if (StatusTable.Trade.PAY.equalsIgnoreCase (tradeType)) {
            money="-"+money;
        } else if (StatusTable.Trade.REFUND.equalsIgnoreCase (tradeType)
                ||StatusTable.Trade.RECHARGE.equalsIgnoreCase (tradeType)
                ) {

            money = "<font color='#22C8D8'>+" + money + "</font>";
        } else  {
            money="-"+money;
        }

        return Html.fromHtml (money);
    }

    public CharSequence getPayAmountDetail() {
        String money = Util.doublePoint (amount, 2);

        if (StatusTable.Trade.PAY.equalsIgnoreCase (tradeType)) {
            money="-"+money;
        } else if (StatusTable.Trade.REFUND.equalsIgnoreCase (tradeType)
                ||StatusTable.Trade.RECHARGE.equalsIgnoreCase (tradeType)
        ) {

            money = "+" + money;
        } else  {
            money="-"+money;
        }

        return Html.fromHtml (money);
    }

    public String getAllTime(){
        return Util.stampToAllDate (tradeTime);
    }


}
