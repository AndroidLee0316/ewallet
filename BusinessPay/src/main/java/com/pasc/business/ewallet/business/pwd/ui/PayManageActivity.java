package com.pasc.business.ewallet.business.pwd.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseActivity;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.business.logout.model.LogoutModel;
import com.pasc.business.ewallet.business.logout.net.resp.MemberValidResp;
import com.pasc.business.ewallet.common.utils.StatisticsUtils;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.pay.common.util.ToastUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * 支付管理页面，即修改支付密码&忘记支付密码的入口页面
 */
public class PayManageActivity extends EwalletBaseActivity implements View.OnClickListener{
    CompositeDisposable compositeDisposable=new CompositeDisposable ();
    @Override
    protected int layoutResId () {
        return R.layout.ewallet_activity_pay_manage_pay;
    }

    @Override
    protected void initView () {
        PascToolbar toolbar = findViewById(R.id.ewallet_activity_toolbar);
        RelativeLayout ewallet_pay_manage_modify_rl = findViewById(R.id.ewallet_pay_manage_modify_rl);
        RelativeLayout ewallet_pay_manage_forgot_rl = findViewById(R.id.ewallet_pay_manage_forgot_rl);

        toolbar.setTitle(getString(R.string.ewallet_manager_for_pay));
        toolbar.enableUnderDivider(true);
        toolbar.addCloseImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finish();
            }
        });

        ewallet_pay_manage_modify_rl.setOnClickListener(this);
        ewallet_pay_manage_forgot_rl.setOnClickListener(this);
        findViewById(R.id.ewallet_pay_manage_logout_rl).setOnClickListener(this);
    }

    @Override
    protected void initData (Bundle bundleData) {
    }

    @Override
    public void onClick (View v) {
        if (v.getId() == R.id.ewallet_pay_manage_modify_rl){
            //跳转到修改支付密码页面
            RouterManager.PassWordRouter.gotoModifyPwd (this);
            StatisticsUtils.qb_setting_modifyPwd ();
        } else if (v.getId() == R.id.ewallet_pay_manage_forgot_rl){
            //跳转到忘记支付密码页面
            RouterManager.PassWordRouter.gotoForgetPwd (getActivity ());
            StatisticsUtils.qb_setting_forgetPwd ();
        }else if (v.getId() == R.id.ewallet_pay_manage_logout_rl){
            memberCancelValid ();
        }
    }

    void memberCancelValid(){
        showLoading ();
        compositeDisposable.add( LogoutModel.memberCancelValid (UserManager.getInstance ().getMemberNo ())
               .subscribe (new Consumer<MemberValidResp> () {
                   @Override
                   public void accept(MemberValidResp memberValidResp) throws Exception {
                       dismissLoading ();
                        if (memberValidResp.allowCancel){
                            RouterManager.LogoutRouter.gotoLogoutEnable (getActivity ());
                        }else {
                            RouterManager.LogoutRouter.gotoLogoutDisable (getActivity (),memberValidResp);
                        }
                   }
               }, new BaseRespThrowableObserver () {
                   @Override
                   public void onV2Error(String code, String msg) {
                       dismissLoading ();
                       ToastUtils.toastMsg (msg);
                   }
               })) ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        compositeDisposable.clear ();
    }
}
