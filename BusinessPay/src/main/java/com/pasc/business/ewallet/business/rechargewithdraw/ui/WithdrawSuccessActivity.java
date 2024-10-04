package com.pasc.business.ewallet.business.rechargewithdraw.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.rechargewithdraw.net.resp.WithdrawResp;

/**
 * @date 2019/8/17
 * @des
 * @modify
 **/
public class WithdrawSuccessActivity extends EwalletBaseActivity {

    private TextView ewalletWithdrawSuccessTv11;
    private TextView ewalletWithdrawSuccessTv12;
    private TextView ewalletWithdrawSuccessTv21;
    private TextView ewalletWithdrawSuccessTv22;
    private TextView ewalletWithdrawMoney;
    private TextView ewalletWithdrawSuccessFee;
    private TextView ewalletWithdrawSuccessBankName;
    private TextView ewalletWithdrawSuccessFinishBtn;
    private TextView ewallet_withdraw_money_real;
    View ewallet_withdraw_fee_ll;
    WithdrawResp resp;

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_withdraw_success_activity;
    }

    @Override
    protected void initView() {
        ewallet_withdraw_money_real=findViewById (R.id.ewallet_withdraw_money_real);
        ewallet_withdraw_fee_ll = findViewById (R.id.ewallet_withdraw_fee_ll);
        ewalletWithdrawSuccessTv11 = findViewById (R.id.ewallet_withdraw_success_tv11);
        ewalletWithdrawSuccessTv12 = findViewById (R.id.ewallet_withdraw_success_tv12);
        ewalletWithdrawSuccessTv21 = findViewById (R.id.ewallet_withdraw_success_tv21);
        ewalletWithdrawSuccessTv22 = findViewById (R.id.ewallet_withdraw_success_tv22);
        ewalletWithdrawMoney = findViewById (R.id.ewallet_withdraw_money);
        ewalletWithdrawSuccessFee = findViewById (R.id.ewallet_withdraw_success_fee);
        ewalletWithdrawSuccessBankName = findViewById (R.id.ewallet_withdraw_success_bankName);
        ewalletWithdrawSuccessFinishBtn = findViewById (R.id.ewallet_withdraw_success_finish_btn);
        ewalletWithdrawSuccessFinishBtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });

        toolbar.setTitle ("提现");
    }

    @Override
    protected void initData(Bundle bundleData) {
        resp = (WithdrawResp) bundleData.getSerializable (BundleKey.Pay.key_withdraw_info);
        if (resp == null) {
            finish ();
            return;
        }

        ewalletWithdrawSuccessTv12.setText (resp.getApplyDate ());
        ewalletWithdrawSuccessTv22.setText (resp.getAccountingDate ());
        ewalletWithdrawMoney.setText ("¥" + resp.getAmount ());
        ewalletWithdrawSuccessBankName.setText (resp.getBankName ());
        ewalletWithdrawSuccessFee.setText ("¥" + resp.getFee ());
        ewallet_withdraw_money_real.setText ("¥" + resp.getRealAmount ());

//        ewallet_withdraw_fee_ll.setVisibility (Util.isEmpty (resp.getFee ()) ? View.GONE : View.VISIBLE);
    }
}
