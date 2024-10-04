package com.pasc.ewallet.dev;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import com.pasc.business.ewallet.base.EwalletBaseActivity;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.business.pay.model.PayModel;
import com.pasc.business.ewallet.business.pay.net.resp.CreatePayOrderResp;
import com.pasc.business.ewallet.business.pay.net.resp.CreateRechargeOrderResp;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.business.util.AccountUtil;
import com.pasc.business.ewallet.callback.OnOpenListener;
import com.pasc.business.ewallet.callback.OnPayListener;
import com.pasc.business.ewallet.callback.OnSignListener;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.config.Constants;
import com.pasc.business.ewallet.openapi.PASCPay;
import com.pasc.ewallet.dev.utils.AppConstant;
import com.pasc.ewallet.dev.utils.AppUtil;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.pay.common.util.ToastUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.math.BigDecimal;
import java.security.SecureRandom;

public class MainActivity extends EwalletBaseActivity implements View.OnClickListener {
    private TextView appVersionTv, home_env;
    private EditText inputOrder, et_memberNo, et_merchantNo,et_money;
    private EditText etSceneId;
    private long exitTime;
    CompositeDisposable compositeDisposable=new CompositeDisposable ();
    CheckBox check_phone;
    @Override
    protected boolean needRegisterEventBus() {
        return false;
    }

    @Override
    protected void statusBarColor() {
        setImmersiveStatusBar (getResources ().getColor (R.color.ewallet_home_title_bg_color), false);

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_home_page;
    }

