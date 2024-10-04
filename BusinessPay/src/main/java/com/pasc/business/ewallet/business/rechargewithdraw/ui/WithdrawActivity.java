package com.pasc.business.ewallet.business.rechargewithdraw.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.rechargewithdraw.net.resp.CalcWithdrawFeeResp;
import com.pasc.business.ewallet.business.rechargewithdraw.net.resp.WithdrawResp;
import com.pasc.business.ewallet.business.rechargewithdraw.presenter.WithdrawPresenter;
import com.pasc.business.ewallet.business.rechargewithdraw.view.WithdrawView;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickCardBean;
import com.pasc.business.ewallet.business.bankcard.net.resp.SafeCardBean;
import com.pasc.business.ewallet.business.home.net.QueryBalanceResp;
import com.pasc.business.ewallet.business.rechargewithdraw.presenter.WithDrawPwdDialog;
import com.pasc.business.ewallet.business.util.AccountUtil;
import com.pasc.business.ewallet.business.util.PwdDialogUtil;
import com.pasc.business.ewallet.common.customview.IReTryListener;
import com.pasc.business.ewallet.common.customview.StatusView;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.LogoutWithdrawEvent;
import com.pasc.business.ewallet.common.event.SafeCardEvent;
import com.pasc.business.ewallet.common.filter.MoneyInputFilter;
import com.pasc.business.ewallet.common.utils.KeyBoardUtils;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.lib.glide.GlideUtil;
import com.pasc.lib.keyboard.EwalletPayView;
import com.pasc.lib.pay.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019/8/15
 * @des
 * @modify
 **/
public class WithdrawActivity extends EwalletBaseMvpActivity<WithdrawPresenter> implements WithdrawView, View.OnClickListener {
    private ImageView ewalletWithdrawBankIv;
    private TextView ewalletWithdrawBankTv;
    private EditText ewalletWithdrawNumEt;
    private TextView ewalletWithdrawBalanceRemindTv;
    private TextView ewalletWithdrawBalanceAllTv;
    private View ewalletWithdrawCommitBtn;
    View ewallet_withdraw_balance_ll, ewallet_withdraw_arrow;
    TextView ewallet_withdraw_tip;
    private double money = 0;
    QueryBalanceResp balanceResp;
    MoneyInputFilter moneyInputFilter;
    StatusView statusView;
    List<SafeCardBean> safeCardBeans;
    SafeCardBean currentSafeCardBean;
    private boolean isFromLogout=false;
    @Override
    protected WithdrawPresenter createPresenter() {
        return new WithdrawPresenter ();
    }

    @Override
    protected CharSequence toolBarTitle() {
        return "提现";
    }

    @Override
    protected boolean needToolBar() {
        return true;
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_withdraw_activity;
    }

