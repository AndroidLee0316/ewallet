package com.pasc.business.ewallet.business.bankcard.net.resp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @date 2019/8/16
 * @des
 * @modify
 **/
public class QuickCardResp {
    @SerializedName("list")
    public List<QuickCardBean> list;
}
