package com.pasc.business.ewallet.business.rechargewithdraw.presenter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.widget.DensityUtils;
import com.pasc.lib.keyboard.EwalletPayView;

/**
 * @date 2019/8/17
 * @des
 * @modify
 **/
@NotProguard
public class WithDrawPwdDialog extends Dialog {
    private EwalletPayView payView;
    private EwalletPayView.PasswordInputListener passwordInputListener;

    public void setPasswordInputListener(EwalletPayView.PasswordInputListener passwordInputListener) {
        this.passwordInputListener = passwordInputListener;
    }

    public WithDrawPwdDialog(@NonNull Context context) {
        super (context, R.style.EwalletBottomDialogStyle);
        View view = LayoutInflater.from (context).inflate (R.layout.ewallet_pay_password, null);
        setContentView (view);
        payView = view.findViewById (R.id.payView);
        payView.reSetBackIcon (R.drawable.ewallet_close_icon);
        payView.setForgetPasswordListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                RouterManager.PassWordRouter.gotoForgetPwd (context);
            }
        });
        payView.setInputPasswordListener (new EwalletPayView.PasswordInputListener () {
            @Override
            public void onPasswordInputFinish(String password) {
                if (passwordInputListener != null) {
                    passwordInputListener.onPasswordInputFinish (password);
                }
            }

            @Override
            public void onSimplePasswordInput() {
                if (passwordInputListener != null) {
                    passwordInputListener.onSimplePasswordInput ();
                }
            }
        });

        payView.setCloseListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                dismiss ();
            }
        });

        setOnDismissListener (new OnDismissListener () {
            @Override
            public void onDismiss(DialogInterface dialog) {
                payView.cleanPassword ();
            }
        });

        payView.setHolderVisible (false);

        ViewGroup.LayoutParams layoutParams=  payView.getLayoutParams ();
        layoutParams.height= DensityUtils.dip2px (context,501);
        payView.setLayoutParams (layoutParams);

    }

    public WithDrawPwdDialog setMoneyText(String text) {
        payView.setMoneyText (text);
        return this;
    }

    public WithDrawPwdDialog setPayTypeText(String text) {
        payView.setPayTypeText (text);
        return this;
    }

    public WithDrawPwdDialog setPayFeeText(String text) {
        payView.setPayFeeText (text);
        return this;
    }


    @Override
    public void show() {
        super.show ();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow ().getAttributes ();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow ().getDecorView ().setPadding (0, 0, 0, 0);

        getWindow ().setAttributes (layoutParams);
    }
}
