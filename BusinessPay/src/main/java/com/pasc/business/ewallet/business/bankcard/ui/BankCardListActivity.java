package com.pasc.business.ewallet.business.bankcard.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.bankcard.adapter.BankListCardAdapter;
import com.pasc.business.ewallet.business.bankcard.adapter.IBankCardItem;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickCardBean;
import com.pasc.business.ewallet.business.bankcard.net.resp.SafeCardBean;
import com.pasc.business.ewallet.business.bankcard.presenter.BankCardListPresenter;
import com.pasc.business.ewallet.business.bankcard.view.BankCardListView;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.RefreshQuickCardEvent;
import com.pasc.business.ewallet.common.event.SafeCardEvent;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.widget.dialognt.CommonDialog;
import com.pasc.lib.pay.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019/8/16
 * @des
 * @modify
 **/
public class BankCardListActivity extends EwalletBaseMvpActivity<BankCardListPresenter> implements BankCardListView, View.OnClickListener {

    private BankListCardAdapter safeCardAdapter, quickCardAdapter;
    private List<SafeCardBean> safeCardBeans = new ArrayList<> ();
    private List<QuickCardBean> quickCardBeans = new ArrayList<> ();
    private ScrollView ewalletSv;
    private TextView ewalletBankcardTitle;
    private TextView ewalletBankcardChangeBind;
    private ListView ewalletSafeCardList;
    private View ewalletDivider;
    private View ewalletSafeEmpty;
    private View ewalletAddSafeLl;
    private ListView ewalletPayCardList;
    private View ewalletPayEmpty;
    private View ewalletAddPayLl;
    private TextView headerTv;

    private View statusErrorView;

    @Override
    protected boolean needToolBar() {
        return true;
    }

    @Override
    protected CharSequence toolBarTitle() {
        return "我的银行卡";
    }

    @Override
    protected BankCardListPresenter createPresenter() {
        return new BankCardListPresenter ();
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_bankcard_list_activity;
    }

    @Override
    protected EventBusObserver registerEventBus() {
        return new EventBusObserver () {
            @Override
            public void handleMessage(BaseEventType eventType) {
                if (eventType instanceof SafeCardEvent) {
                    SafeCardEvent safeCardEvent = (SafeCardEvent) eventType;
                    if (safeCardEvent.list != null && safeCardEvent.list.size () > 0) {
                        safeCardBeans.clear ();
                        for (QuickCardBean quickCardBean : safeCardEvent.list) {
                            safeCardBeans.add (quickCardBean.convert ());
                            //注意，目前只取第一个
                            if (!StatusTable.enableMultiSafeCard){
                                break;
                            }
                        }
                        if (safeCardAdapter!=null){
                            safeCardAdapter.notifyDataSetChanged ();
                        }
                        updateStatusInfo ();
                    }
                }else if (eventType instanceof RefreshQuickCardEvent){
                    mPresenter.getBankCardList (UserManager.getInstance ().getMemberNo ());
                }
            }
        };
    }

