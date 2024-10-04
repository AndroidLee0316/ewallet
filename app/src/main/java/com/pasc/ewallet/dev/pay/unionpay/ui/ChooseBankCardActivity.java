package com.pasc.ewallet.dev.pay.unionpay.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.EventBusListener;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.OpenUnifyEvent;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.ewallet.dev.R;
import com.pasc.ewallet.dev.pay.unionpay.adapter.BankCardAdapter;
import com.pasc.ewallet.dev.pay.unionpay.model.BankCard;
import com.pasc.ewallet.dev.pay.unionpay.model.BankCardOpenStatus;
import com.pasc.ewallet.dev.pay.unionpay.presenter.CheckOpenStatusPresenter;
import com.pasc.ewallet.dev.pay.unionpay.view.CheckOpenStatusView;
import com.pasc.ewallet.dev.utils.PayUtil;
import com.pasc.lib.adapter.base.BaseQuickAdapter;
import com.pasc.lib.pay.common.util.ToastUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择银行卡
 */
public class ChooseBankCardActivity extends EwalletBaseMvpActivity<CheckOpenStatusPresenter> implements
    CheckOpenStatusView {
  private static final String TAG = "ChooseBankCardActivity";
  protected PascToolbar toolbar;
  protected RecyclerView rvBankCard;
  protected RelativeLayout rlAddBankCard;
  protected Button btNext;
  private BankCardAdapter mBankCardAdapter;
  private List<BankCard> mBankCardList = new ArrayList<>();
  private String mBankCardNumber;
  private String mMemberNo;
  private boolean mIsAutoQueryOpenStatus = false;
  @Override protected int layoutResId() {
    return R.layout.activity_choose_bank_card;
  }

  @Override protected void initView() {
    toolbar = findViewById (R.id.ewallet_activity_toolbar);
    initToolBar();
    rvBankCard = findViewById(R.id.rv_bank_card);
    rlAddBankCard = findViewById(R.id.rl_add_bank_card);
    btNext = findViewById(R.id.bt_next);
  }

  @Override protected void initData(Bundle bundleData) {
    EventBusManager.getDefault().register(eventBusListener);
    mMemberNo = getIntent().getStringExtra("memberNo");
    if (getIntent().getSerializableExtra("bankCardList") != null) {
      mBankCardList = (List<BankCard>) getIntent().getSerializableExtra("bankCardList");
    }
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    rvBankCard.setLayoutManager(layoutManager);
    mBankCardAdapter = new BankCardAdapter(mBankCardList);
    mBankCardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mBankCardAdapter.setSelection(position);
        mBankCardNumber = mBankCardList.get(position).cardNo;
        btNext.setEnabled(true);
      }
    });
    rvBankCard.setAdapter(mBankCardAdapter);
    rlAddBankCard.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        gotoActivity(AddBankCardActivity.class);
      }
    });
    btNext.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        //下一步
        mIsAutoQueryOpenStatus = false;
        mPresenter.checkOpenStatus(mMemberNo, UserManager.getInstance().getMchOrderNo(),
            PayUtil.PayType.UNION_BANK, mBankCardNumber);
      }
    });
  }

  @Override protected CheckOpenStatusPresenter createPresenter() {
    return new CheckOpenStatusPresenter();
  }

  protected void initToolBar() {
    toolbar.setTitle (getString (R.string.ewallet_choose_bankcard));
    toolbar.enableUnderDivider (true);
    toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
      @Override
      public void onClick(View v) {
        finish ();
      }
    });
  }

  @Override public void queryOpenStatusSuccess(BankCardOpenStatus bankCardOpenStatus) {
    if (bankCardOpenStatus != null) {
      UserManager.getInstance().setCardNum(mBankCardNumber);
      UserManager.getInstance().setUnionOrderId(bankCardOpenStatus.orderId);
      UserManager.getInstance().setUnionOrderTime(bankCardOpenStatus.orderTime);
      int openFlag = bankCardOpenStatus.openFlag;
      if (openFlag == 1) {
        //已开通
        gotoActivity(VerificationCodeActivity.class);
      } else {
        if (mIsAutoQueryOpenStatus) {
          ToastUtils.toastMsg("未开通银联无跳转支付");
        } else {
          //未开通，跳转到银联页面开通无跳转支付
          RouterManager.gotoWeb (this, null, bankCardOpenStatus.openUrl);
        }
      }
    }
  }

  @Override public void queryOpenStatusError(String code, String msg) {
    ToastUtils.toastMsg(msg);
  }

  EventBusListener eventBusListener = new EventBusListener () {
    @Override
    public void handleMessage(BaseEventType type) {
      LogUtil.logd(TAG, "EventBusListener handleMessage");
      if (type instanceof OpenUnifyEvent && !TextUtils.isEmpty(mBankCardNumber)) {
        mIsAutoQueryOpenStatus = true;
        mPresenter.checkOpenStatus(mMemberNo, UserManager.getInstance().getMchOrderNo(),
            PayUtil.PayType.UNION_BANK, mBankCardNumber);
      }
    }
  };

  @Override protected void onDestroy() {
    super.onDestroy();
    LogUtil.logd(TAG, "onDestroy()");
    EventBusManager.getDefault().unregister(eventBusListener);
  }
}