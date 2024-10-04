package com.pasc.ewallet.dev.behavior;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.ewallet.dev.pay.unionpay.util.UnionPayUtil;
import com.pasc.business.ewallet.config.PayBehaviorHandler;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhuangjiguang on 2022/4/1.
 * 银联微信
 */
public class UnionWechatPayBehaviorHandler implements PayBehaviorHandler {

  @Override public void handlerPay(Activity activity, String payType, PayResp payBean) {
    JSONObject jsonObject = null;
    try {
      jsonObject = new JSONObject(payBean.returnValue);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    if (jsonObject == null) {
      //LogUtil.logd(TAG, "appPayRequest wechat jsonObject为null");
      return;
    }
    JSONObject appPayRequestObject = jsonObject.optJSONObject("appPayRequest");
    if (appPayRequestObject != null) {
      //LogUtil.logd(TAG, "appPayRequest wechat--> " + appPayRequestObject.toString());
      UnionPayUtil.payWX(activity, appPayRequestObject.toString());
    }
  }

  @Override
  public void onActivityResult(Activity activity, String payType, int requestCode, int resultCode,
      Intent data) {

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