    @Override
    protected void initView() {
        ewalletSv = findViewById (R.id.ewallet_sv);
        ewalletBankcardTitle = findViewById (R.id.ewallet_bankcard_title);
        ewalletBankcardChangeBind = findViewById (R.id.ewallet_bankcard_changeBind);
        ewalletSafeCardList = findViewById (R.id.ewallet_safe_card_list);
        ewalletDivider = findViewById (R.id.ewallet_divider);
        ewalletSafeEmpty = findViewById (R.id.ewallet_safe_empty);
        ewalletAddSafeLl = findViewById (R.id.ewallet_add_safe_ll);
        ewalletPayCardList = findViewById (R.id.ewallet_pay_card_list);
        ewalletPayEmpty = findViewById (R.id.ewallet_pay_empty);
        ewalletAddPayLl = findViewById (R.id.ewallet_add_pay_ll);
        headerTv = findViewById (R.id.ewallet_bank_header_title);
        statusErrorView = findViewById (R.id.status_error);
        safeCardAdapter = new BankListCardAdapter (this, safeCardBeans);
        quickCardAdapter = new BankListCardAdapter (this, quickCardBeans);

        ewalletSafeCardList.setAdapter (safeCardAdapter);
        ewalletPayCardList.setAdapter (quickCardAdapter);

        safeCardAdapter.setBankCardListener (new BankListCardAdapter.BankCardListener () {
            @Override
            public void bankClick(IBankCardItem bankCardItem) {
                RouterManager.BankCardRouter.gotoBankCardDetail (getActivity (),bankCardItem);
            }
        });

        quickCardAdapter.setBankCardListener (new BankListCardAdapter.BankCardListener () {
            @Override
            public void bankClick(IBankCardItem bankCardItem) {
                RouterManager.BankCardRouter.gotoBankCardDetail (getActivity (),bankCardItem);
            }
        });

        findViewById (R.id.btn_footer_retry).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                mPresenter.getBankCardList (UserManager.getInstance ().getMemberNo ());
            }
        });
        ewalletBankcardChangeBind.setOnClickListener (this);
        ewalletAddSafeLl.setOnClickListener (this);
        ewalletAddPayLl.setOnClickListener (this);
        ewalletBankcardTitle.setOnClickListener (this);
        showSafeAndPayEmpty ();

    }

    void showError() {
        statusErrorView.setVisibility (View.VISIBLE);
        ewalletSv.setVisibility (View.GONE);
    }

    void showSafeAndPayEmpty() {
        statusErrorView.setVisibility (View.GONE);
        ewalletSv.setVisibility (View.VISIBLE);
        String tip ="";
        if (StatusTable.enableQuickCard){
            tip = "请您添加<font color='#22C8D8'>账户安全卡</font>或<font color='#22C8D8'>支付卡</font>";

        }else {
            tip = "请您添加<font color='#22C8D8'>账户安全卡</font>";
        }
        ewalletBankcardTitle.setText (Html.fromHtml (tip));
        ewalletBankcardChangeBind.setVisibility (View.GONE);
        ewalletSafeCardList.setVisibility (View.GONE);
        ewalletAddSafeLl.setVisibility (View.VISIBLE);
        ewalletSafeEmpty.setVisibility (View.VISIBLE);

        if (StatusTable.enableQuickCard){
            ewalletDivider.setVisibility (View.GONE);
            headerTv.setVisibility (View.GONE);
            ewalletPayCardList.setVisibility (View.GONE);
            ewalletAddPayLl.setVisibility (View.VISIBLE);
            ewalletPayEmpty.setVisibility (View.GONE);
        }



    }

    void showSafeEmptyPay() {
        statusErrorView.setVisibility (View.GONE);
        ewalletSv.setVisibility (View.VISIBLE);

        ewalletBankcardTitle.setText ("账户安全卡");
        ewalletBankcardChangeBind.setVisibility (View.GONE);
        ewalletSafeCardList.setVisibility (View.GONE);
        ewalletSafeEmpty.setVisibility (View.VISIBLE);
        ewalletAddSafeLl.setVisibility (View.VISIBLE);

        if (StatusTable.enableQuickCard) {
            ewalletDivider.setVisibility (View.VISIBLE);
            headerTv.setVisibility (View.VISIBLE);
            ewalletPayCardList.setVisibility (View.VISIBLE);
            ewalletAddPayLl.setVisibility (View.VISIBLE);
            ewalletPayEmpty.setVisibility (View.GONE);
        }
    }

    void showSafePayEmpty() {
        statusErrorView.setVisibility (View.GONE);
        ewalletSv.setVisibility (View.VISIBLE);

        ewalletBankcardTitle.setText ("账户安全卡");
        ewalletBankcardChangeBind.setVisibility (View.VISIBLE);
        ewalletSafeCardList.setVisibility (View.VISIBLE);
        ewalletSafeEmpty.setVisibility (View.GONE);
        ewalletAddSafeLl.setVisibility (View.GONE);

        if (StatusTable.enableQuickCard) {
            ewalletDivider.setVisibility (View.VISIBLE);
            headerTv.setVisibility (View.VISIBLE);
            ewalletPayCardList.setVisibility (View.GONE);
            ewalletAddPayLl.setVisibility (View.VISIBLE);
            ewalletPayEmpty.setVisibility (View.VISIBLE);
        }
    }

    void showAll() {
        statusErrorView.setVisibility (View.GONE);
        ewalletSv.setVisibility (View.VISIBLE);

        ewalletBankcardTitle.setText ("账户安全卡");
        ewalletBankcardChangeBind.setVisibility (View.VISIBLE);
        ewalletSafeCardList.setVisibility (View.VISIBLE);
        ewalletSafeEmpty.setVisibility (View.GONE);
        ewalletAddSafeLl.setVisibility (View.GONE);

        if (StatusTable.enableQuickCard) {
            ewalletDivider.setVisibility (View.VISIBLE);
            headerTv.setVisibility (View.VISIBLE);
            ewalletPayCardList.setVisibility (View.VISIBLE);
            ewalletAddPayLl.setVisibility (View.VISIBLE);
            ewalletPayEmpty.setVisibility (View.GONE);
        }
    }

    @Override
    protected void initData(Bundle bundleData) {
        statusErrorView.setVisibility (View.GONE);
        ewalletSv.setVisibility (View.GONE);
        mPresenter.getBankCardList (UserManager.getInstance ().getMemberNo ());
    }

    @Override
    public void queryCardListSuccess(List<SafeCardBean> safes, List<QuickCardBean> quick) {
        safeCardBeans.clear ();
        safeCardBeans.addAll (safes);
        safeCardAdapter.notifyDataSetChanged ();
        quickCardBeans.clear ();
        quickCardBeans.addAll (quick);
        quickCardAdapter.notifyDataSetChanged ();
        updateStatusInfo ();
    }

    void updateStatusInfo(){
        if (safeCardBeans.size () > 0 && quickCardBeans.size () > 0) {
            showAll ();
        } else if (safeCardBeans.size () > 0) {
            showSafePayEmpty ();
        } else if (quickCardBeans.size () > 0) {
            showSafeEmptyPay ();
        } else {
            showSafeAndPayEmpty ();
        }
    }

    @Override
    public void queryCardListError(String code, String msg) {
        if (safeCardBeans.size () == 0 && quickCardBeans.size () == 0) {
            showError ();
        } else {
            ToastUtils.toastMsg (msg);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        if (v == ewalletBankcardChangeBind) {
            if (1==1) {
                RouterManager.BankCardRouter.gotoBindMainCard (getActivity ());
                return;
            }
            CommonDialog commonDialog = new CommonDialog (getActivity ());
            commonDialog.setTitle (getString (R.string.ewallet_exchange_safe_pay_bankcard_remind))
                    .setContent (getString (R.string.ewallet_exchange_safe_pay_bankcard_remind_content))
                    .setButton1 (getString (R.string.ewallet_give_up))
                    .setButton2 (getString (R.string.ewallet_exchange_safe_pay_bankcard_sure), "#22C8D8")
                    .setOnButtonClickListener (new CommonDialog.OnButtonClickListener () {
                        @Override
                        public void button1Click() {
                        }

                        @Override
                        public void button2Click() {
                            RouterManager.BankCardRouter.gotoBindMainCard (getActivity ());
                        }
                    })
                    .show ();
        } else if (v == ewalletAddSafeLl) {
            RouterManager.BankCardRouter.gotoAddMainCard (getActivity ());
        } else if (v == ewalletAddPayLl) {
            RouterManager.BankCardRouter.gotoAddQuickCard (getActivity ());

        } else if (v == ewalletBankcardTitle) {
//            String tip="安全卡用于钱包账号资金的提现\n安全卡若要用于支付，需在银行卡包中重新添加。";
//            CommonDialog commonDialog = new CommonDialog (getActivity ());
//            commonDialog.setTitle (getString (R.string.ewallet_what_is_safe_pay_bankcard))
//                    .setContent (tip, Gravity.LEFT)
//                    .setButton1 (getString (R.string.ewallet_iknow), "#22C8D8")
//                    .setOnButtonClickListener (new CommonDialog.OnButtonClickListener () {
//                        @Override
//                        public void button1Click() {
//                            super.button1Click ();
//                        }
//                    });
//            commonDialog.show ();
            new SafeCardDialog (getActivity ()).show ();
        }
    }
}