    @Override
    protected void initView() {
        statusView = findViewById (R.id.status_view);
        ewallet_withdraw_arrow = findViewById (R.id.ewallet_withdraw_arrow);
        findViewById (R.id.ewallet_withdraw_title_root).setOnClickListener (this);
        statusView.setContentView (findViewById (R.id.ewallet_withdraw_root));
        ewallet_withdraw_tip = findViewById (R.id.ewallet_withdraw_tip);
        ewallet_withdraw_balance_ll = findViewById (R.id.ewallet_withdraw_balance_ll);
        ewalletWithdrawBankIv = findViewById (R.id.ewallet_withdraw_bank_iv);
        ewalletWithdrawBankTv = findViewById (R.id.ewallet_withdraw_bank_tv);
        ewalletWithdrawNumEt = findViewById (R.id.ewallet_withdraw_num_et);
        ewalletWithdrawBalanceRemindTv = findViewById (R.id.ewallet_withdraw_balance_remind_tv);
        ewalletWithdrawBalanceAllTv = findViewById (R.id.ewallet_withdraw_balance_all_tv);
        ewalletWithdrawCommitBtn = findViewById (R.id.ewallet_withdraw_commit_btn);
        ewalletWithdrawBalanceAllTv.setVisibility (View.GONE);
        moneyInputFilter = new MoneyInputFilter ();
        moneyInputFilter.listenInput (ewalletWithdrawNumEt, new MoneyInputFilter.MoneyInputListener () {
            @Override
            public void inputEmpty() {
                updateBtnStatus (false);
                updateRemindWhenInputEmpty ();
            }

            @Override
            public void isZero() {
//                ewalletWithdrawBalanceRemindTv.setText (getString (R.string.ewallet_withdraw_no_deduct));
//                ewalletWithdrawBalanceAllTv.setVisibility (View.GONE);
                updateBtnStatus (true);
                updateRemindWhenInputEmpty ();

            }

            @Override
            public void outputMoney(double inputMoney) {

                if (inputMoney > money) {
                    ewalletWithdrawBalanceRemindTv.setText (Html.fromHtml ("<font color='#FF4D4F'>该金额已超过可提现余额</font>"));
                    ewalletWithdrawBalanceAllTv.setVisibility (View.GONE);
                    // 提现金额超过余额时按钮是不可点击状态
                    updateBtnStatus (false);
                } else {
//                    ewalletWithdrawBalanceRemindTv.setText (getString (R.string.ewallet_withdraw_no_deduct));
                    ewalletWithdrawBalanceAllTv.setVisibility (View.GONE);
                    updateBtnStatus (true);
                    updateRemindWhenInputEmpty ();

                }

            }
        });
        ewalletWithdrawBalanceAllTv.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String balanceNum = String.valueOf (money);
                //服务器返回的金额数据为字符串，转化为double类型时如果小数点后面是.00，会自动丢失最后面的一位0，所以需要加0
                if (balanceNum.endsWith (".0")) {
                    balanceNum = balanceNum + "0";
                }
                ewalletWithdrawNumEt.setText (balanceNum);
            }
        });
        ewalletWithdrawCommitBtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (!hasSafeCard ()){
                    ToastUtils.toastMsg ("请绑定提现银行卡");
                    return;
                }
                String text = ewalletWithdrawNumEt.getText ().toString ().trim ();
                if (TextUtils.isEmpty (text)) {
                    return;
                }
                double inputMoney = AccountUtil.formatInputMoneyNum (text);
                if (inputMoney == 0) {
                    ToastUtils.toastMsg (R.string.ewallet_toast_input_money_zero);
                    return;
                }
                KeyBoardUtils.closeKeybord (ewalletWithdrawNumEt, getActivity ());
                mPresenter.queryBalanceFee (UserManager.getInstance ().getMemberNo (), (long) (inputMoney * 100));
            }
        });
        updateBtnStatus (false);
        statusView.setTryListener (new IReTryListener () {
            @Override
            public void tryAgain() {
                getData ();
            }
        });

    }

    @Override
    protected EventBusObserver registerEventBus() {
        return new EventBusObserver () {
            @Override
            public void handleMessage(BaseEventType eventType) {
                if (eventType instanceof SafeCardEvent) {
                    SafeCardEvent safeCardEvent = (SafeCardEvent) eventType;
                    if (safeCardEvent.list != null && safeCardEvent.list.size () > 0) {
                        List<SafeCardBean> cardBeans = new ArrayList<> ();
                        for (QuickCardBean quickCardBean : safeCardEvent.list) {
                            cardBeans.add (quickCardBean.convert ());
                        }
                        safeCardBeans = cardBeans;
                        if (safeCardBeans.size () > 0) {
                            currentSafeCardBean = safeCardBeans.get (0);
                        } else {
                            currentSafeCardBean = null;
                        }
                        updateSafeCardInfo ();
                    }
                }
            }
        };
    }
    void showPwdDialog(long originWithdraw,long withdrawAmt,long fee) {
        String moneyText = "¥"+Util.doublePoint (originWithdraw,2);
        String realMoneyText= "¥"+Util.doublePoint(withdrawAmt,2);
        String feeText= "¥"+Util.doublePoint(fee,2);
        WithDrawPwdDialog withDrawPwdDialog = new WithDrawPwdDialog (this)
                .setMoneyText (moneyText)
                .setPayFeeText ("实际到账："+realMoneyText+" (扣除服务费"+feeText+"）")
                .setPayTypeText ("提现");
        withDrawPwdDialog.setPasswordInputListener (new EwalletPayView.PasswordInputListener () {
            @Override
            public void onPasswordInputFinish(String password) {
                mPresenter.personWithdraw (UserManager.getInstance ().getMemberNo (), withdrawAmt, password,originWithdraw);
            }

            @Override
            public void onSimplePasswordInput() {

            }
        });
        withDrawPwdDialog.show ();
    }

    /**
     * 当金额输入框为空的时候刷新remind控件
     */
    private void updateRemindWhenInputEmpty() {
        ewalletWithdrawBalanceRemindTv.setText (getString (R.string.ewallet_withdraw_money_num_pre) + String.format (getResources ().getString (R.string.ewallet_money_num), money));
        //如果金额为零，需要隐藏
        if (money == 0) {
            ewalletWithdrawBalanceRemindTv.setVisibility (View.VISIBLE);
            ewalletWithdrawBalanceAllTv.setVisibility (View.GONE);
        } else {
            ewalletWithdrawBalanceRemindTv.setVisibility (View.VISIBLE);
            ewalletWithdrawBalanceAllTv.setVisibility (View.VISIBLE);
        }
    }


    @Override
    protected void initData(Bundle bundleData) {
        balanceResp = (QueryBalanceResp) bundleData.getSerializable (BundleKey.Pay.key_balance);
        isFromLogout=bundleData.getBoolean (BundleKey.Common.key_flag_tag,false);
        if (balanceResp != null) {
            money = balanceResp.getAvaBalance ();
            updateRemindWhenInputEmpty ();
        } else {
            ewalletWithdrawBalanceAllTv.setVisibility (View.GONE);
        }
        getData ();

    }

    void getData() {
        statusView.showLoading ();
        mPresenter.listSafeBankAndBalance (UserManager.getInstance ().getMemberNo ());

    }

    void updateBtnStatus(boolean agree) {
        ewalletWithdrawCommitBtn.setEnabled (agree);
    }

    @Override
    public void queryBalanceFeeSuccess(long originWithdraw,CalcWithdrawFeeResp feeResp) {
        showPwdDialog (originWithdraw,feeResp.withdrawAmt,feeResp.fee);
    }

    @Override
    public void queryBalanceFeeFail(String code, String msg) {
        ToastUtils.toastMsg (msg);
    }

    @Override
    public void queryListSafeBankSuccess(List<SafeCardBean> safeCardBeans) {
        statusView.showContent ();
        this.safeCardBeans = safeCardBeans;
        if (safeCardBeans.size () > 0) {
            currentSafeCardBean = safeCardBeans.get (0);
        } else {
            currentSafeCardBean = null;
        }
        updateSafeCardInfo ();
    }

    @Override
    public void queryListSafeBankError(String code, String msg) {
        statusView.showError ();
    }

    @Override
    public void withdrawSuccess(WithdrawResp resp,long originWithdraw) {
        if (isFromLogout){
            //如果来自注销账户
            EventBusManager.getDefault ().post (new LogoutWithdrawEvent ());
        }
        finish ();
        RouterManager.AccountRouter.gotoWithdrawSuccess (this,resp);
    }

    @Override
    public void withdrawError(String code, String msg) {
        if (!PwdDialogUtil.pwdErrorIntercept (this, code, msg)) {
            ToastUtils.toastMsg (msg);
        }
    }

    @Override
    public void queryBalanceSuccess(QueryBalanceResp balanceResp) {
        this.balanceResp = balanceResp;
        money = balanceResp.getAvaBalance ();
        updateRemindWhenInputEmpty ();
        KeyBoardUtils.showInput (this, ewalletWithdrawNumEt);
    }

    void updateSafeCardInfo() {
        if (currentSafeCardBean != null) {
            GlideUtil.loadImage (this, ewalletWithdrawBankIv,
                    currentSafeCardBean.bankLogo,
                    R.drawable.ewallet_ic_no_bank_card, R.drawable.ewallet_ic_no_bank_card);
            ewallet_withdraw_tip.setText ("提现至");
            ewalletWithdrawBankTv.setText (currentSafeCardBean.getBankNameAndCard ());
            ewallet_withdraw_arrow.setVisibility (View.GONE);

        } else {
            ewalletWithdrawBankIv.setImageResource (R.drawable.ewallet_add_icon);
            ewallet_withdraw_tip.setText ("请添加银行卡完成提现");
            ewalletWithdrawBankTv.setText ("添加银行卡");
            ewallet_withdraw_arrow.setVisibility (View.VISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        if (safeCardBeans != null && safeCardBeans.size () > 0) {
            if (!StatusTable.enableMultiSafeCard){
                return;

            }            if (safeCardBeans.size () > 1) {
                WithdrawBankDialog withdrawBankDialog = new WithdrawBankDialog (this, safeCardBeans, "请选择到账银行卡");
                withdrawBankDialog.setItemClickListener (new AdapterView.OnItemClickListener () {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        currentSafeCardBean = safeCardBeans.get (position);
                        updateSafeCardInfo ();
                    }
                });
                withdrawBankDialog.show ();
            }
        } else {
            //添加卡
            RouterManager.BankCardRouter.gotoAddMainCard (this);
        }
    }

    private boolean hasSafeCard(){
        return safeCardBeans != null && safeCardBeans.size () > 0;
    }
}
