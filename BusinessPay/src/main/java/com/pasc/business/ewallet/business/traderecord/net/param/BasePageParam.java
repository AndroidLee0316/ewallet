package com.pasc.business.ewallet.business.traderecord.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public class BasePageParam {

    public BasePageParam(int pageNum,int pageSize) {
        this.pageNum = pageNum+"";
        this.pageSize = pageSize+"";
    }

    @SerializedName ("pageSize")
    public String pageSize;
    /***当前页，从1开始***/
    @SerializedName ("pageNum")
    public String pageNum;

}
