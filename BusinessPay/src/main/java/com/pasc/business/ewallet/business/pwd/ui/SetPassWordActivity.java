package com.pasc.business.ewallet.business.pwd.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.business.pwd.presenter.SetPassWordPresenter;
import com.pasc.business.ewallet.business.pwd.view.SetPassWordView;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.QuitAccountCreateEventType;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.common.utils.StatisticsUtils;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.inner.PayManager;
import com.pasc.business.ewallet.widget.dialog.OnCloseListener;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;
import com.pasc.business.ewallet.widget.dialog.common.ConfirmDialogFragment;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.keyboard.EwalletPassWordView;
import com.pasc.lib.keyboard.EwalletPwdKeyBoardListener;
import com.pasc.lib.keyboard.EwalletPwdKeyboardView;
import com.pasc.lib.pay.common.util.ToastUtils;


/**
 * 设置支付密码页面
 */
public class SetPassWordActivity extends EwalletBaseMvpActivity<SetPassWordPresenter>
        implements SetPassWordView {
    private TextView ewallet_add_pay_password_title, ewallet_add_pay_password_tip;
    private EwalletPassWordView pwd;
    private EwalletPwdKeyboardView keyboardView;
    private boolean isFirstSet = false;
    private String FirstPassWord, SecondPassWord;
    private String validateCode;
    private String setPwdTag = StatusTable.PassWordTag.fromDefaultPwdTag;
    private PascToolbar toolbar;

    @Override
    protected SetPassWordPresenter createPresenter() {
        return new SetPassWordPresenter ();
    }

    @Override
    protected EventBusObserver registerEventBus() {
        return null;
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_activity_account_add_pay_password;
    }

    @Override
    protected void initView() {
        toolbar = findViewById (R.id.ewallet_activity_toolbar);
        ewallet_add_pay_password_title = findViewById (R.id.ewallet_add_pay_password_title);
        ewallet_add_pay_password_tip = findViewById (R.id.ewallet_add_pay_password_tip);
        pwd = findViewById (R.id.ewallet_add_pay_password_pwd);
        keyboardView = findViewById (R.id.ewallet_add_pay_password_keyboardView);
        keyboardView.setFinishDelayTime (300);
        toolbar.setTitle ("");
        toolbar.enableUnderDivider (true);
        pwd.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                keyboardView.show ();
            }
        });
        keyboardView.setPwdBoardListener (new EwalletPwdKeyBoardListener () {
            @Override
            public void onPasswordInputFinish(String password, boolean isInvalidatePwd) {
                if (isInvalidatePwd) {
                    //密码过于简单
                    if (!isFirstSet) {
                        passwordInvalidateTip ();
                    } else {
                        // 提示密码错误
                        passWordNotEqual ();
                    }

                } else {
                    if (!isFirstSet) {
                        isFirstSet = true;
                        //输入初始化密码
                        FirstPassWord = password;
                    } else {
                        //确认密码
                        SecondPassWord = password;
                        comparePassword ();
                    }
                    updatePwdStatusTip ();
                }
            }

            @Override
            public void addPassWord(int currentIndex, int totalLength) {
                pwd.refresh (currentIndex, totalLength);
            }

            @Override
            public void removePassWord(int currentIndex, int totalLength) {
                pwd.refresh (currentIndex, totalLength);

            }

            @Override
            public void clearPassWord(int currentIndex, int totalLength) {
                pwd.refresh (currentIndex, totalLength);

            }
        });
        resetPassword ();
        StatisticsUtils.wjmm_smspass ();
        StatisticsUtils. kh_setpassword ();

    }


    void updatePwdStatusTip() {

        if (isFromForgetPwd ()) {
            if (!isFirstSet) {
                ewallet_add_pay_password_title.setText (getString (R.string.ewallet_set_pay_pwd));
                ewallet_add_pay_password_tip.setText (getString (R.string.ewallet_setting_pay_pwd));
            } else {
                ewallet_add_pay_password_title.setText (getString (R.string.ewallet_set_pay_pwd));
                ewallet_add_pay_password_tip.setText (getString (R.string.ewallet_input_to_ensure));
            }
        } else {
            if (!isFirstSet) {
                ewallet_add_pay_password_title.setText (getString (R.string.ewallet_set_pay_pwd));
                ewallet_add_pay_password_tip.setText (getString (R.string.ewallet_verify_when_pay));
            } else {
                ewallet_add_pay_password_title.setText (getString (R.string.ewallet_set_pay_password_again));
                ewallet_add_pay_password_tip.setText (getString (R.string.ewallet_verify_when_pay));
            }
        }
    }

    boolean isFromForgetPwd() {
        return StatusTable.PassWordTag.fromForgetPwdTag.equals (setPwdTag);
    }

    @Override
    protected void initData(Bundle bundleData) {
        validateCode = bundleData.getString (BundleKey.User.key_validateCode);
        setPwdTag = bundleData.getString (BundleKey.User.key_set_pwd_tag, StatusTable.PassWordTag.fromDefaultPwdTag);
        LogUtil.loge (getSimpleName () + " validateCode: " + validateCode + ", setPwdTag:" + setPwdTag);
        updatePwdStatusTip ();

        if (isFromForgetPwd ()){
            toolbar.addLeftImageButton (R.drawable.ewallet_close_icon).setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    onBackPressed ();
                }
            });
        }
    }

    private void comparePassword() {
        if (Util.isEmpty (FirstPassWord) || Util.isEmpty (SecondPassWord)) {
            resetPassword ();
            return;
        }
        if (FirstPassWord.equals (SecondPassWord)) {
            mPresenter.setPassword (validateCode, SecondPassWord);
        } else {
            passWordNotEqual ();
            resetPassword ();
        }
    }

    private void passwordInvalidateTip() {
        //密码太弱
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment.Builder ()
                .setDesc (getString (R.string.ewallet_password_too_simple))
                .setDescLineSpacingExtra (4)
                .setDescSize (15)
                .setDescColor (getResources ().getColor (R.color.ewallet_color_333333))
                .setConfirmText (getResources ().getString (R.string.ewallet_confirm))
                .setConfirmTextSize (18)
                .setHideCloseButton (true)
                .setConfirmTextColor (getResources ().getColor (R.color.ewallet_primary_btn_enable))
                .setOnConfirmListener (new OnConfirmListener<ConfirmDialogFragment> () {
                    @Override
                    public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
                        confirmDialogFragment.dismiss ();
                    }
                })
                .build ();
        confirmDialogFragment.show (getSupportFragmentManager (), "passwordInvalidateTip");
        resetPassword ();
    }

    @Override
    public void setPassWordSuccess() {

        if (isFromForgetPwd ()) {
            RouterManager.PassWordRouter.gotoChangePwdSuccess (this);
        } else if (StatusTable.PassWordTag.fromNormalCreateAccountTag.equalsIgnoreCase (setPwdTag)) {
            //去首页
            RouterManager.gotoHome (this);
            ToastUtils.toastMsg (R.drawable.ewallet_toast_success, getString (R.string.ewallet_account_create_success));
            StatisticsUtils.kh_accountcreated ();
            StatisticsUtils.kh_walletcreated();
        } else if (StatusTable.PassWordTag.fromPayCreateAccountPwdTag.equalsIgnoreCase (setPwdTag)) {
            //跳转到支付页面
            ToastUtils.toastMsg (R.drawable.ewallet_toast_success, getString (R.string.ewallet_account_create_success));
            StatisticsUtils.kh_accountcreated ();
            StatisticsUtils.kh_walletcreated();
            if (PayManager.getInstance ().getCurrentOrderNo () != null) {
                RouterManager.PayRouter.gotoPay (this, UserManager.getInstance ().getMemberNo (),
                        UserManager.getInstance ().getMerchantNo (),
                        PayManager.getInstance ().getCurrentOrderNo ());
            }
        } else {
            // 默认
        }
        UserManager.getInstance ().setDefaultPwdTag ();
        EventBusManager.getDefault ().post (new QuitAccountCreateEventType ());
        finish ();

        StatisticsUtils. wjmm_pwresetsuccess ();
    }

    @Override
    public void setPassWordError(String code, String msg) {
        if (StatusTable.Account.VALIDATE_CODE_INVALID.equals (code) || StatusTable.Account.VALIDATE_CODE_NOT_MATCH.equals (code)){
            showValidateErrorTip (msg);
            return;
        }
        ToastUtils.toastMsg (msg);
    }

    @Override
    public void onBackPressed() {
        showQuit ();
    }

    private void showQuit() {
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment.Builder ()
                .setDesc (getString (R.string.ewallet_quit_set_password))
                .setDescLineSpacingExtra (4)
                .setDescSize (15)
                .setDescColor (getResources ().getColor (R.color.ewallet_color_333333))
                .setConfirmText (getString (R.string.ewallet_ok))
                .setConfirmTextSize (18)
                .setConfirmTextColor (getResources ().getColor (R.color.ewallet_primary_btn_enable))
                .setCloseText (getString (R.string.ewallet_no))
                .setCloseTextSize (18)
                .setCloseTextColor (getResources ().getColor (R.color.ewallet_color_999999))
                .setOnConfirmListener (new OnConfirmListener<ConfirmDialogFragment> () {
                    @Override
                    public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
                        confirmDialogFragment.dismiss ();
                        EventBusManager.getDefault ().post (new QuitAccountCreateEventType ());
                        SetPassWordActivity.super.onBackPressed ();
                    }
                })
                .setOnCloseListener (new OnCloseListener<ConfirmDialogFragment> () {
                    @Override
                    public void onClose(ConfirmDialogFragment dialogFragment) {
                        dialogFragment.dismiss ();
                    }
                })
                .build ();
        confirmDialogFragment.show (getSupportFragmentManager (), "showQuit");
    }

    @Override
    public void showLoading(String msg) {
        if (isFromForgetPwd ()) {
            super.showLoading ("");
        } else {
            super.showLoading (R.string.ewallet_account_creating);
        }
    }

    @Override
    public void passWordNotEqual() {
        //提示重新设置密码
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment.Builder ()
                .setDesc (getString (R.string.ewallet_set_pay_password_diff))
                .setDescLineSpacingExtra (4)
                .setDescSize (15)
                .setDescColor (getResources ().getColor (R.color.ewallet_color_333333))
                .setConfirmText (getString (R.string.ewallet_iknow))
                .setConfirmTextSize (18)
                .setHideCloseButton (true)
                .setConfirmTextColor (getResources ().getColor (R.color.ewallet_primary_btn_enable))
                .setOnConfirmListener (new OnConfirmListener<ConfirmDialogFragment> () {
                    @Override
                    public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
                        confirmDialogFragment.dismiss ();
                    }
                })
                .build ();
        confirmDialogFragment.show (getSupportFragmentManager (), "resetPasswordTip");
        resetPassword ();
    }

    @Override
    public void resetPassword() {
        isFirstSet = false;
        FirstPassWord = "";
        SecondPassWord = "";
        updatePwdStatusTip ();
    }


    @Override
    protected boolean needSafeCheck() {
        return true;
    }

    public void showValidateErrorTip(String msg) {
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment.Builder ()
                .setDesc (msg)
                .setDescLineSpacingExtra (4)
                .setDescSize (15)
                .setDescColor (getResources ().getColor (R.color.ewallet_color_333333))
                .setConfirmText (getString (R.string.ewallet_iknow))
                .setConfirmTextSize (18)
                .setHideCloseButton (true)
                .setConfirmTextColor (getResources ().getColor (R.color.ewallet_primary_btn_enable))
                .setOnConfirmListener (new OnConfirmListener<ConfirmDialogFragment> () {
                    @Override
                    public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
                        UserManager.getInstance ().setDefaultPwdTag ();
                        //关闭其他界面
                        EventBusManager.getDefault ().post (new QuitAccountCreateEventType (QuitAccountCreateEventType.notQuitCreate));

                        finish ();
                        confirmDialogFragment.dismiss ();
                    }
                })
                .build ();
        confirmDialogFragment.show (getSupportFragmentManager (), "showValidateErrorTipActivity");
    }
}
