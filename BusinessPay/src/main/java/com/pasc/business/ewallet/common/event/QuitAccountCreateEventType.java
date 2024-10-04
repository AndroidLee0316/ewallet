package com.pasc.business.ewallet.common.event;

/**
 * Created by ex-huangzhiyi001 on 2019/3/22.
 * 关闭，开户开始的界面---开户完成的界面 之间的activity
 */
public class QuitAccountCreateEventType implements BaseEventType{
    // 不关闭 开户协议界面
    public static final int notQuitCreate=1;
    public int quitType=0;
    public QuitAccountCreateEventType() {
    }

    public QuitAccountCreateEventType(int quitType) {
        this.quitType = quitType;
    }
}
