package com.pasc.business.ewallet.common.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.OpenUnifyEvent;
import com.pasc.business.ewallet.common.event.RefreshQuickCardEvent;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;

public class PascPayWebViewActivity extends EwalletBaseActivity implements View.OnClickListener {

    private PascToolbar toolbar;
    private WebView mWebView;
    private String mTitle = "";
    private String mWebView_url;
    private RelativeLayout layout_rl;
    private boolean isError = false;
    private ProgressBar mProgressbar;
    private String interceptUrls[] = {"pascpay://jzb.bindcard.com", "pascpay://jzb.bingcard.com"};

    boolean needIntercept(String url) {
        if (url != null) {
            for (String cc : interceptUrls) {
                if (url.startsWith (cc)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isHtml(String url) {
        if (!TextUtils.isEmpty (url)) {
            if (url.contains ("</html>")) {
                return true;
            }
        }
        return false;
    }

    public void viewClick(View view) {
        mWebView.loadUrl ("pascpay://jzb.bindcard.com");
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_activity_web_view;
    }

    @Override
    protected void initView() {
        toolbar = findViewById (R.id.ewallet_activity_toolbar);
        mWebView = findViewById (R.id.ewallet_activity_web_view);
        TextView tv_retryload = findViewById (R.id.tv_retryload);
        layout_rl = findViewById (R.id.layout_rl);
        mProgressbar = findViewById (R.id.ewallet_activity_web_progressBar);
        mProgressbar.setVisibility (View.VISIBLE);
        tv_retryload.setOnClickListener (this);
    }

    @Override
    protected void initData(Bundle bundleData) {
        mWebView_url = bundleData.getString (BundleKey.Web.key_url, "");
        mTitle = bundleData.getString (BundleKey.Web.key_title, "");
        if (!TextUtils.isEmpty (mTitle)) {
            toolbar.setTitle (mTitle);
        }
        toolbar.enableUnderDivider (true);
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        initWebView ();
        showLoadingView ();

    }

    void showErrorView() {
        mProgressbar.setVisibility (View.GONE);
        layout_rl.setVisibility (View.VISIBLE);
        mWebView.setVisibility (View.GONE);
    }

    void showContentView() {
        mProgressbar.setVisibility (View.GONE);
        layout_rl.setVisibility (View.GONE);
        mWebView.setVisibility (View.VISIBLE);
    }

    void showLoadingView() {
        mProgressbar.setVisibility (View.VISIBLE);
        layout_rl.setVisibility (View.GONE);
        mWebView.setVisibility (View.GONE);

    }

    void showEmpty() {
        mProgressbar.setVisibility (View.GONE);
        layout_rl.setVisibility (View.GONE);
        mWebView.setVisibility (View.GONE);
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings ();
        settings.setCacheMode (WebSettings.LOAD_NO_CACHE);
        settings.setSupportZoom (false);
        settings.setBuiltInZoomControls (false);
        settings.setJavaScriptEnabled (true);
        settings.setDomStorageEnabled (true);
        //修复Android系统中webkit默认内置的一个searchBoxJavaBridge_ 接口存在远程代码执行漏洞
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        //删除系统内置的JavaScriptInterface，存在远程代码执行漏洞
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversal");
        //修复WebView File域同源策略绕过漏洞
        settings.setAllowFileAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        mWebView.setWebViewClient (new WebViewClient () {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed (); //信任所有证书
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted (view, url, favicon);
                logMsg ("onPageStarted:  " + url);
                if (needIntercept (url)) {
                    showEmpty ();
                    EventBusManager.getDefault ().post (new RefreshQuickCardEvent ());
                    finish ();
                    return;
                }
                showLoadingView ();

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished (view, url);
                logMsg ("onPageFinished:  " + url);
                if (needIntercept (url)) {
                    showEmpty ();
                    return;
                }
                if (!isError) {
                    showContentView ();
                    if (TextUtils.isEmpty (mTitle)) {
                        //更换title
                        String title = view.getTitle ();
                        if (!TextUtils.isEmpty (title)) {
                            toolbar.setTitle (title);
                        }
                    }
                }
                isError = false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                logMsg ("shouldOverrideUrlLoading:  " + url);
                if (url == null || url.startsWith ("http")) return false;
                try {
                    if (url.startsWith ("weixin://") //微信
                            || url.startsWith ("alipays://") //支付宝
                            || url.startsWith ("mailto://") //邮件
                            || url.startsWith ("tel://")//电话
                            || url.startsWith ("dianping://")//大众点评
                            || url.startsWith ("baiduboxapp://")
                            || url.startsWith ("baiduboxlite://")
                            || url.startsWith ("pascpay://")

                        //其他自定义的scheme
                    ) {
                        Intent intent = new Intent (Intent.ACTION_VIEW, Uri.parse (url));
                        startActivity (intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }

                //处理http和https开头的url
//                view.loadUrl (url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                logMsg ("onReceivedError:  " +description + " : " + failingUrl);
                if (failingUrl != null && needIntercept (failingUrl)) {
                    showEmpty ();
                    return;
                }
                isError = true;
                showErrorView ();
                //开通银联无跳转支付之后，返回商户需要查询开通状态
                if ("http://localhost:8080/ACPSample_WuTiaoZhuan/frontRcvResponse".equalsIgnoreCase(failingUrl)) {
                    EventBusManager.getDefault().post(new OpenUnifyEvent("0"));
                    finish();
                }
            }

//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError
//                    error) {
//                super.onReceivedError (view, request, error);
//                logMsg ("onReceivedError:  " + error.getDescription () + " : " + request.getUrl ());
//                if (request.getUrl () != null && needIntercept (request.getUrl ().toString ())) {
//                    showEmpty ();
//                    return;
//                }
//                isError = true;
//                showErrorView ();
//            }

        });
        mWebView.setWebChromeClient (new WebChromeClient () {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                String url = view.getUrl ();
                logMsg ("onProgressChanged: newProgress->  " + newProgress + " url: " + url);
                super.onProgressChanged (view, newProgress);
                if (newProgress >= 100) {
                    mProgressbar.setProgress (100);
                    mProgressbar.setVisibility (View.GONE);
                } else {
                    mProgressbar.setProgress (newProgress);
                    mProgressbar.setVisibility (View.VISIBLE);

                }
            }

        });
        mWebView.getSettings ().setDefaultTextEncodingName ("UTF-8");
        loadUrl (mWebView_url);
    }

    void loadUrl(String url) {
        if (!isHtml (url)) {
            mWebView.loadUrl (url);
            logMsg ("onCreate:  " + mWebView_url);
        } else {
//            mWebView.loadData(url, "text/html;charset=UTF-8", null);
            mWebView.loadDataWithBaseURL (null, url, "text/html", "UTF-8", null);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
    }

    @Override
    public void onClick(View v) {
        if (v.getId () == R.id.tv_retryload) {
//            if (!NetworkUtils.isNetworkAvailable()){
//                ToastUtils.toastMsg("网络错误");
//                return;
//            }
            mWebView.reload ();
        }
    }

    void logMsg(String msg) {
        LogUtil.loge (msg);

    }

}
