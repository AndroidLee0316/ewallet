package com.pasc.ewallet.dev.behavior;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import cmbapi.CMBApi;
import cmbapi.CMBApiFactory;
import cmbapi.CMBEventHandler;
import cmbapi.CMBRequest;
import cmbapi.CMBResponse;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.config.Constants;
import com.pasc.business.ewallet.config.PayBehaviorHandler;
import com.pasc.business.ewallet.openapi.PASCPay;
import com.pasc.business.ewallet.result.PayResult;
import com.pasc.ewallet.dev.pay.cmbpay.CmbPayUtil;
import com.pasc.ewallet.dev.utils.PayUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by zhuangjiguang on 2022/3/31.
 * 招行支付
 */
public class CmbPayBehaviorHandler implements PayBehaviorHandler, CMBEventHandler {
  //招商银行
  private String CMB_APP_ID = "0025000088"; //前4位是分行号，后6位是商户号
  private CMBApi mCmbApi = null;
  @Override public void handlerPay(Activity activity, String payType, PayResp payBean) {
    //创建CMBApi接口对象
    mCmbApi = CMBApiFactory.createCMBAPI(activity, CMB_APP_ID);
    mCmbApi.handleIntent(activity.getIntent(), this);

    String orderInfo = payBean.cmbchinaResponse;
    CMBRequest request = new CMBRequest();
    //支付、协议、领券等业务功能等请求参数，具体内容由业务功能给出具体内容，SDK透传，会将该字段信息Post给对应功能页面
    try {
      //SDK接口的requestData（json字符串先做url编码再进行拼接）
      request.requestData = "charset=utf-8&jsonRequestData=" + URLEncoder.encode(orderInfo, "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    //h5Url与CMBJumpUrl至少有一个赋值，app已经安装时要跳转到的招商银行APP具体功能的url
    request.CMBJumpAppUrl = CmbPayUtil.CMB_JUMP_URL;
    //h5Url与CMBJumpUrl至少有一个赋值，app没有安装时在商户APP打开H5页面
    if (Constants.IS_DEBUG) {
      request.CMBH5Url = CmbPayUtil.H5_URL_DEBUG;
    } else {
      request.CMBH5Url = CmbPayUtil.H5_URL_RELEASE;
    }
    //业务功能类型，SDK透传，支付默认上送pay
    request.method = "pay";
    mCmbApi.sendReq(request);
  }

  @Override
  public void onActivityResult(Activity activity, String payType, int requestCode, int resultCode,
      Intent data) {
    if (mCmbApi != null) {
      mCmbApi.handleIntent(data, this);
    }
  }

  @Override public void onNewIntent(Activity activity, String payType, Intent intent) {
    if (mCmbApi != null) {
      mCmbApi.handleIntent(intent, this);
    }
  }

  @Override
  public void onRequestPermissionsResult(Activity activity, String payType, int requestCode,
      @NonNull String[] permissions, @NonNull int[] grantResults) {

  }

  @Override public void onDestroy(Activity activity, String payType) {
    //为了防止内存泄露问题，当CMBApi对象不再需要时，可以通过如下方法销毁该对象
    CMBApiFactory.destroyCMBAPI();
  }

  @Override public void onResp(CMBResponse cmbResponse) {
    LogUtil.logd("CmbPayBehaviorHandler", "onResp respCode:" + cmbResponse.respCode);
    if (0 == cmbResponse.respCode){
      PASCPay.getInstance().notifyPayResult(PayUtil.PayType.CMBCHINAPAY, PayResult.PAY_SUCCESS, "");
    } else {
      PASCPay.getInstance().notifyPayResult(PayUtil.PayType.CMBCHINAPAY, PayResult.PAY_FAILED, "");
    }
  }
}
