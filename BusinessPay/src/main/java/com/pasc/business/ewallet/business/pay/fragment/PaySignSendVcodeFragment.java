package com.pasc.business.ewallet.business.pay.fragment;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletIPresenter;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.keyboard.EwalletKeyboardView;

/**
 * @date 2019/7/26
 * @des
 * @modify
 **/
public class PaySignSendVcodeFragment extends BasePayFragment {
    private LinearLayout ewalletSendVerifyCodeViewContainLl;
    private PascToolbar ewalletSendVerifyCodeToolbar;
    private TextView ewalletSendVerifyCodeRemindTv;
    private RelativeLayout ewalletSendVerifyCodeInputLl;
    private EditText ewalletSendVerifyCodeEt;
    private TextView ewalletSendVerifyCodeCountDown;
    private ImageView ewalletSendVerifyCodeDelIv;
    private ImageView ewalletSignSendVcodeCheckboxAgree;
    private TextView ewalletSignSendVcodeTvAgreePre;
    private TextView ewalletSignSendVcodeTvAgree;
    private EwalletKeyboardView ewalletSendVerifyCodeViewKeyboard;


    @Override
    protected EwalletIPresenter createPresenter() {
        return null;
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_sign_send_verify_code_view;
    }

    @Override
    protected void initView() {
        ewalletSendVerifyCodeViewContainLl = findViewById(R.id.ewallet_send_verify_code_view_contain_ll);
        ewalletSendVerifyCodeToolbar = findViewById(R.id.ewallet_send_verify_code_toolbar);
        ewalletSendVerifyCodeRemindTv = findViewById(R.id.ewallet_send_verify_code_remind_tv);
        ewalletSendVerifyCodeInputLl = findViewById(R.id.ewallet_send_verify_code_input_ll);
        ewalletSendVerifyCodeEt = findViewById(R.id.ewallet_send_verify_code_et);
        ewalletSendVerifyCodeCountDown = findViewById(R.id.ewallet_send_verify_code_count_down);
        ewalletSendVerifyCodeDelIv = findViewById(R.id.ewallet_send_verify_code_del_iv);
        ewalletSignSendVcodeCheckboxAgree = findViewById(R.id.ewallet_sign_send_vcode_checkbox_agree);
        ewalletSignSendVcodeTvAgreePre = findViewById(R.id.ewallet_sign_send_vcode_tv_agree_pre);
        ewalletSignSendVcodeTvAgree = findViewById(R.id.ewallet_sign_send_vcode_tv_agree);
        ewalletSendVerifyCodeViewKeyboard = findViewById(R.id.ewallet_send_verify_code_view_keyboard);
    }


}
