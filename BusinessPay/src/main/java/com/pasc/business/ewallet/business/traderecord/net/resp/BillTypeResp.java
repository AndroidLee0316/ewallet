package com.pasc.business.ewallet.business.traderecord.net.resp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @date 2019/7/9
 * @des
 * @modify
 **/
public class BillTypeResp {
    @SerializedName("payBillTypeList")
    public List<PayBillTypeBean> payBillTypeList;

    public static class PayBillTypeBean {
        /**
         * id : consectetur sunt
         * typeName : dolore mollit et ullamco enim
         */
        @SerializedName("id")
        public String id;
        @SerializedName("typeName")
        public String typeName;

        public PayBillTypeBean(String id, String typeName) {
            this.id = id;
            this.typeName = typeName;
        }

    }
}
