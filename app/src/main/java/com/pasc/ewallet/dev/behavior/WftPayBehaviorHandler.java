package com.pasc.ewallet.dev.behavior;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.config.PayBehaviorHandler;
import com.pasc.business.ewallet.openapi.PASCPay;
import com.pasc.business.ewallet.result.PayResult;
import com.swiftfintech.pay.MainApplication;
import com.swiftfintech.pay.activity.PayPlugin;
import com.swiftfintech.pay.bean.RequestMsg;

/**
 * Created by zhuangjiguang on 2022/4/1.
 * 威富通
 */
public class WftPayBehaviorHandler implements PayBehaviorHandler {

  @Override public void handlerPay(Activity activity, String payType, PayResp payBean) {
    RequestMsg msg = new RequestMsg();
    msg.setTokenId(payBean.swiftResponse);
    msg.setAppId("wx174a5f32c5770a12"); //传入微信开放平台生成的应用ID
    msg.setMiniProgramId("gh_4d85c1138220"); //传入小程序原始Id
    msg.setMiniProgramType(0); //小程序开发模式：0正式，1开发，2体验
    msg.setIsBack("1"); //微信开放平台移动应用没有配置包名的时候isBack配置为0，否则为1
    msg.setTradeType(MainApplication.PAY_MINI_PROGRAM);
    msg.setPathVersion("1"); //小程序版本,旧版本设置为0，默认为1
    PayPlugin.unifiedH5Pay(activity, msg);
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
