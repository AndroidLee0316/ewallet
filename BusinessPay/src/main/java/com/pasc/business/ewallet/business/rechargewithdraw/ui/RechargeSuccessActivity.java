package com.pasc.business.ewallet.business.rechargewithdraw.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.common.utils.Util;

/**
 * @date 2019/8/19
 * @des
 * @modify
 **/
public class RechargeSuccessActivity extends EwalletBaseActivity {
    private ImageView mIconIV;
    private TextView mResultStatusTV;
    private TextView mResultNumTV;
    private TextView mPayTypeTV;
    private TextView mPayToTV;
    private QueryOrderResp mQueryPayResultResp;
    boolean isSuccess() {
        return StatusTable.PayStatus.SUCCESS.equalsIgnoreCase (mQueryPayResultResp.status);
    }
    @Override
    protected int layoutResId() {
        return R.layout.ewallet_recharge_success_activity;
    }

    @Override
    protected void initView() {
        mIconIV = findViewById (R.id.ewallet_activity_pay_result_success_icon);
        mResultStatusTV = findViewById (R.id.ewallet_activity_pay_result_success_title);
        mResultNumTV = findViewById (R.id.ewallet_activity_pay_result_success_num);
        TextView finishTV = findViewById (R.id.ewallet_activity_pay_result_success_commit_btn);
        mPayTypeTV = findViewById (R.id.ewallet_activity_pay_result_success_type_value_tv);
        mPayToTV = findViewById (R.id.ewallet_activity_pay_result_success_num_value_tv);

        finishTV.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                goHome ();
            }
        });
    }

    @Override
    protected void initData(Bundle bundleData) {
        mQueryPayResultResp = (QueryOrderResp) bundleData.getSerializable (BundleKey.Pay.key_query_order_resp);
        if (mQueryPayResultResp == null) {
            goHome ();
            return;
        }
        if (isSuccess ()) {
            mIconIV.setImageResource (R.drawable.ewallet_ic_pay_result_success);
            mResultStatusTV.setText (R.string.ewallet_pay_success);
        } else {
            mIconIV.setImageResource (R.drawable.ewallet_wait_bill_status);
            mResultStatusTV.setText (R.string.ewallet_pay_result_waitting);
        }
        mPayToTV.setText (mQueryPayResultResp.merchantName);
        mResultNumTV.setText ("¥" + Util.doublePoint (mQueryPayResultResp.amount, 2));
        if (!Util.isEmpty (mQueryPayResultResp.channelDesc)){
            mPayTypeTV.setText (mQueryPayResultResp.channelDesc);
        } else if (StatusTable.PayType.WECHAT.equalsIgnoreCase (mQueryPayResultResp.channel)) {
            mPayTypeTV.setText (getString (R.string.ewallet_pay_type_wechat));
        } else if (StatusTable.PayType.ALIPAY.equalsIgnoreCase (mQueryPayResultResp.channel)) {
            mPayTypeTV.setText (getString (R.string.ewallet_pay_type_ali));
        }
    }

    /**
     * 该页面屏蔽返回按钮
     */
    @Override
    public void onBackPressed() {
        return;
    }

    /**
     * 支付结果回调通知 + 关闭本页面
     */
    private void goHome() {
        finish ();
    }
}
