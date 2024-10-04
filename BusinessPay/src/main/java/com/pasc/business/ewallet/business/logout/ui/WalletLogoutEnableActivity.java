package com.pasc.business.ewallet.business.logout.ui;

import android.os.Bundle;
import android.view.View;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseActivity;
import com.pasc.business.ewallet.business.RouterManager;

/**
 * @date 2019-10-29
 * @des 可以进行账户注销的 （没有余额 / 钱包账户有交易、在途、冻结资金）
 * @modify
 **/
public class WalletLogoutEnableActivity extends EwalletBaseActivity {
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
        return R.layout.ewallet_logout_enable_activity;
    }

    @Override
    protected void initView() {
        findViewById (R.id.ewallet_logout_btn).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                RouterManager.LogoutRouter.gotoLogoutSelect (getActivity ());
            }
        });
    }

    @Override
    protected void initData(Bundle bundleData) {

    }
}
