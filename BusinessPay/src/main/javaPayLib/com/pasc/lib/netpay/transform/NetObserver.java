package com.pasc.lib.netpay.transform;

import com.pasc.lib.netpay.resp.BaseResp;

/**
 * @author yangzijian
 * @date 2018/9/17
 * @des
 * @modify
 **/
public interface NetObserver {
    void notifyErrorNet(BaseResp baseResp);
}
