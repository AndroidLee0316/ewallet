package com.pasc.business.ewallet.business.logout.ui;

import android.os.Bundle;

import com.pasc.business.ewallet.business.common.ui.BaseCountDownSuccessActivity;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.QuitEventType;

/**
 * @date 2019-10-29
 * @des
 * @modify
 **/
public class WalletLogoutSuccessActivity extends BaseCountDownSuccessActivity {
    @Override
    protected String successTitle() {
        return "注销结果";
    }

    @Override
    protected String successStatusText() {
        return "钱包账户注销成功";
    }

    @Override
    protected void startCountDown() {
    }

    @Override
    protected void initData(Bundle bundleData) {
        super.initData (bundleData);
        String s = "返回首页";
        ewallet_pay_result_next.setText (s);
    }

    @Override
    protected void finishSuccessActivity() {
        super.finishSuccessActivity ();
        EventBusManager.getDefault ().post (new QuitEventType ()); // 关闭所有
    }

//    @Override
//    public void showElapseTime(int count) {
//        String s = "返回首页";
//        ewallet_pay_result_next.setText (s);
//    }
}
