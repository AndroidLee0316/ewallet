package com.pasc.business.ewallet.business.home.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.home.adapter.HomeAdapter;
import com.pasc.business.ewallet.business.home.bean.HomeItemBean;
import com.pasc.business.ewallet.business.home.net.QueryBalanceResp;
import com.pasc.business.ewallet.business.home.presenter.EWalletHomePresenter;
import com.pasc.business.ewallet.business.home.view.HomeView;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.common.utils.StatisticsUtils;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.lib.pay.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class EwalletHomeActivity extends EwalletBaseMvpActivity<EWalletHomePresenter> implements View.OnClickListener, HomeView {
    private TextView balanceTv, frozenTv;
    private QueryBalanceResp balanceResp;
    ListView listView;
    private List<HomeItemBean> homeItemBeans = new ArrayList<> ();
    private HomeAdapter homeAdapter;


    @Override
    protected void statusBarColor() {
        setImmersiveStatusBar (getResources ().getColor (R.color.ewallet_home_bg), false);//蓝色

    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_activity_home_new;
    }

    @Override
    protected void initView() {
        listView = findViewById (R.id.ewallet_item_listView);
        TextView titleTv = findViewById (R.id.ewallet_home_title_txt);
        findViewById (R.id.ewallet_home_title_back).setOnClickListener (this);
        findViewById (R.id.ewallet_home_bill_img).setOnClickListener (this);
        findViewById (R.id.ewallet_home_setting_img).setOnClickListener (this);
        balanceTv = findViewById (R.id.ewallet_balance_money_tv);
        frozenTv = findViewById (R.id.ewallet_balance_frozen_tv);
        balanceTv.setOnClickListener (this);
        titleTv.setText (getString (R.string.ewallet_home_title));
        // 使用自定义字体
        balanceTv.setText (R.string.ewallet_money_num_empty);

        homeItemBeans.clear ();

        homeItemBeans.add (new HomeItemBean ().type (HomeItemBean.RECHARGE).text ("充值")
                .icon (R.drawable.ewallet_home_recharge_icon).bgIcon (R.drawable.ewallet_home_recharge_icon_bg));

        homeItemBeans.add (new HomeItemBean ().type (HomeItemBean.WITHDRAW).text ("提现")
                .icon (R.drawable.ewallet_home_withdraw_icon).bgIcon (R.drawable.ewallet_home_withdraw_icon_bg));

        homeItemBeans.add (new HomeItemBean ().type (HomeItemBean.BANKCARD).text ("银行卡")
                .icon (R.drawable.ewallet_home_bank_icon).bgIcon (R.drawable.ewallet_home_bank_icon_bg).bg (R.drawable.ewallet_home_card_bg));

        homeAdapter = new HomeAdapter (this, homeItemBeans);
        listView.setAdapter (homeAdapter);
        listView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeItemBean homeItemBean = homeItemBeans.get (position);
                LogUtil.loge ("onItemClick: "+homeItemBean.getText ());
                switch (homeItemBean.getType ()) {
                    case HomeItemBean.RECHARGE:
                        RouterManager.AccountRouter.gotoRecharge (getActivity ());
                        StatisticsUtils.qb_elementclick_recharge ();
                        break;
                    case HomeItemBean.WITHDRAW:
                        RouterManager.AccountRouter.gotoWithdraw (getActivity (),balanceResp);
                        StatisticsUtils.qb_elementclick_withdraw ();

                        break;
                    case HomeItemBean.BANKCARD:
                        RouterManager.BankCardRouter.gotoBankCardList (getActivity ());
                        StatisticsUtils.qb_elementclick_withdraw ();

                        break;
                }
            }
        });
        StatisticsUtils.qb_main ();
    }

    @Override
    protected void initData(Bundle bundleData) {
    }


    @Override
    protected EWalletHomePresenter createPresenter() {
        return new EWalletHomePresenter ();
    }

    @Override
    protected void onResume() {
        super.onResume ();
        mPresenter.queryBalance (balanceResp == null, UserManager.getInstance ().getMemberNo ());
    }

    @Override
    public void onClick(View view) {
        if (view.getId () == R.id.ewallet_home_title_back) {
            onBackPressed ();
        } else if (view.getId () == R.id.ewallet_home_bill_img) {
            StatisticsUtils.qb_elementclick_bill ();
            RouterManager.BalanceBillRouter.gotoBillList (this, UserManager.getInstance ().getMemberNo ());
        } else if (view.getId () == R.id.ewallet_home_setting_img) {
            //跳转到支付密码管理界面
            StatisticsUtils.qb_elementclick_payManager ();
            RouterManager.PassWordRouter.gotoPayManager (this);
        }
    }


    @Override
    public void queryBalanceSuccess(QueryBalanceResp balanceResp) {
        this.balanceResp = balanceResp;
        balanceTv.setText ("¥ " + Util.doublePoint (balanceResp.balance, 2));

    }

    @Override
    public void queryBalanceError(String code, String msg) {
        ToastUtils.toastMsg (R.string.ewallet_toast_get_balance_failed);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        StatisticsUtils.qb_elementclick_back ();

    }

    @Override
    public void showLoading(String msg) {
        super.showLoading (msg,true);
    }
}
