package com.pasc.business.ewallet.business.traderecord.net.resp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019/8/1
 * @des
 * @modify
 **/
public class BillListResp {
    /**
     * isMore : true
     * orders : {"orderNo":"ipsum officia","tradeType":"sunt ea in quis sit","amount":"sit magna dolore","tradeTime":"commodo qui reprehenderit consequat","status":"tempor","rownum":"in reprehenderit Excepteur enim et","merchantIcon":"elit ullamco nostrud","goodsName":"velit sint cupidatat minim"}
     */
    public boolean isYc = false;

    @SerializedName("isMore")
    public boolean isMore; // 是否有更多数据

    @SerializedName("hasNextPage")
    public boolean hasNextPage; // 是否有更多数据

    @SerializedName("orders")
    public List<BillBean> orders;

    @SerializedName("list")
    public List<BillBean> list;

    public boolean hasMore(){

        if (isYc){
            return hasNextPage;
        }else {
            return isMore;
        }
    }

    public List<BillBean> getOrders() {

        if (isYc) {
            if (list == null) {
                list = new ArrayList<> ();
                return list;
            }else {
                return list;
            }
        } else {
            if (orders == null) {
                orders = new ArrayList<> ();
                return orders;
            }else {
                return orders;
            }
        }

    }
}
