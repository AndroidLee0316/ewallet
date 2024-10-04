package com.pasc.business.ewallet.business.traderecord.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public class AvailBalanceListParam extends BasePageParam {
    @SerializedName ("accessUserId")
    public String accessUserId;
    @SerializedName ("inChannelId")
    public String inChannelId;
    public AvailBalanceListParam(String accessUserId,String inChannelId, int pageNum,int pageSize) {
        super (pageNum, pageSize);
        this.accessUserId=accessUserId;
        this.inChannelId=inChannelId;

    }
}
