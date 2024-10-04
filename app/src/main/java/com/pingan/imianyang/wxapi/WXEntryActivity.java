package com.pingan.imianyang.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.openapi.PASCPay;
import com.pasc.business.ewallet.result.PayResult;
import com.pasc.business.ewallet.result.PayType;
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
    Log.e(TAG, "onReq: " + baseReq.toString());
  }

  @Override public void onResp(BaseResp baseResp) {
    Log.e(TAG, "onResp-> errCode：" + baseResp.errCode + ",errStr: " + baseResp.errStr);
    if (baseResp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
      WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) baseResp;
      String extraData = launchMiniProResp.extMsg; // 对应下面小程序中的app-parameter字段的value
      if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
        PASCPay.getInstance().notifyPayResult(StatusTable.PayType.WECHAT, PayResult.PAY_SUCCESS, "");
      } else if (baseResp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
        PASCPay.getInstance().notifyPayResult(StatusTable.PayType.WECHAT, PayResult.PAY_CANCEL, "");
      } else if (baseResp.errCode == BaseResp.ErrCode.ERR_SENT_FAILED) {
        PASCPay.getInstance().notifyPayResult(StatusTable.PayType.WECHAT, PayResult.PAY_FAILED, baseResp.errStr);
      } else {
        PASCPay.getInstance()
            .notifyPayResult(StatusTable.PayType.WECHAT, PayResult.PAY_WAITING, baseResp.errStr);
      }
      finish();
    }
  }
}