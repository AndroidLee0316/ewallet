package com.pasc.business.ewallet.business.pay.fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.MultiPresenter;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickPaySendMsgResp;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.business.pay.net.resp.UnionPayResponse;
import com.pasc.business.ewallet.business.pay.presenter.PayPresenter;
import com.pasc.business.ewallet.business.pay.presenter.QueryOrderPresenter;
import com.pasc.business.ewallet.business.pay.presenter.SendMsgCodePresenter;
import com.pasc.business.ewallet.business.pay.view.PayView;
import com.pasc.business.ewallet.business.pay.view.QueryOrderView;
import com.pasc.business.ewallet.business.pay.view.SendMsgCodeView;
import com.pasc.business.ewallet.common.customview.ClearEditText;
import com.pasc.business.ewallet.common.utils.StatisticsUtils;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.config.Constants;
import com.pasc.business.ewallet.inner.PayManager;
import com.pasc.business.ewallet.result.PASCPayResult;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.keyboard.EwalletKeyboardExtraView;
import com.pasc.lib.netpay.ErrorCode;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * @date 2019/7/26
 * @des
 * @modify
 **/
public class PaySendVCodeFragment extends BasePayFragment<MultiPresenter> implements
        View.OnClickListener, SendMsgCodeView, PayView, QueryOrderView {
    private PascToolbar toolbar;
    private TextView ewalletSendVerifyCodeRemindTv;
    private ClearEditText ewalletSendVerifyCodeEt;
    private TextView ewalletSendVerifyCodeCountDown;
    private TextView ewalletSendVerifyCodeUnreceiveTv,ewallet_msg_unReceive_tv;
    private EwalletKeyboardExtraView ewalletSendVerifyCodeViewKeyboard;
    SendMsgCodePresenter sendMsgCodePresenter;
    PayPresenter payPresenter;
    private QueryOrderPresenter queryOrderPresenter;
    private long orderAmount; //订单金额，单位：分
//    private View leftNextBtn;
    CheckBox ewallet_verify_check;
    private boolean isPauseInput;
    @Override
    protected MultiPresenter createPresenter() {
        MultiPresenter multiPresenter = new MultiPresenter ();
        sendMsgCodePresenter = new SendMsgCodePresenter ();
        payPresenter = new PayPresenter ();
        queryOrderPresenter=new QueryOrderPresenter ();
        multiPresenter.requestPresenter (sendMsgCodePresenter, payPresenter,queryOrderPresenter);
        return multiPresenter;
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_send_verify_code_view;
    }

    @Override
    protected void initView() {
        toolbar = findViewById (R.id.ewallet_activity_toolbar);
        ewalletSendVerifyCodeRemindTv = findViewById (R.id.ewallet_send_verify_code_remind_tv);
        ewalletSendVerifyCodeEt = findViewById (R.id.ewallet_send_verify_code_et);
        ewalletSendVerifyCodeCountDown = findViewById (R.id.ewallet_send_verify_code_count_down);
        ewalletSendVerifyCodeUnreceiveTv = findViewById (R.id.ewallet_send_verify_code_unreceive_tv);
        ewalletSendVerifyCodeViewKeyboard = findViewById (R.id.ewallet_send_verify_code_view_keyboard);
        ewallet_verify_check=findViewById (R.id.ewallet_verify_check);
        ewallet_msg_unReceive_tv= findViewById (R.id.ewallet_msg_unReceive_tv);
        toolbar.setTitle ("请输入验证码");
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                onBackPressed ();
            }
        });
