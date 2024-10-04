package com.pasc.business.ewallet.business.pay.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.pasc.business.ewallet.BuildConfig;
import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.pay.fragment.BasePayFragment;
import com.pasc.business.ewallet.business.pay.fragment.FragmentFactory;
import com.pasc.business.ewallet.business.pay.fragment.PayMainFragment;
import com.pasc.business.ewallet.business.pay.net.resp.PayContextResp;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.business.pay.net.resp.PayTypeBean;
import com.pasc.business.ewallet.business.pay.presenter.PayMainStandPresenter;
import com.pasc.business.ewallet.business.pay.view.PayCallbackView;
import com.pasc.business.ewallet.business.pay.view.PayMainStandView;
import com.pasc.business.ewallet.callback.OnActivityLifecycleCallback;
import com.pasc.business.ewallet.callback.OnPayTypeClickListener;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.QuickPayStandEvent;
import com.pasc.business.ewallet.common.event.RefreshQuickCardEvent;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.common.utils.StatisticsUtils;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.config.PayBehaviorHandler;
import com.pasc.business.ewallet.config.PayBuildConfig;
import com.pasc.business.ewallet.config.PayTypeConfig;
import com.pasc.business.ewallet.inner.PayManager;
import com.pasc.business.ewallet.result.PASCPayResult;
import com.pasc.lib.pay.common.util.ToastUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 收银台
 *
 * @date 2019/7/25
 * @des
 * @modify
 **/
