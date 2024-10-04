package com.pasc.business.ewallet.common.event;

import android.app.Activity;

/**
 * Created by ex-huangzhiyi001 on 2019/3/22.
 * activity接收到事件后弹出对话框
 * 必须注册eventbus才行
 */
public class TokenInvalidEventType implements BaseEventType{
    public Activity mActivity;
    //对话框显示的消息
    public String msg = "";
}