//        int whiteColor=getResources ().getColor (R.color.ewallet_white);
//        leftNextBtn=toolbar.addRightTextButton ("下一步",whiteColor,R.drawable.ewallet_btn_default_selector);
//        leftNextBtn.setOnClickListener (new View.OnClickListener () {
//            @Override
//            public void onClick(View v) {
//                String msgCode = ewalletSendVerifyCodeEt.getText ().toString ().replace (" ", "");
//                if (!Util.isEmpty (msgCode) && msgCode.length () == 6) {
//                    verifyNext (msgCode);
//                }
//
//            }
//        });
        ewalletSendVerifyCodeCountDown.setOnClickListener (this);
        ewalletSendVerifyCodeUnreceiveTv.setOnClickListener (this);
        ewallet_msg_unReceive_tv.setOnClickListener (this);
        ewalletSendVerifyCodeViewKeyboard.setEditText (getActivity (), ewalletSendVerifyCodeEt);
        ewalletSendVerifyCodeEt.setFilters (new InputFilter[]{new InputFilter.LengthFilter (6)});
        ewalletSendVerifyCodeEt.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateCodeChange ();
            }
        });
        ewalletSendVerifyCodeViewKeyboard.setFocusChangeListener (new View.OnFocusChangeListener () {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ewalletSendVerifyCodeEt.onFocusChange (v, hasFocus);
            }
        });
