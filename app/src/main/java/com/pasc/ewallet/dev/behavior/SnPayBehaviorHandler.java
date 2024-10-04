package com.pasc.ewallet.dev.behavior;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.config.PayBehaviorHandler;
import com.pasc.business.ewallet.openapi.PASCPay;
import com.pasc.business.ewallet.result.PayResult;
import com.pasc.ewallet.dev.pay.snpay.SNPayListener;
import com.pasc.ewallet.dev.pay.snpay.SNPayUtil;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * Created by zhuangjiguang on 2022/3/31.
 * 苏宁支付
 */
public class SnPayBehaviorHandler implements PayBehaviorHandler {

  @Override public void handlerPay(Activity activity, String payType, PayResp payBean) {
    //苏宁
    String orderInfo = payBean.suningResponse;
    SNPayUtil.payV2 (activity, orderInfo, new SNPayListener() {
      @Override
      public void snPaySuccess(String msg) {
        PASCPay.getInstance().notifyPayResult(payType, PayResult.PAY_SUCCESS, "");
      }

      @Override
      public void snPayError(String msg, boolean isCancel) {
        if (!isCancel) {
          ToastUtils.toastMsg (msg);
        }

      }
    });
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