    @Override
    protected void initView() {
        appVersionTv = findViewById (R.id.home_test_version);
        home_env = findViewById (R.id.home_env);
        inputOrder = findViewById (R.id.homt_input_order);
        et_memberNo = findViewById (R.id.et_memberNo);
        et_merchantNo = findViewById (R.id.et_merchantNo);
        etSceneId = findViewById(R.id.et_scene_id);
        et_money=findViewById (R.id.et_money);
        findViewById (R.id.home_goto_btn).setOnClickListener (this);
        findViewById (R.id.home_pay_btn).setOnClickListener (this);
        findViewById (R.id.home_place_btn).setOnClickListener (this);
        findViewById (R.id.home_bill_btn).setOnClickListener (this);
        findViewById (R.id.home_test_btn).setOnClickListener (this);
        findViewById (R.id.yzj_666).setOnClickListener (this);
        findViewById(R.id.recharge_create_order).setOnClickListener(this);
        findViewById(R.id.recharge).setOnClickListener(this);
        et_memberNo.setText (AppConstant.getMemberNo ());
        et_merchantNo.setText (AppConstant.getMerchantNo ());
        etSceneId.setText(AppConstant.getSceneId());
        home_env.setText (AppConstant.getTip ());
        check_phone=findViewById (R.id.check_phone);
        check_phone.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Util.needCheckPhone=isChecked;
            }
        });
        findViewById (R.id.wxSign).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String memberNo = et_memberNo.getText ().toString ();
                PASCPay.getInstance ().sign (getActivity (), memberNo, "WECHATPA",
                    etSceneId.getText().toString(), new OnSignListener () {
                    @Override
                    public void onSignResult(int code, String msg) {
                    ToastUtils.toastMsg (msg);
                    }
                });
            }
        });
        findViewById (R.id.aliSign).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String memberNo = et_memberNo.getText ().toString ();
                String merchantNo = et_merchantNo.getText ().toString ();
                PASCPay.getInstance ().sign (getActivity (), memberNo, merchantNo, "ALIPAY",
                    "com.pasc.smt", //scheme值需要与AndroidManifest中配置的App特有Scheme值一致
                    etSceneId.getText().toString(), new OnSignListener () {
                        @Override
                        public void onSignResult(int code, String msg) {
                            ToastUtils.toastMsg (msg);
                        }
                    });
            }
        });
    }

    @Override
    protected void initData(Bundle bundleData) {
        String versionDesc = "V" + AppUtil.getVersionName (this) + "_" + AppUtil.getVersionCode (this);
        String packDate = AppUtil.getDate ();
        appVersionTv.setText ("当前版本: " + versionDesc + "_" + packDate);
    }

    public void quickTest(View view){
        String memberNo = et_memberNo.getText ().toString ();
        UserManager.getInstance ().setMemberNo (memberNo);
        RouterManager.BankCardRouter.gotoAddQuickCard (this);

        Disposable  disposable = PayModel.pay ("8eb936e15acd4fa98fc023f540617678", "5001900096219", "ALIPAY", "").subscribe (new Consumer<PayResp> () {
            @Override
            public void accept(PayResp payResp) {
//                getView ().dismissLoading ();
//                getView ().paySuccess (orderType, payResp);
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
//                getView ().dismissLoading ();
//                getView ().payError (orderType, code, msg);

            }
        });

    }

    String getOrderNo() {
        int rand = (int) (10000 + (new SecureRandom().nextInt(Integer.MAX_VALUE) % 10001));
        return String.format ("jzb35zykmwlsh0%d", rand);
    }

    @Override
    public void onClick(View view) {
        String memberNo = et_memberNo.getText ().toString ();
        String merchantNo = et_merchantNo.getText ().toString ();
        if (view.getId () == R.id.home_place_btn) {
            long money=0;
            try {
                //double ff=  (Double.parseDouble (et_money.getText ().toString ()) *100);
                //if (ff<1){
                //    ToastUtils.toastMsg ("输入充值金额不能少于一分钱");
                //    return;
                //}
                //money= (long) ff;

                //解决小数点精度问题
                String moneyStr  = et_money.getText().toString();
                money = BigDecimal.valueOf(Double.parseDouble(moneyStr)).multiply(new BigDecimal(100)).longValue();
                if (money < 1){
                    ToastUtils.toastMsg ("输入充值金额不能少于一分钱");
                    return;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            if (money==0){
                ToastUtils.toastMsg ("请输入充值金额");
                return;
            }
            String orderNo = getOrderNo ();
            showLoading ("推单中...");

            Disposable createOrderDispose = PayModel.createPaymentOrder (merchantNo, orderNo,
                memberNo, money, Constants.APP_ID, etSceneId.getText().toString())
                .subscribe (new Consumer<CreatePayOrderResp> () {
                @Override
                public void accept(CreatePayOrderResp createPayOrderResp) {
                    inputOrder.setText (orderNo);
                    ToastUtils.toastMsg ("推单成功");
                    dismissLoading ();
                }
            }, new BaseRespThrowableObserver () {
                @Override
                public void onV2Error(String code, String msg) {
                    dismissLoading ();
                    com.pasc.lib.pay.common.util.ToastUtils.toastMsg (msg);
                }
            });

            compositeDisposable.add (createOrderDispose);

        } else if (view.getId () == R.id.home_pay_btn) {
            String orderNo = inputOrder.getText ().toString ().trim ();

            if (TextUtils.isEmpty (orderNo)) {
                ToastUtils.toastMsg ("请输入订单号");
                return;
            }
            PASCPay.getInstance ().pay (MainActivity.this, merchantNo, memberNo, orderNo, new OnPayListener () {
                @Override
                public void onPayResult(int code, String msg) {
                    LogUtil.loge ("code: " + code + " ,msg: " + msg);
                    if (!TextUtils.isEmpty(msg)) {
                        ToastUtils.toastMsg (msg);
                    }
                }
            });
        } else if (view.getId () == R.id.home_bill_btn) {
            RouterManager.BalanceBillRouter.gotoBillList (this, memberNo);
        } else if (view.getId () == R.id.home_test_btn) {
            MyListDialog myListDialog = new MyListDialog (this);
            myListDialog.show ();
        } else if (view.getId () == R.id.home_goto_btn) {
            PASCPay.getInstance ().open (this, "", memberNo, new OnOpenListener () {
                @Override
                public void onOpenResult(int code, String msg) {
                    LogUtil.loge ("code: " + code + " ,msg: " + msg);
                }

                @Override
                public void onStart() {
//                    showLoading ();
                }

                @Override
                public void onEnd() {
//                    dismissLoading ();
                }
            });


        }else if (view.getId ()==R.id.yzj_666){
//            gotoActivity (TestActivity.class);
            gotoActivity (JihuoActivity.class);
//            MemberValidResp resp=new MemberValidResp ();
//            resp.isTrading=true;
//            resp.balance=1000;
//            resp.withdrawAmt=0;
//            RouterManager.LogoutRouter.gotoLogoutDisable (this,resp);

        } else if(view.getId () == R.id.recharge_create_order) {
            if (TextUtils.isEmpty(memberNo)) {
                ToastUtils.toastMsg ("请输入会员号");
                return;
            }
            if (TextUtils.isEmpty(merchantNo)) {
                ToastUtils.toastMsg ("请输入商户号");
                return;
            }
            long money = 0;
            try {
                double ff = (Double.parseDouble(et_money.getText().toString()) * 100);
                if (ff < 1) {
                    ToastUtils.toastMsg("充值金额不能少于一分钱");
                    return;
                }
                money = (long) ff;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (money == 0) {
                ToastUtils.toastMsg("请输入充值金额");
                return;
            }
            String orderNo = getOrderNo ();
            showLoading ("推单中...");

            Disposable createOrderDispose = PayModel.createRechargeOrder (orderNo, memberNo, money)
                .subscribe (new Consumer<CreateRechargeOrderResp> () {
                    @Override
                    public void accept(CreateRechargeOrderResp createPayOrderResp) {
                        inputOrder.setText (orderNo);
                        ToastUtils.toastMsg ("推单成功");
                        dismissLoading ();
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        dismissLoading ();
                        ToastUtils.toastMsg (msg);
                    }
                });

            compositeDisposable.add (createOrderDispose);
        } else if (view.getId () == R.id.recharge) {
            double inputMoney = AccountUtil.formatInputMoneyNum (et_money.getText ().toString());
            if (inputMoney == 0) {
                ToastUtils.toastMsg (com.pasc.business.ewallet.R.string.ewallet_toast_input_money_zero);
                return;
            }
            String orderNo = inputOrder.getText ().toString ().trim ();
            PASCPay.getInstance().recharge(this, memberNo, orderNo,
                (long) (inputMoney * 100),
                new OnPayListener() {
                    @Override public void onPayResult(int code, String msg) {
                        LogUtil.loge ("code: " + code + " ,msg: " + msg);
                    }
                });
        }
    }



    @Override
    protected void onDestroy() {
        compositeDisposable.clear ();
        super.onDestroy ();
    }

    @Override
    public void onBackPressed() {
        exit ();
    }

    /**
     * 双击退出
     */
    private void exit() {
        if ((System.currentTimeMillis () - exitTime) > 2000) {
            ToastUtils.toastMsg ("再按一次退出");
            exitTime = System.currentTimeMillis ();
        } else {
            super.onBackPressed ();
        }
    }

}
