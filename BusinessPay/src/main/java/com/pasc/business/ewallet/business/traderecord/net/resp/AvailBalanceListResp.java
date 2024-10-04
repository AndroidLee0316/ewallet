package com.pasc.business.ewallet.business.traderecord.net.resp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public class AvailBalanceListResp {
    @SerializedName("balanceList")
    public List<AvailBalanceBean> balanceList;
}
