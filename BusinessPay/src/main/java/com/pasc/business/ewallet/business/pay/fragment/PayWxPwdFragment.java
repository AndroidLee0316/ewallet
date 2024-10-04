package com.pasc.business.ewallet.business.pay.fragment;

import com.pasc.business.ewallet.config.Constants;
import com.pasc.lib.keyboard.EwalletPwdKeyboardView;
import com.pasc.lib.sm.SM2Utils;

/**
 * 无锡市民卡支付
 *
 * @date 2019/7/26
 * @des
 * @modify
 **/
public class PayWxPwdFragment extends PayPwdFragment {
    @Override
    protected void initView() {
        super.initView ();
        payView.setPwdMode (EwalletPwdKeyboardView.simpleMode);
        payView.showForget (false);
    }

    @Override
    protected void pay(String password) {
        String payType = getPayCallbackView ().getPayType ();
        try {
            password= SM2Utils.encrypt (password, Constants.wxCardPuk);
        }catch (Throwable throwable){
            throwable.printStackTrace ();
        }
        mPresenter.pay (mchOrderNo, memberNo, payType, password);
    }
}
