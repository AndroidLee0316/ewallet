package com.pasc.ewallet.dev;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pasc.business.ewallet.base.EwalletBaseActivity;
import com.pasc.business.ewallet.business.rechargewithdraw.presenter.WithDrawPwdDialog;
import com.pasc.business.ewallet.business.pwd.model.PwdModel;
import com.pasc.ewallet.dev.utils.AppConstant;
import com.pasc.lib.keyboard.EwalletPayView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.netpay.resp.VoidObject;
import com.pasc.lib.pay.common.util.ToastUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * @date 2019/8/16
 * @des
 * @modify
 **/
public class JihuoActivity extends EwalletBaseActivity {

    private EditText memberNo;
    private EditText certificateNo;
    private EditText memberName,merchantNo,phone,card;

    CompositeDisposable compositeDisposable=new CompositeDisposable ();



    @Override
    protected int layoutResId() {
        return R.layout.jihuo_activity;
    }

    @Override
    protected void initView() {
        memberNo = findViewById(R.id.memberNo);
        certificateNo = findViewById(R.id.certificateNo);
        memberName = findViewById(R.id.memberName);

        merchantNo = findViewById(R.id.merchantNo);
        phone = findViewById(R.id.phone);
        card = findViewById(R.id.card);

        memberName.setText (AppConstant.getName ());
        memberNo.setText (AppConstant.getMemberNo ());
        merchantNo.setText (AppConstant.getMerchantNo ());
        certificateNo.setText (AppConstant.getIdNo ());

        phone.setText (AppConstant.getPhone ());
        card.setText (AppConstant.getCard ());

    }

    @Override
    protected void initData(Bundle bundleData) {

    }


    public void viewClick(View view){

        if (view.getId ()==R.id.testPwd){
            WithDrawPwdDialog withDrawPwdDialog = new WithDrawPwdDialog (this)
                    .setMoneyText ("20")
                    .setPayFeeText ("实际到账："+1+" (扣除服务费"+19+"）")
                    .setPayTypeText ("提现");
            withDrawPwdDialog.setPasswordInputListener (new EwalletPayView.PasswordInputListener () {
                @Override
                public void onPasswordInputFinish(String password) {
                }

                @Override
                public void onSimplePasswordInput() {

                }
            });
            withDrawPwdDialog.show ();
            return;
        }

         String memberNo=this.memberNo.getText ().toString ();
         String certificateNo=this.certificateNo.getText ().toString ();
         String memberName=this.memberName.getText ().toString ();
        showLoading ();
        compositeDisposable.add( PwdModel.addCert (memberNo,certificateNo,memberName).subscribe (new Consumer<VoidObject> () {
            @Override
            public void accept(VoidObject voidObject) {
                dismissLoading ();
                ToastUtils.toastMsg ("ok");

            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                dismissLoading ();
                ToastUtils.toastMsg (msg);
            }
        }));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear ();
        super.onDestroy ();
    }
}
