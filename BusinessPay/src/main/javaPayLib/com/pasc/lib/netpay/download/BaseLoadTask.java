package com.pasc.lib.netpay.download;

/**
 * Created by YZJ on 2017/6/8: 21.
 *   下载任务基类
 */
public abstract class BaseLoadTask implements Runnable {
    /***是否任务已经取消****/
    private boolean cancle = false;
    /***下载出错任务重复次数 (主要是网络异常导致的,并且没有取消)****/
    private int retryTimes = 0;
    public boolean isCancle(){
        return cancle;
    }
    /****取消下载任务****/
    public void cancle(){
        cancle = true;
    }
    public void reduceRetryTimes(){
        if (retryTimes<=0){
            return;
        }
        retryTimes--;
    }
    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }
}
