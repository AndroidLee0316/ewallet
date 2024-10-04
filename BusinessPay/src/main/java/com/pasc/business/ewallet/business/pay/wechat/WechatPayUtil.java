package com.pasc.business.ewallet.business.pay.wechat;

import android.content.Context;
import android.text.TextUtils;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.util.HashMap;
import org.json.JSONException;


public class WechatPayUtil {
    //微信支付APPID
    private static String WECHAT_PAY_APPID = "";
    private static String WECHAT_PAY_APPID_CURRENT = "";

    public static String originWxAppId() {
        return WECHAT_PAY_APPID;
    }

    public static String currentWxAppId() {
        return WECHAT_PAY_APPID_CURRENT;
    }

    public static void initWxAppId(String wxAppId) {
        if (!TextUtils.isEmpty (wxAppId)) {
            WECHAT_PAY_APPID = wxAppId;
            WECHAT_PAY_APPID_CURRENT = wxAppId;
        }
    }

    public static void updateCurrentWxAppId(String wxAppId) {
        if (!TextUtils.isEmpty (wxAppId)) {
            WECHAT_PAY_APPID_CURRENT = wxAppId;
        }
    }


    /**
     * 初始化微信Appid并registerApp
     *
     * @param context
     * @param wxPayAppId
     * @return
     */
    public static boolean registerWxAppId(Context context, String wxPayAppId) {
        try {
            final IWXAPI msgApi = WXAPIFactory.createWXAPI (context, wxPayAppId);
            // 将该app注册到微信
            msgApi.registerApp (wxPayAppId);
            return true;
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return true;
    }

    /**
     * 调起微信免密签约
     */
    public static boolean sign(Context context, String preEntrustwebId) {
        String appId = originWxAppId ();
        IWXAPI wxapi = WXAPIFactory.createWXAPI (context, appId);
        // 将该app注册到微信
        wxapi.registerApp (appId);

        com.tencent.mm.opensdk.modelbiz.WXOpenBusinessWebview.Req req = new com.tencent.mm.opensdk.modelbiz.WXOpenBusinessWebview.Req();
        req.businessType = 12;//固定值
        HashMap<String, String> queryInfo = new HashMap<>();
        queryInfo.put("pre_entrustweb_id", preEntrustwebId);
        req.queryInfo = queryInfo;
        boolean success = wxapi.sendReq(req);
        return success;
    }

    /**
     * 调起微信支付
     */
    public static boolean pay(Context context, PayResp payBean) {
        String appId = payBean.appid;
        if (TextUtils.isEmpty (appId) || TextUtils.isEmpty (appId.replace ("", " "))) {
            appId = originWxAppId ();
        }
        updateCurrentWxAppId (appId);
        IWXAPI wxapi = WXAPIFactory.createWXAPI (context, appId);
        // 将该app注册到微信
        wxapi.registerApp (appId);

        final PayReq request = new PayReq ();
        request.appId = appId;
        request.partnerId = payBean.partnerid;
        request.prepayId = payBean.prepayid;
        request.packageValue = payBean.packageValue;
        request.nonceStr = payBean.noncestr;
        request.timeStamp = payBean.timestamp;
        request.sign = payBean.sign;
        boolean success = wxapi.sendReq (request);
        return success;
    }

    /**
     * 判断手机是否安装了微信
     *
     * @param context
     * @return
     * @throws JSONException
     */
    public static boolean isWechatInstalled(Context context) {
        IWXAPI api = WXAPIFactory.createWXAPI (context, currentWxAppId ());
        return api.isWXAppInstalled ();
    }

}
