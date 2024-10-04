package com.pasc.business.ewallet.business.rechargewithdraw.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.rechargewithdraw.presenter.RechargePresenter;
import com.pasc.business.ewallet.business.rechargewithdraw.view.RechargeView;
import com.pasc.business.ewallet.business.util.AccountUtil;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.QuitRechargeEvent;
import com.pasc.business.ewallet.common.filter.MoneyInputFilter;
import com.pasc.business.ewallet.common.utils.KeyBoardUtils;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.common.utils.StatisticsUtils;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * @date 2019/8/15
 * @des
 * @modify
 **/
public class RechargeActivity extends EwalletBaseMvpActivity<RechargePresenter> implements View.OnClickListener, RechargeView {

    private EditText rechargeNumEt;
    private View rechargeCommitBtn;

    MoneyInputFilter inputFilter;

    @Override
    protected RechargePresenter createPresenter() {
        return new RechargePresenter ();
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_recharge_activity;
    }

    @Override
    protected CharSequence toolBarTitle() {
        return "充值";
    }

    @Override
    protected boolean needToolBar() {
        return true;
    }

    @Override
    protected EventBusObserver registerEventBus() {
        return new EventBusObserver () {
            @Override
            public void handleMessage(BaseEventType eventType) {
                if (eventType instanceof QuitRechargeEvent){
                    finish ();
                }
            }
        };
    }

    @Override
    protected void initView() {
        rechargeNumEt = findViewById (R.id.ewallet_account_recharge_num_et);
        rechargeCommitBtn = findViewById (R.id.ewallet_recharge_commit_btn);
        rechargeCommitBtn.setOnClickListener (this);
        inputFilter = new MoneyInputFilter ();
        inputFilter.listenInput (rechargeNumEt, new MoneyInputFilter.MoneyInputListener () {
            @Override
            public void inputEmpty() {
                updateBtnStatus (false);
            }

            @Override
            public void isZero() {
                updateBtnStatus (true);
            }

            @Override
            public void outputMoney(double money) {
                updateBtnStatus (money > 0);

            }
        });
        updateBtnStatus (false);
    }

    @Override
    protected void initData(Bundle bundleData) {

    }

    @Override
    public void onClick(View v) {
        if (v == rechargeCommitBtn) {
            if (TextUtils.isEmpty (rechargeNumEt.getText ().toString ().trim ())) {
                return;
            }
            double inputMoney = AccountUtil.formatInputMoneyNum (rechargeNumEt.getText ().toString ());
            if (inputMoney == 0) {
                ToastUtils.toastMsg (R.string.ewallet_toast_input_money_zero);
                return;
            }
            KeyBoardUtils.closeKeybord (rechargeNumEt, this);
            LogUtil.loge ("inputMoney: " + inputMoney);
            RouterManager.PayRouter.gotoRechargePay (this, UserManager.getInstance ().getMemberNo (),
                    UserManager.getInstance ().getMerchantNo (), (long) (inputMoney * 100));
            StatisticsUtils.cz_topupintend ();
        }
    }

    void updateBtnStatus(boolean agree) {
        rechargeCommitBtn.setEnabled (agree);

    }

}
