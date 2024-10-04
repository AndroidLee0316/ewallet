package com.pasc.business.ewallet.business.traderecord.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.traderecord.net.resp.AvailBalanceBean;
import com.pasc.business.ewallet.business.traderecord.presenter.BalanceDetailPresenter;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;

/**
 * @author yangzijian
 * @date 2019/3/22
 * @des
 * @modify
 **/
public class BalanceDetailActivity extends EwalletBaseMvpActivity<BalanceDetailPresenter> {

    private TextView tradeMoneyTv, tradeTypeTv;
    private TextView tradeTimeTv, tradeOrderNo;
    @Override
    protected void initView() {
        PascToolbar toolbar = findViewById(R.id.ewallet_activity_toolbar);
        toolbar.setTitle ("余额明细");
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        tradeMoneyTv = findViewById(R.id.ewallet_pay_yue_record_detail_money);
        tradeTypeTv = findViewById(R.id.ewallet_pay_yue_record_detail_type);
        tradeTimeTv = findViewById(R.id.ewallet_pay_yue_record_detail_time);
        tradeOrderNo = findViewById(R.id.ewallet_pay_yue_record_detail_transysno);
    }

    @Override
    protected BalanceDetailPresenter createPresenter() {
        return new BalanceDetailPresenter ();
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_pay_yue_record_detail_activity;
    }

    @Override
    protected void initData(Bundle bundleData) {
            AvailBalanceBean balanceBean = (AvailBalanceBean) bundleData.getSerializable(BundleKey.Trade.key_balanceBean);
            showDetail(balanceBean);
    }

    private void showDetail(AvailBalanceBean balanceBean){

        if (balanceBean != null){
            tradeMoneyTv.setText(balanceBean.getTradeAmount());
            tradeTypeTv.setText(balanceBean.remark);
            tradeTimeTv.setText(balanceBean.tranTime);
            tradeOrderNo.setText(balanceBean.tranSysNo);
        }
    }

}
