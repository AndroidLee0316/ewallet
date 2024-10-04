package com.pasc.ewallet.dev.pay.unionpay.ui;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.common.customview.SpaceEditText;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.EventBusListener;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.OpenUnifyEvent;
import com.pasc.business.ewallet.common.filter.BankCardInputFilter;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.ewallet.dev.R;
import com.pasc.ewallet.dev.pay.unionpay.model.BankCardOpenStatus;
import com.pasc.ewallet.dev.pay.unionpay.presenter.CheckOpenStatusPresenter;
import com.pasc.ewallet.dev.pay.unionpay.view.CheckOpenStatusView;
import com.pasc.ewallet.dev.utils.PayUtil;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * 添加银行卡
 */
public class AddBankCardActivity extends EwalletBaseMvpActivity<CheckOpenStatusPresenter> implements
    CheckOpenStatusView {
  private static final String TAG = "AddBankCardActivity";
  protected PascToolbar toolbar;
  protected SpaceEditText etBankCard;
  protected Button btNext;
  protected String bankCardNum = "";
  private boolean mIsAutoQueryOpenStatus = false;
  @Override protected int layoutResId() {
    return R.layout.activity_add_bank_card;
  }

  @Override protected void initView() {
    toolbar = findViewById (R.id.ewallet_activity_toolbar);
    initToolBar();
    etBankCard = findViewById(R.id.ewallet_addcard_et_card);
    btNext = findViewById(R.id.ewallet_add_unionpay_card_next);
  }

  @Override protected void initData(Bundle bundleData) {
    EventBusManager.getDefault().register(eventBusListener);
    etBankCard.setFilters (new InputFilter[]{new BankCardInputFilter()});
    //银行卡不需要格式化
    etBankCard.format (null);
    etBankCard.setTextChangeListener (new SpaceEditText.TextChangeListener () {
      @Override
      public void textChange(String text) {
        updateCardChange (text);
      }
    });
    btNext.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        //下一步
        mPresenter.checkOpenStatus(UserManager.getInstance().getMemberNo(),
            UserManager.getInstance().getMchOrderNo(), PayUtil.PayType.UNION_BANK, bankCardNum);
      }
    });
  }

  void updateCardChange(String s) {
    bankCardNum = s.replace (" ", "");
    etBankCard.setTextColor (getResources ().getColor (R.color.ewallet_color_333333));
    //ewallet_addcard_tip.setTextColor (getResources ().getColor (R.color.ewallet_color_999999));
    //if (s.length () > 0) {
    //  ewallet_addcard_et_del_card.setVisibility (View.VISIBLE);
    //} else {
    //  ewallet_addcard_et_del_card.setVisibility (View.INVISIBLE);
    //}
    checkNextStatus ();
  }

  void checkNextStatus() {
    if ((!Util.isEmpty (bankCardNum) && bankCardNum.length () >= 12)) {
      btNext.setEnabled (true);
    } else {
      btNext.setEnabled (false);
    }
  }

  @Override protected CheckOpenStatusPresenter createPresenter() {
    return new CheckOpenStatusPresenter();
  }

  protected void initToolBar() {
    toolbar.setTitle (getString (R.string.ewallet_add_bankcard));
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
      UserManager.getInstance().setCardNum(bankCardNum);
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
      LogUtil.logd(TAG, "bankCard: " + bankCardNum);
      if (type instanceof OpenUnifyEvent && !TextUtils.isEmpty(bankCardNum)) {
        mIsAutoQueryOpenStatus = true;
        mPresenter.checkOpenStatus(UserManager.getInstance().getMemberNo(), UserManager.getInstance().getMchOrderNo(),
            PayUtil.PayType.UNION_BANK, bankCardNum);
      }
    }
  };

  @Override protected void onDestroy() {
    super.onDestroy();
    LogUtil.logd(TAG, "onDestroy()");
    EventBusManager.getDefault().unregister(eventBusListener);
  }

}