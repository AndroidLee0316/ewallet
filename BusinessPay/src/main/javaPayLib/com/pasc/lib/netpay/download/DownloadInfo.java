package com.pasc.lib.netpay.download;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by YZJ on 2017/6/8: 19.
 */
public class DownloadInfo implements Serializable {
    /*** 没有下载的状态*/
    public static final int STATE_NONE = 0;
    /*** 等待中*/
    public static final int STATE_WAITING = 1;
    /*** 下载中*/
    public static final int STATE_DOWNLOADING = 2;
    /*** 暂停*/
    public static final int STATE_PAUSED = 3;
    /*** 下载完毕*/
    public static final int STATE_FINISH = 4;
    /*** 下载失败*/
    public static final int STATE_ERROR = 5;
    /*** 已经安装 ,针对于apk*/
    public static final int STATE_INSTALLED = 6;
    /**** 下载状态*****/
    private int downloadState = DownLoadManager.STATE_NONE;// 下载的状态
    /*****下载连接****/
    private String downloadUrl;
    /****下载文件名 test.apk test.mp4****/
    private String fileName;
    /*****用户自定义 下载文件保存路径文件夹****/
    private String fileSavePathRoot;
    /****当前下载状态***/
    private int progress;
    /****文件的长度****/
    private int totalLength;
    /***文件的icon*****/
    private String icon;
    /***是否需要断点续传****/
    private boolean resumePoint = true;

    /***头信息**/
    private Map<String, String> headers;
    /***post 表单参数**/
    private Map<String, String> params;
    /***post json**/
    private String jsonData;
    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String POST_JSON = "POST_JSON";

    private String method = GET;

    public boolean isPostJson() {
        return POST_JSON.equals (method);
    }

    public String getMethod() {

        if (POST_JSON.equals (method)) {
            return POST;
        } else if (POST.equals (method)) {
            return POST;
        } else if (GET.equals (method)) {
            return GET;
        }

        return GET;
    }

    /****获取进度 0 - 100***/
    public int getPercent() {
        int percent = 0;
        if (progress < totalLength) {
            percent = (int) ((progress + 0.0f) / totalLength * 100);
        } else if (totalLength == 0) {
            percent = 0;
        } else {
            percent = 100;
        }

        return percent;
    }

    /**
     * 获取文件下载路径
     *
     * @param context
     * @return
     */
    public String getFilePath(Context context) {
        /******用户自定义 下载文件根目录****/
        String fileRoot = fileSavePathRoot;
        if (!isValidDirectory (fileRoot)) {
            //没定义下载目录，则用默认的
            fileRoot = DownloadUtil.getLoadPathRoot (context);
        }
        File tmpFile = new File (fileRoot, fileName);
        return tmpFile.getAbsolutePath ();
    }

    public DownloadInfo() {
    }
    private boolean isValidDirectory(String fileRoot) {
        if (fileRoot != null && !TextUtils.isEmpty (fileRoot.trim ())) {
            File file = new File (fileRoot);
            if (!file.exists ()) {
                file.mkdirs ();
            }
            return file.isDirectory ();
        }
        return false;
    }


    public DownloadInfo downloadState(int downloadState) {
        this.downloadState = downloadState;
        return this;
    }

    public DownloadInfo downloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    public DownloadInfo fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public DownloadInfo fileSavePathRoot(String fileSavePathRoot) {
        this.fileSavePathRoot = fileSavePathRoot;
        if (TextUtils.isEmpty (this.fileSavePathRoot) && DownLoadManager.getDownInstance ().getContext () != null) {
            //没定义下载目录，则用默认的
            if (!isValidDirectory (fileSavePathRoot)) {
                this.fileSavePathRoot = DownloadUtil.getLoadPathRoot (DownLoadManager.getDownInstance ().getContext ());
            }
        }
        return this;
    }

    public DownloadInfo progress(int progress) {
        this.progress = progress;
        return this;
    }

    public DownloadInfo totalLength(int totalLength) {
        this.totalLength = totalLength;
        return this;
    }

    public DownloadInfo icon(String icon) {
        this.icon = icon;
        return this;
    }

    public DownloadInfo resumePoint(boolean resumePoint) {
        this.resumePoint = resumePoint;
        return this;
    }

    public DownloadInfo params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public DownloadInfo headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public DownloadInfo jsonData(String jsonData) {
        this.jsonData = jsonData;
        return this;
    }

    public DownloadInfo method(String method) {
        if (GET.equals (method)) {
        } else if (POST.equals (method)) {
        } else if (POST_JSON.equals (method)) {
        }else {
            method = GET;
        }
        this.method = method;
        return this;
    }

    /*****克隆一个新对象*****/
    public DownloadInfo clone() {
        DownloadInfo info = new DownloadInfo ()
                .downloadState (downloadState).downloadUrl (downloadUrl).fileName (fileName)
                .fileSavePathRoot (fileSavePathRoot).progress (progress).totalLength (totalLength)
                .icon (icon).resumePoint (resumePoint).params (params).headers (headers).method (method).jsonData (jsonData);
        return info;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getJsonData() {
        return jsonData;
    }

    public int getProgress() {
        return progress;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public String getFileName() {
        return fileName;
    }

    public String getIcon() {
        return icon;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getFileSavePathRoot() {
        return fileSavePathRoot;
    }

    public boolean isResumePoint() {
        return resumePoint;
    }

    /****下载DownloadInfo 更新目标 DownloadInfo****/
    public void updateDownloadInfo(DownloadInfo updateInfo) {
        if (this == updateInfo) {
            return;
        }
        if (this.equals (updateInfo)) {
            downloadState = updateInfo.getDownloadState ();
            downloadUrl = updateInfo.getDownloadUrl ();
            fileName = updateInfo.getFileName ();
            fileSavePathRoot = updateInfo.getFileSavePathRoot ();
            progress = updateInfo.getProgress ();
            totalLength = updateInfo.getTotalLength ();
            icon = updateInfo.getIcon ();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof DownloadInfo) {
            DownloadInfo newObj = (DownloadInfo) obj;
            return equals (newObj.getDownloadUrl (), downloadUrl);
        }
        return super.equals (obj);
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals (b));
    }
}
