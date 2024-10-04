package com.pasc.business.ewallet.business.pay.fragment;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletIPresenter;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;

/**
 * @date 2019/7/26
 * @des
 * @modify
 **/
public class PayingFragment extends BasePayFragment {
    private LinearLayout ewalletSendVerifyCodeViewContainLl;
    private PascToolbar ewalletPayingViewToolbar;
    private ImageView ewalletPayingViewLoadingIv;
    private TextView ewalletPayingViewLoadingTv;

    @Override
    protected EwalletIPresenter createPresenter() {
        return null;
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_paying_view;
    }

    @Override
    protected void initView() {
        ewalletSendVerifyCodeViewContainLl = findViewById (R.id.ewallet_send_verify_code_view_contain_ll);
        ewalletPayingViewToolbar = findViewById (R.id.ewallet_paying_view_toolbar);
        ewalletPayingViewLoadingIv = findViewById (R.id.ewallet_paying_view_loading_iv);
        ewalletPayingViewLoadingTv = findViewById (R.id.ewallet_paying_view_loading_tv);
    }

}
