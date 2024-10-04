package com.pasc.lib.netpay.download;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 下载管理类
 * Created by YZJ on 2017/6/8: 19.
 */
public class DownLoadManager {
    private final static String TAG = "pascDownload";
    private Context context;
    private static final Handler mHandler = new Handler (Looper.getMainLooper ());
    /*** 没有下载的状态*/
    public static final int STATE_NONE = DownloadInfo.STATE_NONE;
    /*** 等待中*/
    public static final int STATE_WAITING = DownloadInfo.STATE_WAITING;
    /*** 下载中*/
    public static final int STATE_DOWNLOADING = DownloadInfo.STATE_DOWNLOADING;
    /*** 暂停*/
    public static final int STATE_PAUSED = DownloadInfo.STATE_PAUSED;
    /*** 下载完毕*/
    public static final int STATE_FINISH = DownloadInfo.STATE_FINISH;
    /*** 下载失败*/
    public static final int STATE_ERROR = DownloadInfo.STATE_ERROR;
    /*** 已经安装 ,针对于apk*/
    public static final int STATE_INSTALLED = DownloadInfo.STATE_ERROR;
    /*** 存储下载任务*/
    private static List<DownloadInfo> downloadInfos = new ArrayList<> ();
    /*** 存储所有观察者 为什么用CopyOnWriteArrayList ,而不是 ArrayList ,可以去看看源码,ArrayList观察者频繁刷新
     * 会抛出 ConcurrentModificationException  多线程问题,用线程安全的 集合
     * ConcurrentHashMap  替代 HashMap原理一样
     * */
    private List<DownloadObserver> downloadObservers = new CopyOnWriteArrayList<> ();
    /**
     * 存储下载中的任务
     **/
    private Map<String, DownLoadTask> downLoadTasksMap = new ConcurrentHashMap<String, DownLoadTask> ();

    private DownLoadManager() {
    }

    public Context getContext() {
        return context;
    }

    private boolean isDebug = true;

    private void logMsg(String text) {
        if (isDebug)
            Log.d (TAG, text);
    }

    /***初始化上下文***/
    public void init(Context context, int corePoolSize,
                     int maximumPoolSize,
                     long keepAliveTime) {
        init (context, true, corePoolSize, maximumPoolSize, keepAliveTime);
    }

    public void init(Context context, boolean isDebug, int corePoolSize,
                     int maximumPoolSize,
                     long keepAliveTime) {
        this.context = context;
        this.isDebug = isDebug;
        ThreadPoolManager.getInstance ().init (corePoolSize, maximumPoolSize, keepAliveTime);
    }

    public static DownLoadManager getDownInstance() {
        return Single.instance;
    }

    private final static class Single{
        private static final DownLoadManager instance=new DownLoadManager ();
    }


    /******注册一个下载任务 观察者*******/
    public synchronized void registerObserver(DownloadObserver observer) {
        if (observer != null) {
            if (!downloadObservers.contains (observer)) {
                downloadObservers.add (observer);
            }
        }
    }

    /******注销一个下载任务 观察者*******/
    public synchronized void unRegisterObserver(DownloadObserver observer) {
        if (observer != null) {
            int index = downloadObservers.indexOf (observer);
            if (index != -1) {
                downloadObservers.remove (index);
            }
        }
    }

    /******注销所有观察者*******/
    public synchronized void unRegisterAll() {
        downloadObservers.clear ();
    }

    /**
     * 当下载进度 和状态发送改变的时候回调
     */
    private synchronized void notifyDownloadProgressed(final DownloadInfo item, DownloadObserver ob) {
        //单独监听
        if (ob != null) {
            post (item, ob);
        }
        /**
         * 用ArrayyList的话 java.util.ConcurrentModificationException
         */
        for (final DownloadObserver observer : downloadObservers) {
            post (item, observer);

        }

    }

    private void post(final DownloadInfo item, final DownloadObserver observer) {
        if (observer == null) {
            return;
        }
        if (Looper.myLooper () == Looper.getMainLooper ()) {
            observer.onDownloadStateProgressed (item);
        } else {
            mHandler.post (new Runnable () {
                @Override
                public void run() {

                    observer.onDownloadStateProgressed (item);
                }
            });
        }
    }

