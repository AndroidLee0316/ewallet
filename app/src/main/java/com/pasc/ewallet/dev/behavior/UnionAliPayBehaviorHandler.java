package com.pasc.ewallet.dev.behavior;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.chinaums.pppay.unify.UnifyUtils;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.config.PayBehaviorHandler;
import com.pasc.business.ewallet.openapi.PASCPay;
import com.pasc.business.ewallet.result.PayResult;
import com.pasc.ewallet.dev.pay.unionpay.util.UnionPayUtil;
import com.pasc.ewallet.dev.utils.PayUtil;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhuangjiguang on 2022/4/1.
 * 银联支付宝
 */
public class UnionAliPayBehaviorHandler implements PayBehaviorHandler {

  @Override public void handlerPay(Activity activity, String payType, PayResp payBean) {
    if (UnifyUtils.hasInstalledAlipayClient(activity)) {
      JSONObject jsonObject = null;
      try {
        jsonObject = new JSONObject(payBean.returnValue);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      if (jsonObject == null) {
        //LogUtil.logd(TAG, "appPayRequest alipay jsonObject为null");
        return;
      }
      JSONObject appPayRequestObject = jsonObject.optJSONObject("appPayRequest");
      if (appPayRequestObject != null) {
        //LogUtil.logd(TAG, "appPayRequest alipay--> " + appPayRequestObject.toString());
        UnionPayUtil.payAliPayMiniPro(activity, appPayRequestObject.toString());
      }
    } else {
      Toast.makeText(activity, "未安装支付宝客户端", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onActivityResult(Activity activity, String payType, int requestCode, int resultCode,
      Intent data) {

  }

  @Override public void onNewIntent(Activity activity, String payType, Intent intent) {
    showMsg(activity, intent);
  }

  private void showMsg(Context context, Intent intent) {
    if (intent != null) {
      try {
        Uri uri = intent.getData();
        //完整路径
        String url = uri.toString();

        String errCode = uri.getQueryParameter("errCode");
        String errStr = uri.getQueryParameter("errStr");

        String str = "支付结果 ===》 errCode = " + errCode + " ------ errStr = " + errStr;
        //LogUtil.logd(TAG, "str: " + str);
        if ("0000".equals(errCode) || "2001".equals(errCode)) {
          PASCPay.getInstance().notifyPayResult(PayUtil.PayType.UNION_ALIPAYJSAPI, PayResult.PAY_SUCCESS, "");
        } else if ("1000".equals(errCode)) {
          PASCPay.getInstance().notifyPayResult(PayUtil.PayType.UNION_ALIPAYJSAPI, PayResult.PAY_CANCEL, errStr);
        } else {
          PASCPay.getInstance().notifyPayResult(PayUtil.PayType.UNION_ALIPAYJSAPI, PayResult.PAY_FAILED, errStr);
        }
      } catch (Exception e) {
        e.getStackTrace();
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(Activity activity, String payType, int requestCode,
      @NonNull String[] permissions, @NonNull int[] grantResults) {

  }

  @Override public void onDestroy(Activity activity, String payType) {

  }

}
