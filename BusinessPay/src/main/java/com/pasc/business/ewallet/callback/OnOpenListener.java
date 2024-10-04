package com.pasc.business.ewallet.callback;

import com.pasc.business.ewallet.NotProguard;

/**
 * 打开钱包首页的回调Listener
 *
 * Created by qinguohuai on 2019/3/26.
 */
@NotProguard
public abstract class OnOpenListener {

    public  void onStart(){

    }

    public void onEnd(){

    }
    // 回调合并为同一个方法
    public abstract void onOpenResult(int code, String msg);

}