    /*****下载任务工作线程*****/
    private class DownLoadTask extends BaseLoadTask {
        private DownloadInfo info;
        private DownloadObserver observer;

        public DownLoadTask(DownloadInfo info, DownloadObserver observer) {
            /******克隆一个新的对象,最好不要在线程里持有原有的对象,导致其无法回收******/
            this.info = info.clone ();
            this.observer = observer;
        }

        @Override
        public void run() {
            info.downloadState(STATE_DOWNLOADING) ;
            notifyDownloadProgressed (info, observer);
            downLoad ();
        }

        /*******下载********/
        void downLoad() {
            if (isCancle ()) {
                /****任务已经被取消*****/
                info.downloadState(STATE_PAUSED);
                notifyDownloadProgressed (info, observer);
                return;
            }
            /******下载文件大小****/
            int fileSize = 0;
            /******已经下载的文件大小*****/
            int hasLoadSize = 0;
            FileInputStream fis = null;
            HttpURLConnection connection = null;
            InputStream is=null;
            RandomAccessFile randomAccessFile=null;
            /****是否要断点****/
            boolean resumePoint = info.isResumePoint ();

            try {
                /******用户自定义 下载文件根目录****/
                String loadpathRoot = info.getFileSavePathRoot ();
                if (TextUtils.isEmpty (loadpathRoot)) {
                    //没定义下载目录，则用默认的
                    loadpathRoot = DownloadUtil.getLoadPathRoot (context);
                }

                /*****临时下载文件后缀名为 .tmp  ,下载完成后,要记得 把 .tmp去掉*****/
                File tmpFile = new File (loadpathRoot, info.getFileName () + DownloadUtil.loadTmpName);
                if (tmpFile.exists ()) {
                    if (resumePoint) {
                        /********读取之前下载的文件*******/
                        fis = new FileInputStream (tmpFile);
                        int hasDownSize = fis.available ();
                        hasLoadSize += hasDownSize;
                    } else {
                        /***每次都重新下载，不要缓存**/
                        tmpFile.delete ();
                        hasLoadSize = 0;
                    }
                }
                logMsg ("下载地址为： " + info.getDownloadUrl ());
                URL url = new URL (info.getDownloadUrl ());
                connection = (HttpURLConnection) url.openConnection ();
                connection.setRequestMethod (info.getMethod ());
                /*****连接超时时间 1分钟*****/
                connection.setConnectTimeout (60 * 1000);
                /*****读取超时时间 1分钟*****/
                connection.setReadTimeout (60 * 1000);
                // connection.setRequestProperty("Range", "bytes=" + startIndex+ "-" + fileSize);
                /*****开启断点续传*****/
                connection.setRequestProperty ("Range", "bytes=" + hasLoadSize + "-");
                connection.setRequestProperty ("Accept-Encoding", "identity");
                if (info.isPostJson ()){
                    connection.setRequestProperty("Content-Type", "application/json");
                }
                if (info.getHeaders () != null) {
                    for (Map.Entry<String, String> entry : info.getHeaders ().entrySet ()) {
                        connection.setRequestProperty (entry.getKey (), entry.getValue ());
                    }
                }
                connection.connect ();


                DataOutputStream out=null;
                if (DownloadInfo.POST.equals (info.getMethod ())) {
                    if (info.isPostJson ()) {
                        //POST json
                        if (!TextUtils.isEmpty (info.getJsonData ())) {
                            try {
                                 out = new DataOutputStream (connection.getOutputStream ());
//                            String json = java.net.URLEncoder.encode (info.getJsonData (), "utf-8");
                                out.writeBytes (info.getJsonData ());
                                out.flush ();
                            }catch (Exception e){

                            }finally {
                                DownloadUtil.closeIo (out);
                            }

                        }

                    } else {
                        //POST 表单
                        if (info.getParams () != null && info.getParams ().size () > 0) {
                            try {
                                out = new DataOutputStream (connection
                                        .getOutputStream ());

                                // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
                                // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
                                for (Map.Entry<String, String> entry : info.getParams ().entrySet ()) {
                                    String content = entry.getKey () + "=" + URLEncoder.encode (entry.getValue (), "UTF-8");
                                    out.writeBytes (content);

                                }
                                out.flush ();
                            }catch (Exception e){

                            }finally {
                                DownloadUtil.closeIo (out);

                            }

                        }
                    }
                }

                int responseCode = connection.getResponseCode ();
                /*****是否为分块下载***/
                boolean isChunked = false;
                /****服务器请求全部资源 200   服务器请求部分资源 206 ****/
                if (responseCode == 206 || 200 == responseCode) {
                     is = connection.getInputStream ();
                    /*********** 文件大小 = 未下载完的大小 + 已经下载完的大小  ****************/
                    int contentLength = connection.getContentLength ();
                    if (contentLength <= 0) {
                        /***分块下载得到的 contentLength 始终为 -1  ，每次都重新下载，不要缓存**/
                        tmpFile.delete ();
                        hasLoadSize = 0;
                        isChunked = true;
                        //随便填一个
                        fileSize = 0;
                    } else {
                        fileSize = contentLength + hasLoadSize;

                    }
                    info.totalLength(fileSize);
                    randomAccessFile = new RandomAccessFile (tmpFile, "rw");
                    randomAccessFile.seek (hasLoadSize);
                    int tmpLen = -1;
                    /*****已经下载的文件大小*****/
                    int currentSize = hasLoadSize;
                    /*******记录上一次刷新时下载的大小******/
                    int preCurrentSize = 0;
                    byte[] buffer = new byte[1024];
                    /********* 有数据 ,并且没有被取消就是写文件数据**************/
                    while ((tmpLen = is.read (buffer)) != -1 && !isCancle ()) {
                        currentSize += tmpLen;
                        // float persent = (float) ((currentSize + 0.0) / fileSize * 100);
                        // Log.i("yzjTag", "当前大小: " + currentSize + " , 总大小: " + fileSize + " , 百分比: " + persent);
                        randomAccessFile.write (buffer, 0, tmpLen);
                        /*******当下载进度 ?大于 2048 时去刷新 ,不想刷新过于频繁 *****/
                        if (currentSize - preCurrentSize >= 4 * 1024) {
                            info.progress(currentSize) ;
                            /*******通知所有下载观察者*******/
                            notifyDownloadProgressed (info, observer);
                            preCurrentSize = currentSize;
                        }

                    }
                    /****标记任务已经取消***/
                    cancle ();
                    /***防止MessageQueue 堆积 ，导致 回掉几次 STATE_FINISH**/
                    info = info.clone ();

                    if (isChunked) {
                        fileSize = preCurrentSize;
                        info.progress (fileSize);
                        info.totalLength (fileSize);
                        info.downloadState (STATE_FINISH);
                        // 下载完成,修改后缀名,把后缀名去掉
                        DownloadUtil.removeTmpName (tmpFile);
                        stopDownload (info, observer);
                    } else {

                        /*****下载完成 ,当前下载字节刚好当于 文件大小,算是下载成功 ,否则算是下载失败******/
                        if (currentSize == fileSize) {
                            info.progress(info.getTotalLength ());
                            info.downloadState (STATE_FINISH) ;
                            // 下载完成,修改后缀名,把后缀名去掉
                            DownloadUtil.removeTmpName (tmpFile);
                            stopDownload (info, observer);
                        } else if (currentSize > fileSize) {
                            /***下载错误 ,下载字节大小已经 大于文件大小 ,把文件删除,要重新下载***/
                            info.downloadState(STATE_ERROR) ;
                            tmpFile.delete ();
                            info.progress (0);
                            stopDownload (info, observer);
                        } else {
                            /******暂停*******/
                            info.downloadState (STATE_PAUSED);
                            stopDownload (info, observer);
                        }
                    }

                } else {
                    logMsg ("下载错误，错误码为： " + responseCode);
                    /****网络异常取消任务****/
                    cancle ();
                    info.downloadState (STATE_ERROR) ;
                    /******,同时从下载队列移除*********/
                    stopDownload (info, observer);
                }
            } catch (Exception e) {
                logMsg ("下载异常： " + e.getMessage ());

                info = info.clone ();
                /***下载异常 没有被取消且重试次数要大于一 则开启重试, **/
                if (!isCancle () && getRetryTimes () >= 1) {
                    /****重试次数减一***/
                    reduceRetryTimes ();
                    if (connection != null) {
                        connection.disconnect ();
                        connection = null;
                    }
                    DownloadUtil.closeIo (fis);
                    downLoad ();
                } else {
                    /****网络异常取消任务****/
                    cancle ();
                    info.downloadState (STATE_ERROR);
                    /******,同时从下载队列移除*********/
                    stopDownload (info, observer);
                }
            } finally {
                cancle ();
                if (connection != null) {
                    connection.disconnect ();
                    connection = null;
                }
                DownloadUtil.closeIo (is);
                DownloadUtil.closeIo (randomAccessFile);

                DownloadUtil.closeIo (fis);

            }
        }
    }

