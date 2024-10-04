package com.pasc.lib.netpay.transform;

import com.pasc.lib.netpay.resp.BaseResp;

/**
 * @author yangzijian
 * @date 2018/9/17
 * @des
 * @modify
 **/
public class NetObserverManager {
    private NetObserver netObserver;

    private NetObserverManager() {
    }

    private static class SingletonHolder {
        private static final NetObserverManager INSTANCE = new NetObserverManager();
    }
    public static NetObserverManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public synchronized void registerObserver(NetObserver netObserver) {
        this.netObserver = netObserver;
    }

    public synchronized void unRegisterObserver() {
        this.netObserver = null;
    }

    public synchronized void notifyObserver(BaseResp baseResp){
        if (netObserver!=null && baseResp!=null){
            netObserver.notifyErrorNet(baseResp);
        }
    }

}
