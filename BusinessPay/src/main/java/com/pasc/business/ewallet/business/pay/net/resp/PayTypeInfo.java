package com.pasc.business.ewallet.business.pay.net.resp;

/**
 * @date 2019-08-28
 * @des
 * @modify
 **/
public class PayTypeInfo {

    public String payTypeName;
    public String payType;
    public String unionOrderId;
    public String paydate;
    public String cardKey;

    public PayTypeInfo(String payTypeName,
                       String payType,
                       String unionOrderId, String paydate, String cardKey) {
        this.unionOrderId = unionOrderId;
        this.paydate = paydate;
        this.cardKey = cardKey;
    }
}
