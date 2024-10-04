package com.pasc.business.ewallet.business.pwd.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.base.MultiPresenter;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.pwd.presenter.SetPassWordPresenter;
import com.pasc.business.ewallet.business.pwd.presenter.VerifyPassWordPresenter;
import com.pasc.business.ewallet.business.pwd.view.SetPassWordView;
import com.pasc.business.ewallet.business.pwd.view.VerifyPayPwdView;
import com.pasc.business.ewallet.business.util.PwdDialogUtil;
import com.pasc.business.ewallet.common.utils.StatisticsUtils;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.widget.dialog.OnCloseListener;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;
import com.pasc.business.ewallet.widget.dialog.common.ConfirmDialogFragment;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.keyboard.EwalletPassWordView;
import com.pasc.lib.keyboard.EwalletPwdKeyBoardListener;
import com.pasc.lib.keyboard.EwalletPwdKeyboardView;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * 修改密码
 *
 * @date 2019/7/24
 * @des
 * @modify
 **/
public class PassWordModifyActivity extends EwalletBaseMvpActivity<MultiPresenter> implements SetPassWordView, VerifyPayPwdView {
    private TextView ewallet_pay_pwd_modify_title;
    private TextView ewallet_pay_pwd_modify_tip;
    private EwalletPassWordView pwd;
    private EwalletPwdKeyboardView keyboardView;
    /***验证密码成功**/
    private boolean verifyPwdSuccess;
    /***是否为第一次设置密码**/
    private boolean isFirstSet = false;
    private String FirstPassWord, SecondPassWord;
    private String validateCode;

    VerifyPassWordPresenter verifyPassword;
    SetPassWordPresenter setPassWordPresenter;

    @Override
    protected MultiPresenter createPresenter() {
        verifyPassword = new VerifyPassWordPresenter ();
        setPassWordPresenter = new SetPassWordPresenter ();
        MultiPresenter multiPresenter = new MultiPresenter ();
        multiPresenter.requestPresenter (verifyPassword, setPassWordPresenter);
        return multiPresenter;
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_activity_pay_pwd_modify;
    }

    @Override
    protected void initView() {
        PascToolbar toolbar = findViewById (R.id.ewallet_activity_toolbar);
        ewallet_pay_pwd_modify_title = findViewById (R.id.ewallet_pay_pwd_modify_title);
        ewallet_pay_pwd_modify_tip = findViewById (R.id.ewallet_pay_pwd_modify_tip);
        pwd = findViewById (R.id.ewallet_pay_pwd_modify_pwd);
        keyboardView = findViewById (R.id.ewallet_pay_pwd_modify_keyboardView);
        keyboardView.setFinishDelayTime (300);

        toolbar.setTitle ("");
        toolbar.enableUnderDivider (true);
        toolbar.addLeftImageButton (R.drawable.ewallet_close_icon).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                showQuit ();
            }
        });

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
                    //简单密码,无效
                    if (!verifyPwdSuccess) {
                        //校验密码 不拦截简单密码
                        verifyPassword.verifyPassword (password,true);
                    } else if (!isFirstSet) {
                        //密码过于简单
                        passwordInvalidateTip ();
                    } else {
                        // 提示密码错误
                        passWordNotEqual ();
                    }
                } else {
                    if (!verifyPwdSuccess) {
                        //输入支付密码
                        verifyPassword.verifyPassword (password,true);
                    } else if (!isFirstSet) {
                        isFirstSet = true;
                        //输入初始化密码
                        FirstPassWord = password;
                        updatePwdStatusTip ();
                    } else {
                        //确认密码
                        SecondPassWord = password;
                        comparePassword ();
                    }
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
        updatePwdStatusTip ();
    }

    @Override
    protected void initData(Bundle bundleData) {

    }

    private void comparePassword() {
        if (Util.isEmpty (FirstPassWord) || Util.isEmpty (SecondPassWord)) {
            resetPassword ();
            return;
        }
        if (FirstPassWord.equals (SecondPassWord)) {
            setPassWordPresenter.setPassword (validateCode, SecondPassWord);
        } else {
            passWordNotEqual ();
            resetPassword ();
        }
    }

    void updatePwdStatusTip() {
        if (!verifyPwdSuccess) {
            ewallet_pay_pwd_modify_title.setText (R.string.ewallet_amend_password);
            ewallet_pay_pwd_modify_tip.setText (R.string.ewallet_input_pwd_to_verify);
        } else if (!isFirstSet) {
            ewallet_pay_pwd_modify_title.setText (R.string.ewallet_setting_new_pwd);
            ewallet_pay_pwd_modify_tip.setText (R.string.ewallet_setting_pay_pwd);
        } else {
            ewallet_pay_pwd_modify_title.setText (R.string.ewallet_setting_new_pwd);
            ewallet_pay_pwd_modify_tip.setText (getString (R.string.ewallet_input_to_ensure));

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
    public void verifyPwdError(String code, String msg) {
        if (!PwdDialogUtil.pwdErrorIntercept (this, code, msg)) {
            ToastUtils.toastMsg (msg);
        }
    }

    @Override
    public void verifyPwdSuccess(String validateCode) {
        this.validateCode = validateCode;
        verifyPwdSuccess = true;
        updatePwdStatusTip ();
    }


    @Override
    public void onBackPressed() {
        showQuit ();
    }

    private void passwordInvalidateTip() {
        //密码太弱太简单
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
    }

    private void showQuit() {
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment.Builder ()
                .setDesc (getString (R.string.ewallet_quit_amend_password))
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
                        PassWordModifyActivity.super.onBackPressed ();
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
    public void setPassWordSuccess() {
        StatisticsUtils.xgmm_pwresetsuccess ();
        RouterManager.PassWordRouter.gotoChangePwdSuccess (this);
        finish ();
    }

    @Override
    public void setPassWordError(String code, String msg) {
        ToastUtils.toastMsg (msg);
    }
}
