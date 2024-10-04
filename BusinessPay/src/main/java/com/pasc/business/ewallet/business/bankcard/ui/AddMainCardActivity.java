package com.pasc.business.ewallet.business.bankcard.ui;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.bankcard.presenter.AddMainCardPresenter;
import com.pasc.business.ewallet.business.bankcard.view.AddMainCardView;
import com.pasc.business.ewallet.common.customview.SpaceEditText;
import com.pasc.business.ewallet.common.event.AccountInvalidCodeEventType;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.QuitAccountCreateEventType;
import com.pasc.business.ewallet.common.filter.BankCardInputFilter;
import com.pasc.business.ewallet.common.filter.PhoneInputFilter;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.config.Constants;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.keyboard.EwalletKeyboardExtraView;
import com.pasc.lib.pay.common.util.ToastUtils;


/**
 * 首次开户添加主卡
 * 或者
 * 开完户后 开通钱包（主卡）
 */
public class AddMainCardActivity extends EwalletBaseMvpActivity<AddMainCardPresenter>
        implements AddMainCardView, View.OnClickListener {
    protected PascToolbar toolbar;
    protected TextView ewallet_addcard_info_name;
    protected SpaceEditText ewallet_addcard_et_card;
    protected Button ewallet_addcard_next;
    protected View ewallet_addcard_check_rl;
    protected RelativeLayout ewallet_addcard_tip_rl;
    protected RelativeLayout ewallet_addcard_input_card_rl;
    protected SpaceEditText ewallet_addcard_et_phone;
    protected RelativeLayout ewallet_addcard_input_phone_rl;
    protected LinearLayout ewallet_addcard_ll;
    protected TextView ewallet_addcard_tip;
    protected ImageView ewallet_addcard_et_del_card;
    protected ImageView ewallet_addcard_et_del_phone;
    protected TextView ewallet_addcard_tv_support_card;
    protected String bankCardNum = "";
    protected String phoneNum = "";

    protected String name = "";
    protected String validateCode = "";
    protected boolean isOpenSecondClassCard;

    @Override
    protected AddMainCardPresenter createPresenter() {
        return new AddMainCardPresenter ();
    }

    @Override
    protected EventBusObserver registerEventBus() {
        return new EventBusObserver () {
            @Override
            public void handleMessage(BaseEventType type) {
                if (type instanceof QuitAccountCreateEventType) {
                    //关闭
                    finish ();
                } else if (type instanceof AccountInvalidCodeEventType) {
                    //关闭
                    finish ();
                }
            }
        };
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_activity_account_add_card;
    }

    @Override
    protected void initView() {
        toolbar = findViewById (R.id.ewallet_activity_toolbar);
        initToolBar ();
        ewallet_addcard_check_rl = findViewById (R.id.ewallet_addcard_check_rl);
        ewallet_addcard_info_name = findViewById (R.id.ewallet_addcard_info_name);
        ewallet_addcard_et_card = findViewById (R.id.ewallet_addcard_et_card);
        ewallet_addcard_et_del_card = findViewById (R.id.ewallet_addcard_et_del_card);
        ewallet_addcard_next = findViewById (R.id.ewallet_addcard_next);
        EwalletKeyboardExtraView ewallet_addcard_kv_card = findViewById (R.id.ewallet_addcard_kv_card);
        EwalletKeyboardExtraView ewallet_addcard_kv_phone = findViewById (R.id.ewallet_addcard_kv_phone);
        ewallet_addcard_tip_rl = findViewById (R.id.ewallet_addcard_tip_rl);
        ewallet_addcard_input_card_rl = findViewById (R.id.ewallet_addcard_input_card_rl);
        ewallet_addcard_et_phone = findViewById (R.id.ewallet_addcard_et_phone);
        ewallet_addcard_et_del_phone = findViewById (R.id.ewallet_addcard_et_del_phone);
        ewallet_addcard_input_phone_rl = findViewById (R.id.ewallet_addcard_input_phone_rl);
        ewallet_addcard_ll = findViewById (R.id.ewallet_addcard_ll);
        ewallet_addcard_tip = findViewById (R.id.ewallet_addcard_tip);
        ewallet_addcard_tv_support_card = findViewById (R.id.ewallet_addcard_tv_support_card);
        ewallet_addcard_next.setOnClickListener (this);
        ewallet_addcard_et_del_card.setOnClickListener (this);
        ewallet_addcard_et_del_phone.setOnClickListener (this);
        ewallet_addcard_tv_support_card.setOnClickListener (this);
        //设置键盘
        ewallet_addcard_kv_phone.setEditText (this, ewallet_addcard_et_phone);
        ewallet_addcard_kv_card.setEditText (this, ewallet_addcard_et_card);

        //限定字数, 19位数字
        ewallet_addcard_kv_card.setFocusChangeListener (new View.OnFocusChangeListener () {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //有焦点，删除按钮显示；失去焦点，删除按钮隐藏
                if (hasFocus) {
                    if (ewallet_addcard_et_card.getText ().length () > 0) {
                        //有数据
                        ewallet_addcard_et_del_card.setVisibility (View.VISIBLE);
                    } else {
                        ewallet_addcard_et_del_card.setVisibility (View.INVISIBLE);
                    }
                } else {
                    ewallet_addcard_et_del_card.setVisibility (View.INVISIBLE);
                }
            }
        });
        ewallet_addcard_et_card.setFilters (new InputFilter[]{new BankCardInputFilter ()});
        //银行卡格式化 4-4-4-4-?
        ewallet_addcard_et_card.format (4, 4, 4, 4, 3);
        ewallet_addcard_et_card.setTextChangeListener (new SpaceEditText.TextChangeListener () {
            @Override
            public void textChange(String text) {
                updateCardChange (text);
            }
        });

        ewallet_addcard_kv_phone.setFocusChangeListener (new View.OnFocusChangeListener () {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //有焦点，删除按钮显示；失去焦点，删除按钮隐藏
                if (hasFocus) {
                    if (ewallet_addcard_et_phone.getText ().length () > 0) {
                        //有数据
                        ewallet_addcard_et_del_phone.setVisibility (View.VISIBLE);
                    } else {
                        ewallet_addcard_et_del_phone.setVisibility (View.INVISIBLE);
                    }
                } else {
                    ewallet_addcard_et_del_phone.setVisibility (View.INVISIBLE);
                }
            }
        });
        int limit = 11;
        ewallet_addcard_et_phone.setFilters (new InputFilter[]{new PhoneInputFilter ()});
        ewallet_addcard_et_phone.delLastSpace (limit + 1);
        ewallet_addcard_et_phone.setTextChangeListener (new SpaceEditText.TextChangeListener () {
            @Override
            public void textChange(String text) {
                updatePhoneChange (text);
            }
        });
    }

    protected void initToolBar() {
        toolbar.setTitle (getString (R.string.ewallet_add_bankcard));
        toolbar.enableUnderDivider (false);
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
    }

    @Override
    protected void initData(Bundle bundleData) {
        validateCode = bundleData.getString (BundleKey.User.key_validateCode);
        name = bundleData.getString (BundleKey.User.key_name, UserManager.getInstance ().getSafeName ());
        isOpenSecondClassCard = bundleData.getBoolean (BundleKey.User.key_open_second_card_flag);
        LogUtil.loge (getSimpleName () + " name: " + name + " , validateCode: " + validateCode + " , isOpenSecondClassCard: " + isOpenSecondClassCard);
        StringBuilder sb = new StringBuilder ();
        String prefix = getString (R.string.ewallet_add_prefix);
        String suffix = getString (R.string.ewallet_bankcard_suffix);
        sb.append (prefix);
        if (!TextUtils.isEmpty (name)) {
            sb.append (name);
        }
        String content = sb.append (suffix).toString ();
        SpannableString spannableString = new SpannableString (content);
        //设置字体前景颜色
        spannableString.setSpan (new ForegroundColorSpan (getResources ().getColor (R.color.ewallet_third_text)),
                prefix.length (), spannableString.length () - suffix.length (), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体大小
        spannableString.setSpan (new AbsoluteSizeSpan (17, true),
                prefix.length (), spannableString.length () - suffix.length (), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ewallet_addcard_info_name.setText (spannableString);
    }

    @Override
    public void onClick(View v) {
        if (v.getId () == R.id.ewallet_addcard_next) {
            boolean matchPhone = Util.isValidPhone (phoneNum);
            boolean matchCard = Util.isValidBankCard (bankCardNum);
            if (!matchCard) {
                //提示银行卡格式不对
                regexCardNumError ();
            }
            if (!matchPhone) {
                //提示手机卡格式不对
                regexPhoneNumError ();
            }
            if (matchCard && matchPhone) {
                mPresenter.addAndBindCard (UserManager.getInstance ().getMemberNo (), bankCardNum, phoneNum);
            }
        } else if (v.getId () == R.id.ewallet_addcard_et_del_card) {
            ewallet_addcard_et_card.setText ("");
            ewallet_addcard_et_del_card.setVisibility (View.INVISIBLE);
        } else if (v.getId () == R.id.ewallet_addcard_et_del_phone) {
            ewallet_addcard_et_phone.setText ("");
            ewallet_addcard_et_del_phone.setVisibility (View.INVISIBLE);
        } else if (v.getId () == R.id.ewallet_addcard_tv_support_card) {
            showSupportCard ();
        }
    }

    void regexCardNumError() {
        ewallet_addcard_et_card.setTextColor (getResources ().getColor (R.color.ewallet_error_highlight_text));
        ewallet_addcard_tip.setTextColor (getResources ().getColor (R.color.ewallet_error_highlight_text));

    }

    void regexPhoneNumError() {
        ewallet_addcard_et_phone.setTextColor (getResources ().getColor (R.color.ewallet_error_highlight_text));
    }

    void showSupportCard() {
        //查看支持的银行卡
        RouterManager.gotoWeb (this, "", Constants.CREATE_ACCOUNT_SUPPORT_BANK_CARD);
    }


    void updateCardChange(String s) {
        bankCardNum = s.replace (" ", "");
        ewallet_addcard_et_card.setTextColor (getResources ().getColor (R.color.ewallet_color_333333));
        ewallet_addcard_tip.setTextColor (getResources ().getColor (R.color.ewallet_color_999999));
        if (s.length () > 0) {
            ewallet_addcard_et_del_card.setVisibility (View.VISIBLE);
        } else {
            ewallet_addcard_et_del_card.setVisibility (View.INVISIBLE);
        }
        checkNextStatus ();
    }

    void updatePhoneChange(String s) {
        //输入框粘贴的规则如下，身份证 手机号 银行卡号 粘贴时如果有中文，
        // 就沾不进去,过长也沾不进去,切到中间粘贴如果沾上之后超过限定位数的话也沾不上去
        phoneNum = s.replace (" ", "");
        ewallet_addcard_et_phone.setTextColor (getResources ().getColor (R.color.ewallet_color_333333));
        if (s.length () > 0) {
            ewallet_addcard_et_del_phone.setVisibility (View.VISIBLE);
        } else {
            ewallet_addcard_et_del_phone.setVisibility (View.INVISIBLE);
        }
        checkNextStatus ();
    }

    void checkNextStatus() {
        if ((!Util.isEmpty (bankCardNum) && bankCardNum.length () >= 16) && (!Util.isEmpty (phoneNum) && phoneNum.length () == 11)) {
            ewallet_addcard_next.setEnabled (true);
        } else {
            ewallet_addcard_next.setEnabled (false);
        }
    }

    @Override
    protected boolean needSafeCheck() {
        return true;
    }

    @Override
    public void bindCardSuccess() {
        RouterManager.BankCardRouter.gotoAddMainCardPhoneOtp (this, bankCardNum, phoneNum, false);
    }

    @Override
    public void bindCardError(String code, String msg) {
        ToastUtils.toastMsg (msg);

    }
}
