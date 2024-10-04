package com.pasc.ewallet.dev.behavior;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.config.PayBehaviorHandler;
import com.pasc.business.ewallet.openapi.PASCPay;
import com.pasc.business.ewallet.result.PayResult;
import com.pasc.ewallet.dev.model.MiniProgramResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by zhuangjiguang on 2022/4/1.
 * 微信小程序支付
 */
public class MiniProgramPayBehaviorHandler implements PayBehaviorHandler {

  @Override public void handlerPay(Activity activity, String payType, PayResp payBean) {
    String orderInfo = payBean.returnValue;
    Gson gson = new Gson();
    MiniProgramResp miniProgramResp = gson.fromJson(orderInfo, MiniProgramResp.class);
    String appId = miniProgramResp.getSkipAppId(); // 填应用AppId
    IWXAPI api = WXAPIFactory.createWXAPI(activity, appId);
    WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
    req.userName = miniProgramResp.getSkipMiniprogramAppId(); // 填小程序原始id
    req.path = miniProgramResp.getPath();   //拉起小程序页面的可带参路径，不填默认拉起小程序首页
    req.miniprogramType = miniProgramResp.getMiniprogramType();// 可选打开 开发版，体验版和正式版
    api.sendReq(req);
  }

  @Override
  public void onActivityResult(Activity activity, String payType, int requestCode, int resultCode,
      Intent data) {
    if (data == null) {
      return;
    }
    String respCode = data.getExtras().getString("resultCode");
    if (!TextUtils.isEmpty(respCode) && respCode.equalsIgnoreCase("success")) {
      //标示支付成功
      PASCPay.getInstance().notifyPayResult(payType, PayResult.PAY_SUCCESS, "");
    } else {
      //其他状态NOPAY状态：取消支付，未支付等状态
      PASCPay.getInstance().notifyPayResult(payType, PayResult.PAY_FAILED, "");
    }
  }

  @Override public void onNewIntent(Activity activity, String payType, Intent intent) {

  }

  @Override
  public void onRequestPermissionsResult(Activity activity, String payType, int requestCode,
      @NonNull String[] permissions, @NonNull int[] grantResults) {

  }

  @Override public void onDestroy(Activity activity, String payType) {

  }

}
