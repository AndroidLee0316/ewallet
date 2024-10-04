package com.pasc.business.ewallet.business.pay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.QuitRechargeEvent;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.inner.PayManager;
import com.pasc.business.ewallet.result.PASCPayResult;

public class PayResultFailedActivity extends EwalletBaseActivity {


    private TextView mPayInfoTV;
    private boolean needCallback=false;

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_pay_result_failed;
    }

    @Override
    protected void initView() {
        mPayInfoTV = findViewById (R.id.ewallet_activity_pay_result_failed_value);
        TextView checkOrderTV = findViewById (R.id.ewallet_activity_pay_result_failed_check_order_btn);

        checkOrderTV.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                callBack ();
                finish ();
            }
        });
        // 关闭充值界面
        EventBusManager.getDefault ().post (new QuitRechargeEvent ());

    }

    @Override
    protected void initData(Bundle bundleData) {
        QueryOrderResp queryPayResultResp = (QueryOrderResp) bundleData.getSerializable (BundleKey.Pay.key_query_order_resp);
        if (queryPayResultResp == null) {
            callBack ();
            finish ();
            return;
        }
        needCallback=queryPayResultResp.needCallback;
        mPayInfoTV.setText (queryPayResultResp.statusDesc);
    }

    /**
     * 该页面屏蔽返回按钮
     */
    @Override
    public void onBackPressed() {
        callBack ();
        super.onBackPressed ();
    }

    private void callBack() {
        if (needCallback) {
            if (PayManager.getInstance ().getOnPayListener () != null) {
                PayManager.getInstance ().getOnPayListener ().onPayResult (PASCPayResult.PASC_PAY_CODE_FAILED, PASCPayResult.PASC_PAY_MSG_FAILED);
            }
            LogUtil.loge ("PayResultFailedActivity - >"+PASCPayResult.PASC_PAY_MSG_FAILED);

        }

    }
}
