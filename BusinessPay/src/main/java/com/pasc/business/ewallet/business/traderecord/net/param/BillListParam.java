package com.pasc.business.ewallet.business.traderecord.net.param;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.business.StatusTable;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
public class BillListParam {
    @SerializedName("memberNo")
    public String memberNo; //会员号

    @SerializedName("tradeType")
    public String tradeType; //交易类型；RECHARGE：充值，REFUND：退款；PAY：消费；WITHDRAW：提现

    @SerializedName("lastOrderNo")
    public String lastOrderNo; //最后一笔订单的序号（rownum）

    @SerializedName("limit")
    public int limit; //pageNum

    @SerializedName("yearOfMonth")
    public String yearOfMonth; // 年月，例： 2019-01

    @SerializedName("status")
    public String status= StatusTable.PayStatus.SUCCESS; //必须 订单状态；SUCCESS-成功；FAIL-失败；PROCESSING-进行中；CLOSE-订单关闭

    @SerializedName("keyWord")
    public String keyWord;




}
