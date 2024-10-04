package com.whmstr.whsmk.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.chinaums.pppay.unify.UnifyPayPlugin;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.openapi.PASCPay;
import com.pasc.business.ewallet.result.PayResult;
import com.pasc.ewallet.dev.utils.PayUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
  private static final String TAG = "WXEntryActivity";
  private IWXAPI api;
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    api = WXAPIFactory.createWXAPI(this, PASCPay.getInstance().getWechatPayAppID());
    //注意：第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
    try {
      if (!api.handleIntent(getIntent(), this)) {
        finish();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override public void onReq(BaseReq baseReq) {
    LogUtil.logd(TAG, "onReq: " + baseReq.toString());
  }

  @Override public void onResp(BaseResp baseResp) {
    LogUtil.logd(TAG, "onResp-> errCode：" + baseResp.errCode + ",errStr: " + baseResp.errStr);
    if (baseResp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
      WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) baseResp;
      String extraData = launchMiniProResp.extMsg; // 对应下面小程序中的app-parameter字段的value
      finish();
      if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
        PASCPay.getInstance().notifyPayResult(PayUtil.PayType.UNION_WECHATJSAPI, PayResult.PAY_SUCCESS, "");
      } else if (baseResp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
        PASCPay.getInstance().notifyPayResult(PayUtil.PayType.UNION_WECHATJSAPI, PayResult.PAY_CANCEL, "");
      } else if (baseResp.errCode == BaseResp.ErrCode.ERR_SENT_FAILED) {
        PASCPay.getInstance().notifyPayResult(PayUtil.PayType.UNION_WECHATJSAPI, PayResult.PAY_FAILED, baseResp.errStr);
      } else {
        PASCPay.getInstance().notifyPayResult(PayUtil.PayType.UNION_WECHATJSAPI, PayResult.PAY_WAITING, baseResp.errStr);
      }
      UnifyPayPlugin.getInstance(this).getWXListener().onResponse(this, baseResp);
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    LogUtil.logd("JZB", "WXEntryActivity, onDestroy()");
  }
}