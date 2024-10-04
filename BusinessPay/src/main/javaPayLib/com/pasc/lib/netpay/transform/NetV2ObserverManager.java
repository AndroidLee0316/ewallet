package com.pasc.lib.netpay.transform;

import com.pasc.lib.netpay.resp.BaseV2Resp;

/**
 * @author yangzijian
 * @date 2018/9/17
 * @des
 * @modify
 **/
public class NetV2ObserverManager {
    private NetV2Observer netObserver;

    private NetV2ObserverManager() {
    }

    private static class SingletonHolder {
        private static final NetV2ObserverManager INSTANCE = new NetV2ObserverManager();
    }

    public static NetV2ObserverManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized void registerObserver(NetV2Observer netObserver) {
        this.netObserver = netObserver;
    }

    public synchronized void unRegisterObserver() {
        this.netObserver = null;
    }

    public synchronized void notifyObserver(BaseV2Resp baseResp){
        if (netObserver!=null && baseResp!=null){
            netObserver.notifyErrorNet(baseResp);
        }
    }

}
