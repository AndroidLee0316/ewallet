package com.pasc.business.ewallet.business.common.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.pwd.presenter.BaseCountDownPresenter;
import com.pasc.business.ewallet.business.pwd.view.BaseCountDownView;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;

/**
 * @date 2019-10-29
 * @des
 * @modify
 **/
public abstract class BaseCountDownSuccessActivity extends EwalletBaseMvpActivity<BaseCountDownPresenter> implements View.OnClickListener, BaseCountDownView {

    @Override
    protected BaseCountDownPresenter createPresenter() {
        return new BaseCountDownPresenter (4);
    }
    protected TextView ewallet_pay_result_next,ewallet_status_tv;
    protected PascToolbar toolbar;
    @Override
    protected int layoutResId() {
        return R.layout.ewallet_activity_pay_success_result;
    }
    protected abstract String successTitle();
    protected abstract String successStatusText();

    @Override
    protected void initView() {
        toolbar = findViewById (R.id.ewallet_activity_toolbar);
        toolbar.setTitle (successTitle ());
        ewallet_status_tv=findViewById (R.id.ewallet_status_tv);
        ewallet_pay_result_next = (Button) findViewById (R.id.ewallet_pay_result_next);
        ewallet_status_tv.setText (successStatusText ());
        toolbar.enableUnderDivider (true);
        ewallet_pay_result_next.setOnClickListener (this);
        startCountDown ();
    }

    protected void startCountDown(){
        mPresenter.countDownStart ();

    }

    @Override
    protected void initData(Bundle bundleData) {

    }

    @Override
    public void onClick(View v) {
        if (v == ewallet_pay_result_next) {
            finishSuccessActivity ();
        }
    }

    @Override
    public void showElapseTime(int count) {
        String s = "完成(" + count + "s)";
        ewallet_pay_result_next.setText (s);
    }

    @Override
    public void showElapseTimeUp() {
        finishSuccessActivity ();
    }

    protected void finishSuccessActivity(){
        finish ();
    }

    @Override
    public void onBackPressed() {
        finishSuccessActivity ();
    }
}
