package com.pasc.business.ewallet.business.pay.fragment;

import android.os.Bundle;

import com.pasc.business.ewallet.base.EwalletBaseMvpFragment;
import com.pasc.business.ewallet.base.EwalletIPresenter;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.pay.view.PayCallbackView;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * @date 2019/7/26
 * @des
 * @modify
 **/
public abstract class BasePayFragment<P extends EwalletIPresenter> extends EwalletBaseMvpFragment<P> {
    public PayCallbackView getPayCallbackView() {
        return (PayCallbackView) getActivity ();
    }

    // true 为支付 ，false 为充值
    protected boolean isPayMode() {
        return StatusTable.PayMode.payMode.equalsIgnoreCase (payMode);
    }

    protected String tradeType() {
        if (isPayMode ()) {
            return StatusTable.Trade.PAY;
        } else {
            return StatusTable.Trade.RECHARGE;
        }
    }
    protected String merchantNo, memberNo, mchOrderNo;
    protected long money = 0;
    protected String payMode = StatusTable.PayMode.payMode;

    protected boolean orderIsValid() {
        if (Util.isEmpty (mchOrderNo)) {
            ToastUtils.toastMsg ("订单号为空");
            return false;
        }
        return true;
    }

    @Override
    protected void initData(Bundle bundleData) {
        merchantNo = bundleData.getString (BundleKey.Pay.key_merchantNo, UserManager.getInstance ().getMerchantNo ());
        memberNo = bundleData.getString (BundleKey.Pay.key_memberNo, UserManager.getInstance ().getMemberNo ());
        mchOrderNo = bundleData.getString (BundleKey.Pay.key_mchOrderNo);
        payMode = bundleData.getString (BundleKey.Pay.key_pay_mode, StatusTable.PayMode.payMode);
        money = bundleData.getLong (BundleKey.Pay.key_money);
    }

    public void onBackPressed(){

    }

    public void sendMsgCode(){

    }
}
