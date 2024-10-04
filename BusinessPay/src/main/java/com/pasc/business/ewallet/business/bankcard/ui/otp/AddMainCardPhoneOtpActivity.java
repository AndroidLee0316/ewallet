package com.pasc.business.ewallet.business.bankcard.ui.otp;

import android.os.Bundle;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.bankcard.presenter.otp.AddMainCardPhoneOtpPresenter;
import com.pasc.business.ewallet.business.common.ui.otp.BasePhoneOtpActivity;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.QuitAccountCreateEventType;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * @date 2019-08-26
 * @des
 * @modify
 **/
public class AddMainCardPhoneOtpActivity extends BasePhoneOtpActivity<AddMainCardPhoneOtpPresenter> {

    protected boolean isBind = false;

    @Override
    protected void initData(Bundle bundleData) {
        super.initData (bundleData);
        isBind = bundleData.getBoolean (BundleKey.User.key_flag_bind);
    }

    @Override
    public void gotoSetPassWord(String validateCode) {
        ToastUtils.toastMsg (R.drawable.ewallet_toast_success, isBind ? "换绑成功":"添加成功");
        EventBusManager.getDefault ().post (new QuitAccountCreateEventType ());
        finish ();
    }

    @Override
    protected AddMainCardPhoneOtpPresenter createPresenter() {
        return new AddMainCardPhoneOtpPresenter ();
    }

    @Override
    protected void sendMsgCode() {
        mPresenter.addAndBindCard (UserManager.getInstance ().getMemberNo (), bankCardNum, phoneNum);
    }

    @Override
    protected void verifyNext() {
        mPresenter.bindCardValid (UserManager.getInstance ().getMemberNo (), bankCardNum, msgCode);
    }
}
