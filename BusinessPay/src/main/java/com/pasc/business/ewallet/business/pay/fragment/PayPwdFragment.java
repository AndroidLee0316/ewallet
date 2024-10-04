package com.pasc.business.ewallet.business.pay.fragment;

import android.view.View;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickPaySendMsgResp;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.business.pay.presenter.PayPresenter;
import com.pasc.business.ewallet.business.pay.view.PayView;
import com.pasc.business.ewallet.business.util.PwdDialogUtil;
import com.pasc.business.ewallet.common.utils.StatisticsUtils;
import com.pasc.lib.keyboard.EwalletPayView;
import com.pasc.lib.netpay.ErrorCode;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * 密码支付 / 银联支付密码校验
 * @date 2019/7/26
 * @des
 * @modify
 **/
public class PayPwdFragment extends BasePayFragment<PayPresenter> implements PayView {
    protected EwalletPayView payView;

    @Override
    protected PayPresenter createPresenter() {
        return new PayPresenter ();
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_pay_password;
    }

    @Override
    protected void initView() {
        payView = findViewById (R.id.payView);
        payView.setForgetPasswordListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                RouterManager.PassWordRouter.gotoForgetPwd (getActivity ());
            }
        });

        payView.setInputPasswordListener (new EwalletPayView.PasswordInputListener () {
            @Override
            public void onPasswordInputFinish(String password) {
                pay (password);
            }

            @Override
            public void onSimplePasswordInput() {

            }
        });

        payView.setCloseListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                onBackPressed ();
            }
        });
        payView.setMoneyVisible (false).setPayTypeVisible (false).setPayFeeVisible (false).setHolderVisible (true);
    }

    @Override
    public void onBackPressed() {
        getPayCallbackView ().switchToPage (FragmentFactory.MainPosition);

    }

    protected void pay(String password) {
        String payType=getPayCallbackView ().getPayType ();
        if (isPayMode ()) {
            if (StatusTable.PayType.UNIONQUICKPAY.equalsIgnoreCase (payType)){
                mPresenter.validPwdAndSendMsgCode (password,getPayCallbackView ().getCardKey (),mchOrderNo,
                        StatusTable.Trade.PAY,0);
            }else {
                // 余额支付
                mPresenter.pay (mchOrderNo, memberNo, payType, password);
            }
        } else {
            //充值是没有余额 充值的
            //银联充值
            if (StatusTable.PayType.UNIONQUICKPAY.equalsIgnoreCase (payType)) {
                mPresenter.validPwdAndSendMsgCode (password,getPayCallbackView ().getCardKey (),null,
                        StatusTable.Trade.RECHARGE,money);
            }
        }

    }


    @Override
    public void onDestroyView() {
        payView.cleanPassword ();
        super.onDestroyView ();
    }

    @Override
    public void paySuccess(String payType, PayResp tradePayResp) {
//        ToastUtils.toastMsg (R.drawable.ewallet_toast_success,"支付成功");
        // 余额支付
        StatisticsUtils.syt_passwordpass ();
        StatisticsUtils.syt_paymentsuccess ("钱包支付");
        QueryOrderResp orderResp = new QueryOrderResp ();
        orderResp.status= StatusTable.PayStatus.SUCCESS;
        orderResp.channel=payType;
        orderResp.needCallback=isPayMode ();
        orderResp.amount = getPayCallbackView ().getOrderAmount ();
        orderResp.merchantName = getPayCallbackView ().getMerchantName ();
        orderResp.channelDesc = getPayCallbackView ().getPayTypeName ();
        RouterManager.PayRouter.gotoPaySuccessResult (getActivity (), orderResp);
        //        getPayCallbackView ().closePayActivity (true);
        getPayCallbackView ().closePayActivityDelay ();

    }

    @Override
    public void payError(String payType, String code, String msg) {
        if (!PwdDialogUtil.pwdErrorIntercept (getActivity (), code, msg)) {
            if ((ErrorCode.ERROR+"").equalsIgnoreCase (code)){
                ToastUtils.toastMsg (msg);
            }else {
                QueryOrderResp queryOrderResp = new QueryOrderResp ();
                //跳转失败
                queryOrderResp.statusDesc = msg;
                queryOrderResp.needCallback=isPayMode ();
                RouterManager.PayRouter.gotoPayErrorResult (getActivity (), queryOrderResp);
                //                getPayCallbackView ().closePayActivity (true);
                getPayCallbackView ().closePayActivityDelay ();
            }
        }
    }

    @Override
    public void validPwdAndSendMsgCodeSuccess(QuickPaySendMsgResp paySendMsgResp) {
        StatisticsUtils.syt_passwordpass ();
        getPayCallbackView ().setPayDate (paySendMsgResp.paydate);
        getPayCallbackView ().setUnionOrderId (paySendMsgResp.unionOrderId);
        getPayCallbackView ().switchToPage (FragmentFactory.VerifyCodePosition);
        BasePayFragment payFragment = FragmentFactory.instance ().getFragment (FragmentFactory.VerifyCodePosition);
        if (payFragment != null) {
            payFragment.sendMsgCode ();
        }
    }

    @Override
    public void validPwdAndSendMsgCodeError(String code,String msg) {
        if (!PwdDialogUtil.pwdErrorIntercept (getActivity (), code, msg)) {
            ToastUtils.toastMsg (msg);
        }
    }

}