    /*****取消所有下载任务*******/
    public synchronized void cancelAllTask() {
        for (DownLoadTask loadTask : downLoadTasksMap.values ()) {
            if (loadTask != null) {
                /******取消对应的任务*******/
                ThreadPoolManager.getInstance ().cancel (loadTask);
            }
        }
        unRegisterAll ();
    }

    /*****已经在下载任务列表中*******/
    public synchronized boolean hasInDownload(DownloadInfo item) {
        if (item == null) {
            logMsg ("DownloadInfo can't be null");
            return false;
        }
        if (TextUtils.isEmpty (item.getDownloadUrl ())) {
            logMsg ("download url can't be null");
            return false;
        }
        if (downLoadTasksMap.containsKey (item.getDownloadUrl ())) {
            DownLoadTask downLoadTask = downLoadTasksMap.get (item.getDownloadUrl ());
            return downLoadTask != null && !downLoadTask.isCancle ();
        }

        return false;

    }

    public synchronized void startDownload(DownloadInfo item) {
        startDownload (item, null);
    }

    /*****开始下载一个任务*******/
    /**
     * @param item
     * @param observer 单独监听下载
     */
    public synchronized void startDownload(final DownloadInfo item, final DownloadObserver observer) {
        if (item == null) {
            logMsg ("DownloadInfo can't be null");
            return;
        }
        if (TextUtils.isEmpty (item.getDownloadUrl ())) {
            logMsg ("download url can't be null");
            return;
        }
        if (context != null) {
            /***文件已经下载过了***/
            File file = new File (item.getFilePath (context));
            if (file.exists ()) {
                logMsg("file " + file.getAbsolutePath () + " is exists");
                item.downloadState (STATE_FINISH);
                notifyDownloadProgressed (item, observer);
                return;
            }
        }


        /*******先检查任务是否已经存在*******/
        if (downLoadTasksMap.containsKey (item.getDownloadUrl ())) {
            DownLoadTask downLoadTask = downLoadTasksMap.get (item.getDownloadUrl ());
            if (downLoadTask != null && !downLoadTask.isCancle ()) {
                /****,并且没有被取消,则 return******/
                return;
            } else {
                /****,任务取消,则先移除这个任务******/
                ThreadPoolManager.getInstance ().cancel (downLoadTask);
            }
        }

        /*******开启新的下载任务*********/
        DownLoadTask downLoadTask = new DownLoadTask (item, observer);
        /*****放入任务集合*****/
        downLoadTasksMap.put (item.getDownloadUrl (), downLoadTask);
        ThreadPoolManager.getInstance ().execute (downLoadTask);
        item.downloadState (STATE_WAITING);
        notifyDownloadProgressed (item, observer);


    }

    public synchronized void stopDownload(DownloadInfo item) {
        stopDownload (item, null);
    }

    /*** 暂停或者取消任务*/
    public synchronized void stopDownload(DownloadInfo item, final DownloadObserver observer) {
        if (item != null) {// 修改下载状态
            notifyDownloadProgressed (item, observer);
            DownLoadTask task = downLoadTasksMap.remove (item.getDownloadUrl ());// 先从集合中找出下载任务
            if (task != null) {
                ThreadPoolManager.getInstance ().cancel (task);// 然后从线程池中移除
            }
        }

    }

}
