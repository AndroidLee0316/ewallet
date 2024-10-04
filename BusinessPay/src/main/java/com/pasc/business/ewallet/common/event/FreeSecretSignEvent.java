package com.pasc.business.ewallet.common.event;

/**
 * @date 2019/12/15
 * @des
 * @modify
 **/
public class FreeSecretSignEvent implements BaseEventType{
    public int signType;
    public int signResult;

    public FreeSecretSignEvent(int signType, int signResult) {
        this.signType = signType;
        this.signResult = signResult;
    }
}
