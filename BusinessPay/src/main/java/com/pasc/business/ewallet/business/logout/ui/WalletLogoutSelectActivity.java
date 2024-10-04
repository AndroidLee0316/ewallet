package com.pasc.business.ewallet.business.logout.ui;

import android.os.Bundle;
import android.view.View;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseActivity;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.common.UserManager;

/**
 * @date 2019-10-29
 * @des 注销钱包账户 选择
 * @modify
 **/
public class WalletLogoutSelectActivity extends EwalletBaseActivity implements View.OnClickListener {
    @Override
    protected CharSequence toolBarTitle() {
        return "注销钱包账户";
    }

    @Override
    protected boolean needToolBar() {
        return true;
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_logout_select_activity;
    }

    @Override
    protected void initView() {
        findViewById (R.id.ewallet_logout_verify_pwd).setOnClickListener (this);
        findViewById (R.id.ewallet_logout_verify_id).setOnClickListener (this);
    }

    @Override
    protected void initData(Bundle bundleData) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId () == R.id.ewallet_logout_verify_pwd) {
            RouterManager.PassWordRouter.gotoVerifyPassWord (this,"注销账户", StatusTable.VerifyTag.LOGOUT_WALLET);
        } else if (v.getId () == R.id.ewallet_logout_verify_id) {
            RouterManager.PassWordRouter.gotoCertification (this, UserManager.getInstance ().getPhoneNum (),StatusTable.PassWordTag.fromPayLogoutTag);
        }
    }
}
