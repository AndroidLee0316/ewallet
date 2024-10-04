package com.pasc.business.ewallet.business.logout.ui;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.logout.net.resp.MemberValidResp;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.LogoutWithdrawEvent;
import com.pasc.business.ewallet.common.utils.Util;

/**
 * @date 2019-10-29
 * @des 不可以进行账户注销的 （有余额 / 钱包账户有交易、在途、冻结资金）
 * @modify
 **/
public class WalletLogoutDisableActivity extends EwalletBaseActivity implements View.OnClickListener{
    MemberValidResp memberValidResp;
    private View ewalletWithdrawTipLl;
    private TextView ewalletWithdrawTipTv;
    private TextView ewalletWithdrawTipTv2;
    private View ewalletOnTradeTipLl;
    private View ewalletOnTradeTipTv;
    private View ewalletLogoutCloseBtn;

    @Override
    protected EventBusObserver registerEventBus() {
        return new EventBusObserver () {
            @Override
            public void handleMessage(BaseEventType eventType) {
                if (eventType instanceof LogoutWithdrawEvent){
                    finish ();
                }
            }
        };
    }

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
        return R.layout.ewallet_logout_disable_activity;
    }

    @Override
    protected void initView() {
        ewalletWithdrawTipLl =  findViewById(R.id.ewallet_withdraw_tip_ll);
        ewalletWithdrawTipTv =  findViewById(R.id.ewallet_withdraw_tip_tv);
        ewalletWithdrawTipTv2 =  findViewById(R.id.ewallet_withdraw_tip_tv2);
        ewalletOnTradeTipLl =  findViewById(R.id.ewallet_onTrade_tip_ll);
        ewalletOnTradeTipTv =  findViewById(R.id.ewallet_onTrade_tip_tv);
        ewalletLogoutCloseBtn =  findViewById(R.id.ewallet_logout_close_btn);
        ewalletLogoutCloseBtn.setOnClickListener (this);
        ewalletWithdrawTipLl.setOnClickListener (this);
    }

    @Override
    protected void initData(Bundle bundleData) {
        memberValidResp= (MemberValidResp) bundleData.getSerializable (BundleKey.Logout.key_member_valid_info);
        if (memberValidResp==null){
            finish ();
            return;
        }
        showOnTrade (memberValidResp.isTrading);

        //可提现余额为 0 时， 点击提现无效且 颜色置灰
        boolean enable=memberValidResp.hasWithdrawAmt ();
        ewalletWithdrawTipLl.setEnabled (enable);
        String tip="钱包账户有余额 ¥"+ Util.doublePoint (memberValidResp.balance,2) +"、可提现金额 ¥"+Util.doublePoint (memberValidResp.withdrawAmt,2);
        ewalletWithdrawTipTv.setText (tip);
        String inner = "可用于消费或";
        StringBuilder sb = new StringBuilder ();
        String prefix = "提现";
        sb.append (inner);
        sb.append (prefix);
        String content = sb.toString ();
        @ColorRes  int defaultColor=R.color.ewallet_color_999999;
        if (enable){
            defaultColor=R.color.ewallet_theme_color;
            ewalletWithdrawTipTv.setTextColor (getResources ().getColor (R.color.ewallet_color_333333));
        }else {
            defaultColor=R.color.ewallet_color_999999;
            ewalletWithdrawTipTv.setTextColor (getResources ().getColor (R.color.ewallet_color_999999));
        }
        SpannableString spannableString = new SpannableString (content);
        spannableString.setSpan (new ForegroundColorSpan (getResources ().getColor (defaultColor)),
                inner.length (), content.length (), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ewalletWithdrawTipTv2.setText (spannableString);

    }

    //显示基金在途
    void showOnTrade(boolean show){
        show=true;
        ewalletOnTradeTipLl.setVisibility (show?View.VISIBLE:View.GONE);
        ewalletOnTradeTipTv.setVisibility (show?View.VISIBLE:View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v==ewalletWithdrawTipLl){
            RouterManager.AccountRouter.gotoWithdraw (this,null,true);
        }
        else if (v==ewalletLogoutCloseBtn)
        {
            finish ();
        }

    }
}
