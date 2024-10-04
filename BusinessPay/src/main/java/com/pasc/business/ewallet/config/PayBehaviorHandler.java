package com.pasc.business.ewallet.config;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;

/**
 * Created by zhuangjiguang on 2020/12/1.
 */
@NotProguard
public interface PayBehaviorHandler {

  void handlerPay(Activity activity, String payType, PayResp payBean);
  void onActivityResult(Activity activity, String payType, int requestCode, int resultCode, Intent data);
  void onNewIntent(Activity activity, String payType, Intent intent);
  void onRequestPermissionsResult(Activity activity, String payType, int requestCode, @NonNull
      String[] permissions,
      @NonNull int[] grantResults);
  void onDestroy(Activity activity, String payType);
}