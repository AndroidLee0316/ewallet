package com.pasc.business.ewallet.business.account.ui.otp;

import android.os.Bundle;

import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.account.presenter.otp.CreateAccountPhoneOtpPresenter;
import com.pasc.business.ewallet.business.common.ui.otp.BasePhoneOtpActivity;
import com.pasc.business.ewallet.business.logout.model.LogoutModel;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.common.utils.StatisticsUtils;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.netpay.resp.VoidObject;
import com.pasc.lib.pay.common.util.ToastUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * 开户 发送验证码 和 开通钱包 / 忘记密码 / 实名销户
 *
 * @date 2019/7/22
 * @des
 * @modify
 **/
public class CreateAccountPhoneOtpActivity extends BasePhoneOtpActivity<CreateAccountPhoneOtpPresenter> {
    private String setPwdTag;
    String scene = StatusTable.Scenes.SET_PSW;

    @Override
    protected CreateAccountPhoneOtpPresenter createPresenter() {
        return new CreateAccountPhoneOtpPresenter ();
    }

    @Override
    protected void initData(Bundle bundleData) {
        StatisticsUtils.kh_phonenumber ();
        super.initData (bundleData);
        setPwdTag = bundleData.getString (BundleKey.User.key_set_pwd_tag);
//        scene = StatusTable.PassWordTag.fromForgetPwdTag.equals (setPwdTag)
//                ? StatusTable.Scenes.FORGET_PSW : StatusTable.Scenes.SET_PSW;
        scene =StatusTable.getScenes (setPwdTag);
        LogUtil.loge (getSimpleName () + "setPwdTag: " + setPwdTag +"，scenes: "+scene);
        StatisticsUtils.wjmm_identitypass ();
    }


    @Override
    public void gotoSetPassWord(String validateCode) {
        if (StatusTable.PassWordTag.fromPayLogoutTag.equalsIgnoreCase (setPwdTag)){
            // 注销的
            memberReset (validateCode);
        }else {
            // 设置密码
            finish ();
            RouterManager.PassWordRouter.gotoSetPassWord (this, validateCode, setPwdTag);
        }
    }

    @Override
    protected void sendMsgCode() {
        mPresenter.sendSms (UserManager.getInstance ().getMemberNo (), scene);
    }

    @Override
    protected void verifyNext() {
        StatisticsUtils.kh_pnnextstep ();
        mPresenter.validSms (UserManager.getInstance ().getMemberNo (), msgCode,scene);
    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable ();
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

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        compositeDisposable.clear ();
    }
}
