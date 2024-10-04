package com.pasc.lib.netpay.download;

/**
 * @author yangzijian
 * @date 2018/7/16
 * @des  下载观察者
 * @modify
 **/
public interface DownloadObserver {
    void onDownloadStateProgressed(DownloadInfo updateInfo);

}