//        ewallet_verify_check.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener () {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                updateCodeChange ();
//            }
//        });
//        updateCodeChange ();
    }

    protected void updateCodeChange() {
        String msgCode = ewalletSendVerifyCodeEt.getText ().toString ().replace (" ", "");
//        boolean isCheck=ewallet_verify_check.isChecked ();
        if (!Util.isEmpty (msgCode) && msgCode.length () == 6 /***&& isCheck***/) {
//            leftNextBtn.setEnabled (true);
            if (isPauseInput){//暂停调用输入完成回调
                return;
            }
            isPauseInput = true;
            toolbar.postDelayed (runnable,500);
            verifyNext (msgCode);
        }else {
//            leftNextBtn.setEnabled (false);
        }
    }
    Runnable runnable=new Runnable () {
        @Override
        public void run() {
            isPauseInput = false;
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint (isVisibleToUser);
        if (isVisibleToUser && ewalletSendVerifyCodeRemindTv!=null){
            StringBuilder sb = new StringBuilder ();
            String prefix = getString (R.string.ewallet_send_verification_code_prefix);
            String suffix = getString (R.string.ewallet_send_verification_suffix);
            sb.append (prefix);
            String phoneNum = getPayCallbackView ().getQuickCardPhone ();
            if (Util.isEmpty (phoneNum)) {
                phoneNum =   UserManager.getInstance ().getPhoneNum ();
            }
            if (!Util.isEmpty (phoneNum)) {
                //脱敏
                sb.append (Util.formatPhoneNum (phoneNum));
            }
            String content = sb.append (suffix).toString ();
            SpannableString spannableString = new SpannableString (content);
            //设置字体前景颜色
            spannableString.setSpan (new ForegroundColorSpan (getResources ().getColor (R.color.ewallet_third_text)),
                    prefix.length (), spannableString.length () - suffix.length (), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置字体大小
            spannableString.setSpan (new AbsoluteSizeSpan (17, true),
                    prefix.length (), spannableString.length () - suffix.length (), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ewalletSendVerifyCodeRemindTv.setText (spannableString);
        }
    }

    @Override
    public void onBackPressed() {
        getPayCallbackView ().switchToPage (FragmentFactory.MainPosition);
    }

    @Override
    public void onClick(View v) {
        if (v == ewalletSendVerifyCodeUnreceiveTv) {
            RouterManager.gotoWeb (getActivity (), "", Constants.PAY_BANKCARD_SIGN_SERVICE_PROTOCOL);
        }else if(v==ewallet_msg_unReceive_tv){
            String phoneNum = getPayCallbackView ().getQuickCardPhone ();
            if (Util.isEmpty (phoneNum)) {
                phoneNum =   UserManager.getInstance ().getPhoneNum ();
            }
           RouterManager.gotoWeb (getActivity (), "", Constants.OPENACCOUNT_UNRECEIVED_CODE + Util.formatPhoneNum (phoneNum));
        }
      else if (v == ewalletSendVerifyCodeCountDown) {
            sendMsgCode (false);
        }
    }

    @Override
    public void sendMsgCode() {
        sendMsgCode (true);
    }

    /***发送验证码**/
    protected void sendMsgCode(boolean justCountDown) {
        if (justCountDown) {
            sendMsgCodePresenter.countDownStart ();
        } else {
            //发送验证码
            if (isPayMode ()) {
                sendMsgCodePresenter.sendMsgCode (getPayCallbackView ().getCardKey (),
                        mchOrderNo, StatusTable.Trade.PAY, 0
                );
            } else {
                //充值
                sendMsgCodePresenter.sendMsgCode (getPayCallbackView ().getCardKey (),
                        null, StatusTable.Trade.RECHARGE, money
                );
            }


        }
    }

    /***下一步**/
    protected void verifyNext(String unionVerifyCode) {
        String orderType = getPayCallbackView ().getPayType ();
        String unionOrderId = getPayCallbackView ().getUnionOrderId ();
        String paydate = getPayCallbackView ().getPayDate ();
        String cardKey = getPayCallbackView ().getCardKey ();
        if (isPayMode ()) {
            payPresenter.payQuickCard (mchOrderNo, memberNo, orderType, unionOrderId, paydate, cardKey,unionVerifyCode);
        } else {
            payPresenter.rechargeQuickCard ( memberNo, orderType,money, unionOrderId, paydate, cardKey,unionVerifyCode);

        }
//        ewalletSendVerifyCodeEt.setText ("");
    }

    @Override
    public void showElapseTime(int count) {
        String s = count + getString (R.string.ewallet_second_retry);
        ewalletSendVerifyCodeCountDown.setText (s);
        ewalletSendVerifyCodeCountDown.setEnabled (false);

    }

    @Override
    public void showElapseTimeUp() {
        String s = getString (R.string.ewallet_fetch_again);
        ewalletSendVerifyCodeCountDown.setText (s);
        ewalletSendVerifyCodeCountDown.setEnabled (true);

    }

    @Override
    public void gotoSetPassWord(String validateCode) {

    }

    @Override
    public void paySuccess(String payType, PayResp tradePayResp) {
//        StatisticsUtils.syt_paymentsuccess ("银行卡支付");
        //直接查询
        UnionPayResponse unionPayResponse= tradePayResp.unionPayResponse;
        if (unionPayResponse!=null){
            orderAmount=unionPayResponse.getAmount ();
//            QueryOrderResp queryOrderResp=new QueryOrderResp ();
//            queryOrderResp.status=unionPayResponse.getPayStatus ();
//            queryOrderResp.needCallback=isPayMode ();
//            queryOrderResp.amount=unionPayResponse.getAmount ();
//            queryOrderResp.merchantName = getPayCallbackView ().getMerchantName ();
//            queryOrderResp.channel=payType;
//
//            if (StatusTable.PayStatus.SUCCESS.equals (unionPayResponse.getPayStatus ())){
//                RouterManager.PayRouter.gotoPaySuccessResult (getActivity (), queryOrderResp);
//            }else if (StatusTable.PayStatus.PROCESSING.equals (unionPayResponse.getPayStatus ())){
//                RouterManager.PayRouter.gotoPaySuccessResult (getActivity (), queryOrderResp);
//            }else {
//                RouterManager.PayRouter.gotoPayErrorResult (getActivity (), queryOrderResp);
//            }
//            if (!isPayMode ()){
//                EventBusManager.getDefault ().post (new QuitRechargeEvent ());
//            }
//            getPayCallbackView ().closePayActivity (true);
        }
//        else {
//            getPayCallbackView ().closePayActivity (true);
//        }
        if (isPayMode ()){
            //支付
            queryOrderPresenter.queryOrderStatus (mchOrderNo, null, getPayCallbackView ().getPayType (), tradeType (), true, true);
        }else {
            //充值
            queryOrderPresenter.queryOrderStatus (null, tradePayResp.rechargeOrderNo, getPayCallbackView ().getPayType (), tradeType (), true, true);

        }
    }

    @Override
    public void payError(String payType, String code, String msg) {
//        ToastUtils.toastMsg (msg);
        if ((ErrorCode.ERROR+"").equalsIgnoreCase (code)){
            ToastUtils.toastMsg (msg);
        }else {
            QueryOrderResp queryOrderResp = new QueryOrderResp ();
            //跳转失败
            queryOrderResp.statusDesc = msg;
            queryOrderResp.needCallback=isPayMode ();
            RouterManager.PayRouter.gotoPayErrorResult (getActivity (), queryOrderResp);
            getPayCallbackView ().closePayActivityDelay ();

        }
    }

    @Override
    public void validPwdAndSendMsgCodeSuccess(QuickPaySendMsgResp paySendMsgResp) {
        getPayCallbackView ().setUnionOrderId (paySendMsgResp.unionOrderId);
        getPayCallbackView ().setPayDate (paySendMsgResp.paydate);
    }

    @Override
    public void validPwdAndSendMsgCodeError(String code, String msg) {
        ToastUtils.toastMsg (msg);
    }

    @Override
    public void queryOrderStatusSuccess(QueryOrderResp orderResp) {
        String bankCardName=getPayCallbackView ().getBankCardName ();
        if (!Util.isEmpty (bankCardName)){
            orderResp.channelDesc=bankCardName;
        }
        if (StatusTable.PayStatus.SUCCESS.equalsIgnoreCase (orderResp.status)) {
            if (isPayMode ()) {
                //第三方支付成功后，不需要跳成功界面
                if (PayManager.getInstance ().getOnPayListener () != null) {
                    PayManager.getInstance ().getOnPayListener ().onPayResult (PASCPayResult.PASC_PAY_CODE_SUCCESS, PASCPayResult.PASC_PAY_MSG_SUCCESS);
                }
                StatisticsUtils.syt_paymentsuccess ("银行卡支付");
                RouterManager.PayRouter.gotoPaySuccessResult (getActivity (), orderResp);
            } else {
                //第三方，充值成功后需要跳成功界面
                orderResp.needCallback = isPayMode ();
                if (Util.isEmpty (orderResp.merchantName)) {
                    orderResp.merchantName = "我的钱包";
                }
                RouterManager.PayRouter.gotoPaySuccessResult (getActivity (), orderResp);
                //关闭 充值界面
//                EventBusManager.getDefault ().post (new QuitRechargeEvent ());
                StatisticsUtils.cz_topupsuccess ();

            }


        } else {
            //跳转成功
            if (orderResp.amount == 0) {
                orderResp.amount = orderAmount;
            }
            if (!isPayMode ()){
                if (Util.isEmpty (orderResp.merchantName)) {
                    orderResp.merchantName = "我的钱包";
                }
            }else {
                if (Util.isEmpty (orderResp.merchantName)) {
                    orderResp.merchantName = getPayCallbackView ().getMerchantName ();
                }
            }
            orderResp.needCallback = isPayMode ();
            RouterManager.PayRouter.gotoPaySuccessResult (getActivity (), orderResp);
//            if (!isPayMode ()) {
//                //关闭 充值界面
//                EventBusManager.getDefault ().post (new QuitRechargeEvent ());
//            }

        }
//        getPayCallbackView ().closePayActivity (true);
        getPayCallbackView ().closePayActivityDelay ();

    }

    @Override
    public void queryOrderStatusError(String code, String msg) {
        QueryOrderResp queryOrderResp = new QueryOrderResp ();
        //跳转失败
        queryOrderResp.statusDesc = msg;
        queryOrderResp.needCallback = isPayMode ();
//        if (!isPayMode ()) {
//            //关闭 充值界面
//            EventBusManager.getDefault ().post (new QuitRechargeEvent ());
//        }
        RouterManager.PayRouter.gotoPayErrorResult (getActivity (), queryOrderResp);
//        getPayCallbackView ().closePayActivity (true);
        getPayCallbackView ().closePayActivityDelay ();


    }

    @Override
    public void queryOrderStatusTimeOut() {
        ToastUtils.toastMsg (R.string.ewallet_toast_network_error_and_retry);
    }

    @Override
    public void onDestroyView() {
        isPauseInput=false;
        super.onDestroyView ();
    }
}
