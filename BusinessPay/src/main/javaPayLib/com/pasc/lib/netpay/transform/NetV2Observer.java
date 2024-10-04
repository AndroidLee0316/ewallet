package com.pasc.lib.netpay.transform;

import com.pasc.lib.netpay.resp.BaseV2Resp;

/**
 * @author yangzijian
 * @date 2018/9/17
 * @des
 * @modify
 **/
public interface NetV2Observer {
    void notifyErrorNet(BaseV2Resp baseResp);
}
