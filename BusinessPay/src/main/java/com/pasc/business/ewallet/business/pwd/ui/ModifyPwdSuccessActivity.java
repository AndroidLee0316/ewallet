package com.pasc.business.ewallet.business.pwd.ui;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.common.ui.BaseCountDownSuccessActivity;

/**
 * @date 2019/7/25
 * @des
 * @modify
 **/
public class ModifyPwdSuccessActivity extends BaseCountDownSuccessActivity {
    @Override
    protected int layoutResId() {
        return R.layout.ewallet_activity_pay_success_result;
    }

    @Override
    protected String successTitle() {
        return "密码设置结果";
    }

    @Override
    protected String successStatusText() {
        return "支付密码设置成功";
    }

}
