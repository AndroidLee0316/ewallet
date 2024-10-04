package com.pasc.ewallet.dev.behavior;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.pasc.business.ewallet.base.EwalletBaseActivity;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.config.PayBehaviorHandler;
import com.pasc.ewallet.dev.pay.unionpay.model.BankCard;
import com.pasc.ewallet.dev.pay.unionpay.model.UnionPayModel;
import com.pasc.ewallet.dev.pay.unionpay.ui.AddBankCardActivity;
import com.pasc.ewallet.dev.pay.unionpay.ui.ChooseBankCardActivity;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.pay.common.util.ToastUtils;
import io.reactivex.functions.Consumer;
import java.io.Serializable;
import java.util.List;

/**
 * Created by zhuangjiguang on 2022/4/1.
 * 银联无跳转支付
 */
public class UnionBankPayBehaviorHandler implements PayBehaviorHandler{
  private String memberNo;
  private String mchOrderNo;
  @SuppressLint("CheckResult")
  @Override public void handlerPay(Activity activity, String payType, PayResp payBean) {
    if (payBean != null) {
      memberNo = payBean.returnValue;
      mchOrderNo = payBean.orderno;
    }
    if (activity instanceof EwalletBaseActivity) {
      ((EwalletBaseActivity)activity).showLoading();
    }
    UnionPayModel.queryBankCardList(memberNo)
        .subscribe(new Consumer<List<BankCard>>() {
          @Override
          public void accept(List<BankCard> bankCardList) {
            if (activity instanceof EwalletBaseActivity) {
              ((EwalletBaseActivity)activity).dismissLoading();
            }
            UserManager.getInstance().setMchOrderNo(mchOrderNo);
            if (bankCardList != null && bankCardList.size() > 0) {
              if (activity != null) {
                Intent intent = new Intent(activity, ChooseBankCardActivity.class);
                intent.putExtra("memberNo", memberNo);
                intent.putExtra("bankCardList", (Serializable) bankCardList);
                activity.startActivity(intent);
              }
            } else {
              if (activity != null) {
                activity.startActivity(new Intent(activity, AddBankCardActivity.class));
              }
            }
          }
        }, new BaseRespThrowableObserver() {
          @Override
          public void onV2Error(String code, String msg) {
            if (activity instanceof EwalletBaseActivity) {
              ((EwalletBaseActivity)activity).dismissLoading();
            }
            ToastUtils.toastMsg(msg);
          }
        });
  }

  @Override
  public void onActivityResult(Activity activity, String payType, int requestCode, int resultCode,
      Intent data) {

  }

  @Override public void onNewIntent(Activity activity, String payType, Intent intent) {

  }

  @Override
  public void onRequestPermissionsResult(Activity activity, String payType, int requestCode,
      @NonNull String[] permissions, @NonNull int[] grantResults) {

  }

  @Override public void onDestroy(Activity activity, String payType) {

  }

}
