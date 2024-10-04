package com.pasc.business.ewallet.business.pay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.QuitRechargeEvent;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.inner.PayManager;
import com.pasc.business.ewallet.result.PASCPayResult;

public class PayResultSuccessActivity extends EwalletBaseActivity {

    private ImageView mIconIV;
    private TextView mResultStatusTV;
    private TextView mResultNumTV;
    private TextView mPayTypeTV;
    //private TextView mPayToTV;

    private QueryOrderResp mQueryPayResultResp;
    private boolean needCallback;

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_pay_result_success;
    }

    boolean isSuccess() {
        return StatusTable.PayStatus.SUCCESS.equalsIgnoreCase (mQueryPayResultResp.status);
    }


    @Override
    protected void initView() {

        mIconIV = findViewById (R.id.ewallet_activity_pay_result_success_icon);
        mResultStatusTV = findViewById (R.id.ewallet_activity_pay_result_success_title);
        mResultNumTV = findViewById (R.id.ewallet_activity_pay_result_success_num);
        TextView finishTV = findViewById (R.id.ewallet_activity_pay_result_success_commit_btn);
        mPayTypeTV = findViewById (R.id.ewallet_activity_pay_result_success_type_value_tv);
        //mPayToTV = findViewById (R.id.ewallet_activity_pay_result_success_num_value_tv);

        finishTV.setOnClickListener (new View.OnClickListener () {
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
        mQueryPayResultResp = (QueryOrderResp) bundleData.getSerializable (BundleKey.Pay.key_query_order_resp);
        if (mQueryPayResultResp == null) {
            callBack ();
            finish ();
            return;
        }
        needCallback=mQueryPayResultResp.needCallback;
        if (isSuccess ()) {
            mIconIV.setImageResource (R.drawable.ewallet_ic_pay_result_success);
            mResultStatusTV.setText (R.string.ewallet_pay_success);
        } else {
            mIconIV.setImageResource (R.drawable.ewallet_wait_bill_status);
//            mResultStatusTV.setText (R.string.ewallet_pay_result_waitting);
            mResultStatusTV.setText ("支付中");

        }
        //mPayToTV.setText (mQueryPayResultResp.merchantName);
        mResultNumTV.setText ("¥" + Util.doublePoint (mQueryPayResultResp.amount, 2));
        if (!Util.isEmpty (mQueryPayResultResp.channelDesc)){
            mPayTypeTV.setText (mQueryPayResultResp.channelDesc);
        } else if (StatusTable.PayType.WECHAT.equalsIgnoreCase (mQueryPayResultResp.channel)) {
            mPayTypeTV.setText (getString (R.string.ewallet_pay_type_wechat));
        } else if (StatusTable.PayType.ALIPAY.equalsIgnoreCase (mQueryPayResultResp.channel)) {
            mPayTypeTV.setText (getString (R.string.ewallet_pay_type_ali));
        }else if (StatusTable.PayType.UNIONQUICKPAY.equalsIgnoreCase (mQueryPayResultResp.channel)){
            mPayTypeTV.setText (getString (R.string.ewallet_pay_type_quick_card));
        }

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
            if (isSuccess ()) {
                if (PayManager.getInstance ().getOnPayListener () != null) {
                    PayManager.getInstance ().getOnPayListener ().onPayResult (PASCPayResult.PASC_PAY_CODE_SUCCESS, PASCPayResult.PASC_PAY_MSG_SUCCESS);
                }
                LogUtil.loge ("PayResultSuccessActivity - >"+PASCPayResult.PASC_PAY_MSG_SUCCESS);
            } else {
                if (PayManager.getInstance ().getOnPayListener () != null) {
                    PayManager.getInstance ().getOnPayListener ().onPayResult (PASCPayResult.PASC_PAY_CODE_WAITING, PASCPayResult.PASC_PAY_MSG_WAITING);
                }
                LogUtil.loge ("PayResultSuccessActivity - >"+PASCPayResult.PASC_PAY_MSG_WAITING);
            }
        }

    }
}
