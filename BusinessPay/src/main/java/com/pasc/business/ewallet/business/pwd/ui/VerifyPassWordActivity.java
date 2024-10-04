package com.pasc.business.ewallet.business.pwd.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.bankcard.model.CardModel;
import com.pasc.business.ewallet.business.logout.model.LogoutModel;
import com.pasc.business.ewallet.business.pwd.presenter.VerifyPassWordPresenter;
import com.pasc.business.ewallet.business.pwd.view.VerifyPayPwdView;
import com.pasc.business.ewallet.business.util.PwdDialogUtil;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;
import com.pasc.business.ewallet.widget.dialog.common.ConfirmDialogFragment;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.keyboard.EwalletPassWordView;
import com.pasc.lib.keyboard.EwalletPwdKeyBoardListener;
import com.pasc.lib.keyboard.EwalletPwdKeyboardView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.netpay.resp.VoidObject;
import com.pasc.lib.pay.common.util.ToastUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * 验证密码，用于敏感操作的验证入口
 * 目前只用于添加卡，验证密码
 */
public class VerifyPassWordActivity extends EwalletBaseMvpActivity<VerifyPassWordPresenter>
        implements VerifyPayPwdView {
    private TextView title;
    private EwalletPassWordView pwd;
    private EwalletPwdKeyboardView keyboardView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable ();
    private String verifyTag;

    @Override
    protected VerifyPassWordPresenter createPresenter() {
        return new VerifyPassWordPresenter ();
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_activity_account_verify_pwd;
    }

    @Override
    protected void initView() {
        PascToolbar toolbar = findViewById (R.id.ewallet_activity_toolbar);
        title = findViewById (R.id.ewallet_verify_password_title);
        pwd = findViewById (R.id.ewallet_verify_password_pwd);
        keyboardView = findViewById (R.id.ewallet_verify_password_keyboardView);
        keyboardView.setFinishDelayTime (300);
        toolbar.setTitle ("");
        toolbar.enableUnderDivider (true);
        toolbar.addLeftImageButton (R.drawable.ewallet_close_icon).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });

        pwd.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                keyboardView.show ();
            }
        });
    }

    @Override
    protected void initData(Bundle bundleData) {
        String titleStr = bundleData.getString (BundleKey.Common.key_title);
        verifyTag = bundleData.getString (BundleKey.Common.key_verify_tag);
        title.setText (titleStr);
        keyboardView.setPwdBoardListener (new EwalletPwdKeyBoardListener () {
            @Override
            public void onPasswordInputFinish(String password, boolean isInvalidatePwd) {
                    boolean disMissDialog =! (StatusTable.VerifyTag.ADD_QUICK_CARD.equals (verifyTag) || StatusTable.VerifyTag.LOGOUT_WALLET.equals (verifyTag));
                    mPresenter.verifyPassword (password, disMissDialog);
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

    @Override
    public void verifyPwdSuccess(String validateCode) {
        if (StatusTable.VerifyTag.ADD_QUICK_CARD.equals (verifyTag)) {
            gotoQuickCardWeb ();
        }else if (StatusTable.VerifyTag.LOGOUT_WALLET.equals (verifyTag)){
            //解绑
            memberReset (validateCode);
        }else {
            finish ();
        }
    }


    @Override
    public void verifyPwdError(String code, String msg) {
        if (!PwdDialogUtil.pwdErrorIntercept (this, code, msg)) {
            ToastUtils.toastMsg (msg);
        }
    }

    @Override
    protected boolean needSafeCheck() {
        return true;
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear ();
        super.onDestroy ();
    }

    void gotoQuickCardWeb() {
        showLoading ();
        Disposable disposable = CardModel.jumpBindCardPage (UserManager.getInstance ().getMemberNo ()).subscribe (new Consumer<String> () {
            @Override
            public void accept(String htmlData) throws Exception {
                dismissLoading ();
                RouterManager.gotoWeb (getActivity (), "银联绑卡", htmlData);
                finish ();
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                dismissLoading ();
                ToastUtils.toastMsg (msg);
            }
        });

        compositeDisposable.add (disposable);
    }

    void memberReset(String validateCode){
        showLoading ();
        compositeDisposable.add(LogoutModel.memberReset (UserManager.getInstance ().getMemberNo (),validateCode)
                .subscribe (new Consumer<VoidObject> () {
                    @Override
                    public void accept(VoidObject voidObject) throws Exception {
                        dismissLoading ();
                        RouterManager.LogoutRouter.gotoLogoutSuccess (getActivity ());
                        finish ();
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        dismissLoading ();
                        ToastUtils.toastMsg (msg);
                        finish ();

                    }
                }));
    }
}
