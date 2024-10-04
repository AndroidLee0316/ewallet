package com.pasc.business.ewallet.callback;


import com.pasc.business.ewallet.NotProguard;

import java.util.Map;

/**
 * 支付回调Listener
 * <p>
 * Created by qinguohuai on 2019/2/26.
 */
@NotProguard
public abstract class OnPayListener {

    public void onStartPay() {
    }

    public abstract void onPayResult(int code, String msg);

    public void onOption(String type, Map<String,String> extData){

    }

}
