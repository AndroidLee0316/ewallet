package cn.gov.dg.iguanjia.app.prd.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.openapi.PASCPay;
import com.pasc.business.ewallet.result.PayResult;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

	private static final String TAG = "wxPayTag";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ewallet_wechatpay_result);

		api = WXAPIFactory.createWXAPI(this, PASCPay.getInstance().getWechatPayAppID());
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
        Log.e (TAG, "onReq: "+req.toString () );
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.e (TAG, "onResp-> errCode："+resp.errCode +",errStr: "+resp.errStr);

		if(resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
			if(resp.errCode==BaseResp.ErrCode.ERR_OK){
				PASCPay.getInstance().notifyPayResult(StatusTable.PayType.WECHAT, PayResult.PAY_SUCCESS, "");
			}else if (resp.errCode==BaseResp.ErrCode.ERR_USER_CANCEL){
				PASCPay.getInstance().notifyPayResult(StatusTable.PayType.WECHAT, PayResult.PAY_CANCEL, "");
			}else if (resp.errCode==BaseResp.ErrCode.ERR_SENT_FAILED){
				PASCPay.getInstance().notifyPayResult(StatusTable.PayType.WECHAT, PayResult.PAY_FAILED, resp.errStr);
			}else {
				PASCPay.getInstance().notifyPayResult(StatusTable.PayType.WECHAT, PayResult.PAY_WAITING, resp.errStr);
			}
			finish();
		}
	}
}