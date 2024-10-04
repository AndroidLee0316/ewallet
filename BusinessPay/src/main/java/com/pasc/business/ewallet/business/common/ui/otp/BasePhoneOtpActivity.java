package com.pasc.business.ewallet.business.common.ui.otp;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.common.presenter.BasePhoneOtpPresenter;
import com.pasc.business.ewallet.business.bankcard.view.BasePhoneOtpView;
import com.pasc.business.ewallet.common.customview.ClearEditText;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.QuitAccountCreateEventType;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.config.Constants;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.keyboard.EwalletKeyboardExtraView;


/**
 * @date 2019/7/19
 * @des
 * @modify
 **/
public abstract class BasePhoneOtpActivity<P extends BasePhoneOtpPresenter> extends EwalletBaseMvpActivity<P>
        implements android.view.View.OnClickListener, BasePhoneOtpView {
    protected RelativeLayout ewalletPhoneNumVerifyRl;
    protected RelativeLayout ewalletPhoneNumVerifyTipRl;
    protected TextView ewalletPhoneNumVerifyName;
    protected ClearEditText ewalletPhoneNumVerifyCode;
    protected TextView ewalletPhoneNumVerifyCount;
    protected TextView ewalletPhoneNumVerifyHelp;
    protected Button ewalletPhoneNumVerifyNext;
    protected EwalletKeyboardExtraView ewalletPhoneNumVerifyKv;
    protected PascToolbar toolbar;
    protected String msgCode;
    protected String safeMobile = ""; // 脱敏手机号
    //bundle data
    protected String phoneNum, bankCardNum;

    @Override
    protected EventBusObserver registerEventBus() {
        return new EventBusObserver () {
            @Override
            public void handleMessage(BaseEventType type) {
                if (type instanceof QuitAccountCreateEventType) {
                    finish ();
                }
            }
        };
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_activity_account_phone_num_verify;
    }

    @Override
    protected void initView() {
        toolbar = findViewById (R.id.ewallet_activity_toolbar);
        ewalletPhoneNumVerifyRl = findViewById (R.id.ewallet_phone_num_verify_rl);
        ewalletPhoneNumVerifyTipRl = findViewById (R.id.ewallet_phone_num_verify_tip_rl);
        ewalletPhoneNumVerifyName = findViewById (R.id.ewallet_phone_num_verify_name);
        ewalletPhoneNumVerifyCode = findViewById (R.id.ewallet_phone_num_verify_code);
        ewalletPhoneNumVerifyCount = findViewById (R.id.ewallet_phone_num_verify_count);
        ewalletPhoneNumVerifyHelp = findViewById (R.id.ewallet_phone_num_verify_help);
        ewalletPhoneNumVerifyNext = findViewById (R.id.ewallet_phone_num_verify_next);
        ewalletPhoneNumVerifyKv = findViewById (R.id.ewallet_phone_num_verify_kv);
        toolbar.setTitle (getString (R.string.ewallet_phone_num_tip));
        toolbar.enableUnderDivider (false);
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        ewalletPhoneNumVerifyCount.setOnClickListener (this);
        ewalletPhoneNumVerifyHelp.setOnClickListener (this);
        ewalletPhoneNumVerifyNext.setOnClickListener (this);

        ewalletPhoneNumVerifyKv.setEditText (this, ewalletPhoneNumVerifyCode);
        ewalletPhoneNumVerifyCode.setFilters (new InputFilter[]{new InputFilter.LengthFilter (6)});

        ewalletPhoneNumVerifyKv.setFocusChangeListener (new View.OnFocusChangeListener () {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ewalletPhoneNumVerifyCode.onFocusChange (v, hasFocus);
            }
        });
        ewalletPhoneNumVerifyCode.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateCodeChange (s.toString ());

            }
        });
        updateNextStatus ();
    }

    @Override
    protected void initData(Bundle bundleData) {
        phoneNum = bundleData.getString (BundleKey.User.key_phoneNum);
        bankCardNum = bundleData.getString (BundleKey.User.key_bindCardNo);
        StringBuilder sb = new StringBuilder ();
        String prefix = getString (R.string.ewallet_send_verification_code_prefix);
        String suffix = getString (R.string.ewallet_send_verification_suffix);
        sb.append (prefix);
        if (!TextUtils.isEmpty (phoneNum)) {
            //脱敏
            safeMobile = Util.formatPhoneNum (phoneNum);
            sb.append (safeMobile);
        }
        String content = sb.append (suffix).toString ();
        SpannableString spannableString = new SpannableString (content);
        //设置字体前景颜色
        spannableString.setSpan (new ForegroundColorSpan (getResources ().getColor (R.color.ewallet_third_text)),
                prefix.length (), spannableString.length () - suffix.length (), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体大小
        spannableString.setSpan (new AbsoluteSizeSpan (17, true),
                prefix.length (), spannableString.length () - suffix.length (), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ewalletPhoneNumVerifyName.setText (spannableString);

        sendFirstMsgCode ();
    }

    protected void updateCodeChange(String text) {
        msgCode = text.replace (" ", "");
        updateNextStatus ();
    }

    protected void updateNextStatus() {
        if (!Util.isEmpty (msgCode) && msgCode.length () == 6) {
            ewalletPhoneNumVerifyNext.setEnabled (true);
        } else {
            ewalletPhoneNumVerifyNext.setEnabled (false);

        }
    }

    @Override
    public void onClick(View v) {
        if (v == ewalletPhoneNumVerifyCount) {
            sendMsgCode ();
        } else if (v == ewalletPhoneNumVerifyHelp) {
            RouterManager.gotoWeb (this, "", Constants.OPENACCOUNT_UNRECEIVED_CODE + safeMobile);
        } else if (v == ewalletPhoneNumVerifyNext) {
            verifyNext ();
        }
    }

    @Override
    public void showElapseTime(int count) {
        String s = count + getString (R.string.ewallet_second_retry);
        ewalletPhoneNumVerifyCount.setText (s);
        ewalletPhoneNumVerifyCount.setEnabled (false);
    }

    @Override
    public void showElapseTimeUp() {
        String s = getString (R.string.ewallet_fetch_again);
        ewalletPhoneNumVerifyCount.setText (s);
        ewalletPhoneNumVerifyCount.setEnabled (true);
    }

    @Override
    public abstract void gotoSetPassWord(String validateCode);

    protected void sendFirstMsgCode() {
        //首次发验证码
        mPresenter.countDownStart ();
    }

    /***发送验证码**/
    protected void sendMsgCode() {
    }

    /***下一步**/
    protected void verifyNext() {
    }


}