@NotProguard
public class PayMainStandActivity extends EwalletBaseMvpActivity<PayMainStandPresenter> implements
    PayMainStandView, PayCallbackView {
    private static final String TAG = "PayMainStandActivity";
    @Override
    protected PayMainStandPresenter createPresenter() {
        return new PayMainStandPresenter ();
    }

    String merchantName, merchantNo, memberNo, mchOrderNo, payMode;
    private ViewPager viewPager;
    private long rechargeAmount/**充值余额***/
            , orderAmount;
    /***订单余额**/
    private String channel, payType, payTypeName, cardKey;
    private String unionOrderId, payDate, quickCardBindPhone;
    private String bankCardName;
    private String payOption;

    private PayMainFragment mainFragment;

    /***支付选项**/
    public boolean isPayMode() {
        return StatusTable.PayMode.payMode.equalsIgnoreCase (payMode);
    }

    private boolean isAnim = false, isFinishing = false;

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_pay_main_activity;
    }

    @Override
    protected void setContViewBefore(Bundle savedInstanceState) {
        //if (Constants.IS_DEBUG) {
        //    //支付宝沙箱环境
        //    EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        //}
        super.setContViewBefore(savedInstanceState);
    }

    @Override
    protected void initView() {
        FragmentFactory.instance ().clearAll ();
        viewPager = findViewById (R.id.ewallet_viewpager);
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter (getSupportFragmentManager ()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = FragmentFactory.instance ().newInstance (position);
                if (getIntent () != null) {
                    fragment.setArguments (getIntent ().getExtras ());
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return FragmentFactory.FragmentSize;
            }
        };
        viewPager.setAdapter (pagerAdapter);
        viewPager.setOffscreenPageLimit (FragmentFactory.FragmentSize);
        viewPager.setVisibility (View.INVISIBLE);
        isFinishing = false;
        isAnim = false;
        PayManager.getInstance().setOnPayTypeClickListener(new OnPayTypeClickListener() {
            @Override public void onPayTypeChoose(Activity activity, String payType, PayResp payBean) {
                PayBehaviorHandler payBehaviorHandler = PayTypeConfig.getInstance().getCustomerBehaviors().get(payType);
                if (payBehaviorHandler != null) {
                    payBehaviorHandler.handlerPay(activity, payType, payBean);
                }
            }
        });
        PayManager.getInstance().setOnActivityLifecycleCallback(new OnActivityLifecycleCallback() {
            @Override public void onActivityResult(Activity activity, String payType, int requestCode, int resultCode, Intent data) {
                PayBehaviorHandler payBehaviorHandler = PayTypeConfig.getInstance().getCustomerBehaviors().get(payType);
                if (payBehaviorHandler != null) {
                    payBehaviorHandler.onActivityResult(activity, payType, requestCode, resultCode, data);
                }
            }

            @Override public void onNewIntent(Activity activity, String payType, Intent intent) {
                PayBehaviorHandler payBehaviorHandler = PayTypeConfig.getInstance().getCustomerBehaviors().get(payType);
                if (payBehaviorHandler != null) {
                    payBehaviorHandler.onNewIntent(activity, payType, intent);
                }
            }

            @Override
            public void onRequestPermissionsResult(Activity activity, String payType, int requestCode, @NonNull
                String[] permissions, @NonNull int[] grantResults) {
                PayBehaviorHandler payBehaviorHandler = PayTypeConfig.getInstance().getCustomerBehaviors().get(payType);
                if (payBehaviorHandler != null) {
                    payBehaviorHandler.onRequestPermissionsResult(activity, payType, requestCode, permissions, grantResults);
                }
            }

            @Override public void onDestroy(Activity activity, String payType) {
                if (TextUtils.isEmpty(payType)) {
                    return;
                }
                PayBehaviorHandler payBehaviorHandler = PayTypeConfig.getInstance().getCustomerBehaviors().get(payType);
                if (payBehaviorHandler != null) {
                    payBehaviorHandler.onDestroy(activity, payType);
                }
            }
        });
    }

    @Override
    protected void statusBarColor() {
    }

    @Override
    protected void initData(Bundle bundleData) {
        merchantNo = bundleData.getString (BundleKey.Pay.key_merchantNo);
        memberNo = bundleData.getString (BundleKey.Pay.key_memberNo);
        mchOrderNo = bundleData.getString (BundleKey.Pay.key_mchOrderNo);
        payMode = bundleData.getString (BundleKey.Pay.key_pay_mode, StatusTable.PayMode.payMode);
        rechargeAmount = bundleData.getLong (BundleKey.Pay.key_money, 0);
        payOption = bundleData.getString (BundleKey.Pay.key_payOption, StatusTable.PayOption.DefaultOp);
        refreshData (false);

    }

    void refreshData(boolean isRefresh) {
        if (isPayMode ()) {
            boolean errFlag = false;
            LogUtil.loge (getSimpleName () + " merchantNo: " + merchantNo + " ,memberNo: " + memberNo + " ,mchOrderNo: " + mchOrderNo);
            if (BuildConfig.currentBuildType == PayBuildConfig.jhPayBuildType){
                if (Util.isEmpty (mchOrderNo)) {
                    errFlag = true;
                    ToastUtils.toastMsg (R.string.ewallet_toast_order_no_empty);
                }
            }else {
                if (Util.isEmpty (mchOrderNo)) {
                    errFlag = true;
                    ToastUtils.toastMsg (R.string.ewallet_toast_order_no_empty);
                } else if (Util.isEmpty (merchantNo)) {
                    errFlag = true;
                    ToastUtils.toastMsg (R.string.ewallet_toast_merchant_no_empty);
                }
                //else if (Util.isEmpty (memberNo)) {
                //    errFlag = true;
                //    ToastUtils.toastMsg (R.string.ewallet_toast_member_no_empty);
                //}
            }

            if (errFlag) {
                finishPayActivity ();
            } else {
                mPresenter.getPayContext (merchantNo, memberNo, mchOrderNo, StatusTable.Scenes.PAY_SB, isRefresh);
            }
        } else {
            //if (Util.isEmpty (memberNo)) {
            //    ToastUtils.toastMsg (R.string.ewallet_toast_member_no_empty);
            //    finishPayActivity ();
            //} else {
            //    mPresenter.getPayContext (null, memberNo, null, StatusTable.Scenes.RECHARGE, isRefresh);
            //}
            mPresenter.getPayContext (null, memberNo, null, StatusTable.Scenes.RECHARGE, isRefresh);
        }
    }

    @Override
    protected EventBusObserver registerEventBus() {
        return new EventBusObserver () {
            @Override
            public void handleMessage(BaseEventType eventType) {
                if (eventType instanceof RefreshQuickCardEvent) {
                    //刷新银联数据,由 PascPayWebViewActivity 触发
                    refreshData (true);
                } else if (eventType instanceof QuickPayStandEvent) {
                    finishPayActivity ();
                }
            }
        };
    }

    @Override
    public void queryPayContextSuccess(PayContextResp typeBeans, boolean isRefresh) {
        LogUtil.loge ("queryPayContextSuccess 查询订单成功： " + typeBeans.toString ());
        showPayMain (typeBeans, isRefresh);
        if (!isRefresh) {
            StatisticsUtils.start_sdk ();
        }
    }

    @Override
    public void queryPayContextError(String code, String msg, boolean isRefresh) {
        LogUtil.loge ("queryPayContextError 查询订单失败： " + msg);
        ToastUtils.toastMsg (msg);
        if (!isRefresh) {
            if (isPayMode ()) {
                if (PayManager.getInstance ().getOnPayListener () != null) {
                    PayManager.getInstance ().getOnPayListener ().onPayResult (PASCPayResult.PASC_PAY_CODE_FAILED, msg);
                }
            }
            finishPayActivity ();
        }
    }

    @Override
    public void onBackPressed() {
        BasePayFragment payFragment = FragmentFactory.instance ().getFragment (viewPager.getCurrentItem ());
        if (payFragment != null) {
            payFragment.onBackPressed ();
        }

    }

    @Override
    public void payCancel() {
        if (!isAnim && !isFinishing) {
            if (isPayMode ()) {
                if (PayManager.getInstance ().getOnPayListener () != null) {
                    PayManager.getInstance ().getOnPayListener ().onPayResult (PASCPayResult.PASC_PAY_CODE_CANCELED, PASCPayResult.PASC_PAY_MSG_CANCELED);
                }
            }
            closePayActivity (true);
        }
    }

    @Override
    public void payError(String msg) {
        if (isPayMode ()) {
            if (PayManager.getInstance ().getOnPayListener () != null) {
                PayManager.getInstance ().getOnPayListener ().onPayResult (PASCPayResult.PASC_PAY_CODE_FAILED, msg);
            }
        }
        closePayActivity (true);
    }

    @Override
    public String getMerchantName() {
        return merchantName;
    }

    @Override
    public String getPayTypeName() {
        return payTypeName;
    }

    @Override
    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    @Override
    public String getPayType() {
        return payType;
    }

    @Override
    public long getOrderAmount() {
        return orderAmount;
    }

    @Override
    public String getBankCardName() {
        return bankCardName;
    }

    @Override
    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    @Override
    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Override public String getChannel() {
        return channel;
    }

    @Override public void setChannel(String channel) {
        this.channel = channel;
    }


    @Override
    public String getCardKey() {
        return cardKey;
    }

    @Override
    public void setCardKey(String cardKey) {
        this.cardKey = cardKey;
    }

    @Override
    public String getQuickCardPhone() {
        return quickCardBindPhone;
    }

    @Override
    public void setQuickCardPhone(String bindPhone) {
        this.quickCardBindPhone = bindPhone;
    }

    @Override
    public String getUnionOrderId() {
        return unionOrderId;
    }

    @Override
    public void setUnionOrderId(String unionOrderId) {
        this.unionOrderId = unionOrderId;
    }

    @Override
    public String getPayDate() {
        return payDate;
    }

    @Override
    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public void showPayMain(PayContextResp contextBean, boolean isRefresh) {

        if (StatusTable.PayMode.rechargeMode.equalsIgnoreCase (payMode)) {
            contextBean.orderAmount = rechargeAmount;
            contextBean.merchantName = "余额充值";
        }
        if (StatusTable.PayOption.WxCardPaNoBindOp.equals (payOption)) {
            //1.支付方式只有一种时，回调通知绑卡，同时关闭收银台
            //2.支付方式有多种时, 剔除无锡卡支付方式
            if (contextBean.list != null) {
                if (contextBean.list.size () == 1 && StatusTable.PayType.WXCITIZEN.equalsIgnoreCase (contextBean.list.get (0).payType)) {
                    finishPayActivity ();
                    if (PayManager.getInstance ().getOnPayListener () != null) {
                        PayManager.getInstance ().getOnPayListener ().onOption (StatusTable.PayOption.WxCardPaNoBindOp, new HashMap<> ());
                    }
                    return;
                } else {
                    deletePayTypes (contextBean, StatusTable.PayType.WXCITIZEN);
                }
            }
        } else if (StatusTable.PayOption.WxCardPayHasBindOp.equals (payOption)) {
            //do nothing,无锡市民卡已经绑卡
        } else {
            // 过滤支付方式
            deletePayTypes (contextBean, StatusTable.PayType.WXCITIZEN);
        }

        merchantName = contextBean.merchantName;
        orderAmount = contextBean.orderAmount;
        mainFragment = FragmentFactory.instance ().getFragment (FragmentFactory.MainPosition);
        if (mainFragment != null) {
            mainFragment.setData (contextBean, isRefresh);
        }
        if (!isRefresh) {
            Animation enterAnim = AnimationUtils.loadAnimation (getActivity (), R.anim.ewallet_widget_dialog_bottom_in);
            viewPager.startAnimation (enterAnim);
            viewPager.setVisibility (View.VISIBLE);
        }

    }

    void deletePayTypes(PayContextResp contextBean, String... payTypes) {
        if (payTypes != null && payTypes.length > 0) {
            // 过滤支付方式
            List<PayTypeBean> payTypeBeans = new ArrayList<> ();
            List<String> payTypeList = Arrays.asList (payTypes);
            if (contextBean.list != null) {
                for (PayTypeBean payTypeBean : contextBean.list) {
                    if (!payTypeList.contains (payTypeBean.payType)) {
                        payTypeBeans.add (payTypeBean);
                    }
                }
            }
            contextBean.list = payTypeBeans;
        }
    }

    @Override
    public void closePayActivity(boolean needFinishActivity) {
        if (Util.ignoreAnim ()) {
            finishPayActivity ();
            return;
        }
        if (!isAnim) {
            isAnim = true;
            Animation outAnim = AnimationUtils.loadAnimation (getActivity (), R.anim.ewallet_widget_dialog_bottom_out);
            outAnim.setAnimationListener (new Animation.AnimationListener () {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnim = false;
                    if (needFinishActivity) {
                        finishPayActivity ();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            viewPager.startAnimation (outAnim);
        }
    }

    @Override
    public void closePayActivityDelay() {
//        viewPager.postDelayed (new Runnable () {
//            @Override
//            public void run() {
//                LogUtil.loge ("closePayActivityDelay");
//                finishPayActivity ();
//            }
//        },500);
        isFinishing = true;
        finish ();
        try {
            //淡出动画 更和谐一些
            overridePendingTransition (0, R.anim.ewallet_anim_fade_out_ac);
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void finishPayActivity() {
        isFinishing = true;
        finish ();
        try {
            overridePendingTransition (0, 0);
        } catch (Exception e) {
            e.printStackTrace ();
        }

    }

    @Override
    public void switchToPage(int position) {
        int currentPosition = viewPager.getCurrentItem ();
        if (Math.abs (currentPosition - position) > 1) {
            viewPager.setCurrentItem (position, false);
        } else {
            viewPager.setCurrentItem (position);
        }
    }

    @Override
    protected void onDestroy() {
        FragmentFactory.instance ().clearAll ();
        super.onDestroy ();
        PayManager.getInstance().getOnActivityLifecycleCallback().onDestroy(this, getFullPayType());
    }

    @Override
    protected boolean needSafeCheck() {
        return true;
    }

    @Override
    protected boolean needSafeToast() {
        return false;
    }

    @Override protected void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);
        setIntent(intent);
        PayManager.getInstance().getOnActivityLifecycleCallback().onNewIntent(this, getFullPayType(), intent);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PayManager.getInstance().getOnActivityLifecycleCallback().onActivityResult(
            this, getFullPayType(), requestCode, resultCode, data);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PayManager.getInstance().getOnActivityLifecycleCallback().onRequestPermissionsResult(
            this, getFullPayType(), requestCode, permissions, grantResults);
    }

    public String getFullPayType() {
        return getChannel() + "_" + getPayType();
    }

}
