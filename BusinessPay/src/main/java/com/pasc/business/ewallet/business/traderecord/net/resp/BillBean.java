package com.pasc.business.ewallet.business.traderecord.net.resp;

import android.text.Html;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.common.utils.Util;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
public class BillBean {

    /**
     * orderNo : 120919000749241565158664
     * tradeType : PAY
     * amount : 1
     * tradeTime : 2019-08-07 14:17:45
     * status : SUCCESS
     * statusDesc : 成功
     * rownum : 1
     * merchantName : 716新商户
     * merchantIcon : null
     * payOrderNo : null
     * merchantNo : 10819010716
     * goodsName : 买了个表
     * channel : WECHAT
     * channelDesc : 微信支付
     * mchOrderNo : gogololzuotoumopopoo
     */

    @SerializedName("orderNo")
    public String orderNo;

    /***交易类型 {@link com.pasc.business.ewallet.business.StatusTable.Trade}*****/
    @SerializedName("tradeType")
    public String tradeType;
    @SerializedName("amount")
    public long amount;
    @SerializedName("tradeTime")
    private String tradeTime;

    /***
     * {@link com.pasc.business.ewallet.business.StatusTable.PayStatus}
     * 状态；SUCCESS-成功；FAIL-失败；PROCESSING-进行中；CLOSE-订单关闭
     * **/
    @SerializedName("status")
    public String status;
    @SerializedName("statusDesc")
    public String statusDesc;
    @SerializedName("rownum")
    public String rownum;
    @SerializedName("merchantName")
    public String merchantName;
    @SerializedName("merchantIcon")
    public String merchantIcon;
    @SerializedName("payOrderNo")
    public Object payOrderNo;
    @SerializedName("merchantNo")
    public String merchantNo;
    @SerializedName("goodsName")
    public String goodsName;
    @SerializedName("channel")
    public String channel;
    @SerializedName("channelDesc")
    public String channelDesc;
    @SerializedName("mchOrderNo")
    public String mchOrderNo;


    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
        getHeaderValue ();
        getTime ();
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public boolean isLastInIt;
    public boolean isHeader;
    private String headerValue;

    public String getHeaderValue() {
        if (Util.isEmpty (headerValue)) {
            headerValue = Util.getYear_Month (tradeTime);
        }

        return headerValue;
    }

    private String getTime;

    public String getTime() {
        if (Util.isEmpty (getTime)) {
            getTime = Util.getMonth_Day_Hour_Min (tradeTime);
        }

        return getTime;
    }

    public CharSequence getPayAmount() {
        String money = Util.doublePoint (amount, 2);
        if (StatusTable.Trade.PAY.equalsIgnoreCase (tradeType)) {
            money = "-" + money;
        } else if (StatusTable.Trade.REFUND.equalsIgnoreCase (tradeType)
                || StatusTable.Trade.RECHARGE.equalsIgnoreCase (tradeType)
                ) {

            money = "<font color='#22C8D8'>+" + money + "</font>";
        } else {
            money = "-" + money;
        }
        return Html.fromHtml (money);
    }

    public int getDefaultIcon() {
        if (StatusTable.Trade.PAY.equals (tradeType)) {
//            return R.drawable.ewallet_balance_icon;
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


}
