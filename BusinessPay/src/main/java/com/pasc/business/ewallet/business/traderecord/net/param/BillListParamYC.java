package com.pasc.business.ewallet.business.traderecord.net.param;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.business.common.param.MemberNoParam;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
public class BillListParamYC extends MemberNoParam {
    @SerializedName("tradeType")
    public String tradeType; //交易类型；RECHARGE：充值，REFUND：退款；PAY：消费；WITHDRAW：提现

    @SerializedName("pageNum")
    public int pageNum;

    @SerializedName("pageSize")
    public int pageSize; //pageSize

    @SerializedName("yearOfMonth")
    public String yearOfMonth; // 年月，例： 2019-01

    @SerializedName("keyWord")
    public String keyWord;


    public BillListParamYC(String memberNo) {
        super (memberNo);
    }
}
