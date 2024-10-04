package com.pasc.lib.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;

/**
 * @author yangzijian
 * @date 2019/2/14
 * @des
 * @modify 支付View
 **/
@NotProguard
public class EwalletPayView extends FrameLayout {

    Context mContext;

    private EwalletPwdKeyboardView keyboardView;
    View.OnClickListener closeListener;
    private TextView tvPayType;
    private TextView tvForgetPwd;
    private TextView tvMoney;
    private View ewalletPlaceHolder;
    private PasswordInputListener inputListener;
    EwalletPassWordView pwd;
    private TextView tvPayFee;
    private PascToolbar toolbar;

    public void reSetBackIcon(int icon){
        toolbar.clearLeftMenu ();
        toolbar.addLeftImageButton (icon).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (closeListener != null) {
                    closeListener.onClick (v);
                }
            }
        });
    }

    public EwalletPayView(Context context) {
        this (context, null);
    }

    public EwalletPayView(Context context, AttributeSet attrs) {
        super (context, attrs);
        this.mContext = context;

        View view = View.inflate (context, R.layout.ewallet_layout_popup_bottom, null);

        toolbar = view.findViewById (R.id.ewallet_activity_toolbar);
        toolbar.setTitle ("请输入支付密码");
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (closeListener != null) {
                    closeListener.onClick (v);
                }
            }
        });
        keyboardView = view.findViewById (R.id.keyboardView);
        pwd = view.findViewById (R.id.pwd);

        pwd.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick(View v) {
                keyboardView.show ();
            }
        });

        initView (view);

        setupView ();

        addView (view);
    }

    public void setPwdMode(int mode){
        keyboardView.setMode (mode);
    }
    public void showForget(boolean show){
        tvForgetPwd.setVisibility (show?VISIBLE:GONE);
    }
    private void initView(View view) {
        tvPayFee = view.findViewById (R.id.tvPayFee);
        tvPayType = view.findViewById (R.id.tvPayType);
        tvForgetPwd = view.findViewById (R.id.tvForgetPwd);
        tvMoney = view.findViewById (R.id.tvMoney);
        ewalletPlaceHolder = view.findViewById (R.id.ewallet_place_holder);

    }

    public void setFinishDelayTime(int delayTime) {
        if (keyboardView != null) {
            keyboardView.setFinishDelayTime (delayTime);
        }
    }

    private void setupView() {
        keyboardView.setPwdBoardListener (new EwalletPwdKeyBoardListener () {
            @Override
            public void onPasswordInputFinish(String password, boolean isInvalidatePwd) {
//                if (isInvalidatePwd){
//                    //简单密码
//                    if (inputListener!=null){
//                        inputListener.onSimplePasswordInput ();
//                    }
//                    return;
//                }
                Log.e ("yzj", "onPasswordInputFinish: "+password+" : "+isInvalidatePwd );
                if (inputListener != null) {
                    inputListener.onPasswordInputFinish (password);
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
    }

    public EwalletPwdKeyboardView getKeyboardView() {

        return keyboardView;
    }

    public void setInputPasswordListener(PasswordInputListener inputListener) {
        this.inputListener = inputListener;
    }

    /**
     * 设置点击忘记密码监听
     *
     * @param listener
     */
    public void setForgetPasswordListener(OnClickListener listener) {
        tvForgetPwd.setOnClickListener (listener);
    }

    /**
     * 设置支付金额
     *
     * @param num
     */
    public void setPayNum(double num) {
        tvMoney.setText (String.format (mContext.getResources ().getString (R.string.ewallet_money_num), num));
    }

    public void setCloseListener(View.OnClickListener closeListener) {
        this.closeListener = closeListener;
    }

    /**
     * 清除输入的密码
     */
    public void cleanPassword() {
        if (pwd != null) {
            keyboardView.clearPassWord ();
        }
    }

    public void setTipTypeVisiable(int tipTypeVisiable) {
        tvPayType.setVisibility (tipTypeVisiable);
    }

    public void setPayNumVisiable(int payNumVisiable) {
        tvMoney.setVisibility (payNumVisiable);
    }

    public EwalletPayView setMoneyText(String text) {
        tvMoney.setText (text);
        tvMoney.setVisibility (VISIBLE);
        return this;
    }

    public EwalletPayView setPayTypeText(String text) {
        tvPayType.setText (text);
        tvPayType.setVisibility (VISIBLE);
        return this;
    }

    public EwalletPayView setPayFeeText(String text) {
        tvPayFee.setText (text);
        tvPayFee.setVisibility (VISIBLE);
        return this;
    }

    public EwalletPayView setMoneyVisible(boolean visible) {
        tvMoney.setVisibility (visible ? VISIBLE : GONE);
        return this;
    }

    public EwalletPayView setPayTypeVisible(boolean visible) {
        tvPayType.setVisibility (visible ? VISIBLE : GONE);
        return this;
    }

    public EwalletPayView setPayFeeVisible(boolean visible) {
        tvPayFee.setVisibility (visible ? VISIBLE : GONE);
        return this;
    }

    public EwalletPayView setHolderVisible(boolean visible) {
        ewalletPlaceHolder.setVisibility (visible ? VISIBLE : GONE);
        return this;
    }

    public EwalletPayView setTitle(CharSequence title) {
        toolbar.setTitle (title);
        return this;
    }

    @NotProguard
    public interface PasswordInputListener {
        //密码输入完之后的回调
        void onPasswordInputFinish(final String password);

        //检测到简单密码到回调
        void onSimplePasswordInput();
    }

}
