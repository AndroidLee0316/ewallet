package com.pasc.lib.netpay.resp;

import com.pasc.business.ewallet.NotProguard;

/**
 * 最终返回数据类型定义成Void的接口换成定义为此类类型，由于Rxjava2不支持传递null数据流了，所以之前将Void定义成最终返回数据类型的接口会报空指针异常，这不是我们想要的
 */
@NotProguard
public class VoidObject {

    private static final VoidObject INSTANCE = new VoidObject();

    public static VoidObject getInstance() {
        return INSTANCE;
    }

    private VoidObject() {
    }
}