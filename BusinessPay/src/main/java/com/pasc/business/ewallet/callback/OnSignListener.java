package com.pasc.business.ewallet.callback;

import com.pasc.business.ewallet.NotProguard;

/**
 * 签约回调Listener
 * <p>
 * Created by zhuangjiguang on 2019/12/15.
 */
@NotProguard
public abstract class OnSignListener {

    public void onStartSign() {
    }

    public abstract void onSignResult(int code, String msg);

}
