package com.pasc.business.ewallet.common.event;

/**
 * @date 2019/7/30
 * @des
 * @modify
 **/
public class ThirdPayEvent implements BaseEventType{
    public String payType;
    public int payStatus;
    public String payMsg;

    public ThirdPayEvent(String payType, int payStatus, String payMsg) {
        this.payType = payType;
        this.payStatus = payStatus;
        this.payMsg = payMsg;
    }
}
