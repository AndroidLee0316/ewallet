package com.pasc.business.ewallet.business.account.ui;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.account.net.resp.QueryMemberResp;
import com.pasc.business.ewallet.business.account.presenter.CreateAccountPresenter;
import com.pasc.business.ewallet.business.account.view.CreateAccountView;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.QuitAccountCreateEventType;
import com.pasc.business.ewallet.common.utils.StatisticsUtils;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.config.Constants;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * 开通账户
 */
public class CreateAccountActivity extends EwalletBaseMvpActivity<CreateAccountPresenter>
        implements CreateAccountView, View.OnClickListener {
    public PascToolbar ewallet_activity_toolbar;
    public TextView ewallet_create_account_tv_name;
    public TextView ewallet_create_account_tv_des;
    public ImageView ewallet_create_account_checkbox_agree;
    public TextView ewallet_create_account_tv_agree;
    public Button ewallet_create_account_btn_create;
    public LinearLayout ewallet_create_account_ll;
    private boolean agree = false;
    private String memberNo;
    @Override
    protected EventBusObserver registerEventBus() {
        return new EventBusObserver () {
            @Override
            public void handleMessage(BaseEventType type) {
                if (type instanceof QuitAccountCreateEventType) {
                    if (((QuitAccountCreateEventType) type).quitType==QuitAccountCreateEventType.notQuitCreate){
                        return;
                    }
                    //关闭
                    finish ();
                }
            }
        };
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_activity_account_create_account;
    }

    @Override
    protected void initView() {
        PascToolbar toolbar = findViewById (R.id.ewallet_activity_toolbar);
        ewallet_create_account_tv_name = findViewById (R.id.ewallet_create_account_tv_name);
        ewallet_create_account_tv_des = findViewById (R.id.ewallet_create_account_tv_des);
        ewallet_create_account_checkbox_agree = findViewById (R.id.ewallet_addcard_info_checkbox_agree);
        ewallet_create_account_tv_agree = findViewById (R.id.ewallet_addcard_info_tv_agree);
        ewallet_create_account_btn_create = findViewById (R.id.ewallet_create_account_btn_create);
        ewallet_create_account_ll = findViewById (R.id.ewallet_create_account_ll);

        toolbar.setTitle (getString (R.string.ewallet_create_account));
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });

        ewallet_create_account_tv_name.setText (R.string.ewallet_who_name);
        ewallet_create_account_tv_des.setText (R.string.ewallet_service_desc);

        String inner = getString (R.string.ewallet_create_account_protocol);
        StringBuilder sb = new StringBuilder ();
        String prefix = getString (R.string.ewallet_agree);
        String suffix = "";
        sb.append (prefix);
        if (!TextUtils.isEmpty (inner)) {
            sb.append (inner);
        }
        String content = sb.append (suffix).toString ();

        SpannableString spannableString = new SpannableString (content);
        spannableString.setSpan (new ForegroundColorSpan (getResources ().getColor (R.color.ewallet_theme_color)),
                prefix.length (), spannableString.length () - suffix.length (), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ewallet_create_account_tv_agree.setText (spannableString);
        ewallet_create_account_tv_agree.setOnClickListener (this);
        ewallet_create_account_checkbox_agree.setOnClickListener (this);
        ewallet_create_account_btn_create.setOnClickListener (this);
        updateBtnStatus (agree);

    }

    @Override
    protected void initData(Bundle bundleData) {
        //获取用户信息
        memberNo = bundleData.getString (BundleKey.Pay.key_memberNo, UserManager.getInstance ().getMemberNo ());
        if (!Util.isEmpty (memberNo)) {
        } else {
            finish ();
            return;
        }
    }

    @Override
    protected CreateAccountPresenter createPresenter() {
        return new CreateAccountPresenter ();
    }

    @Override
    public void onClick(View v) {
        if (v.getId () == R.id.ewallet_addcard_info_checkbox_agree) {
            agree = !agree;
            if (agree) {
                ewallet_create_account_checkbox_agree.setImageResource (R.drawable.ewallet_rect_check);
            } else {
                ewallet_create_account_checkbox_agree.setImageResource (R.drawable.ewallet_rect_uncheck);
            }
            updateBtnStatus (agree);
        } else if (v.getId () == R.id.ewallet_create_account_btn_create) {
            gotoNext ();
        } else if (v.getId () == R.id.ewallet_addcard_info_tv_agree) {
            RouterManager.gotoWeb (this, "", Constants.OPENACCOUNT_SERVICE_PROTOCOL);
        }
    }

    void updateBtnStatus(boolean agree) {
        ewallet_create_account_btn_create.setEnabled (agree);
    }

    void gotoNext() {
        //开户拦截，系统维护
        if (!agree) {
            return;
        }
        StatisticsUtils.kh_create ();
        StatisticsUtils.kh_create_uv ();
        mPresenter.queryMemberByMemberNo (memberNo);
    }


    @Override
    public void queryQueryMemberSuccess(QueryMemberResp memberResp) {
        RouterManager.PassWordRouter.gotoCreateCertification (this,memberResp.phone);
    }

    @Override
    public void queryQueryMemberError(String code, String msg) {
        ToastUtils.toastMsg (msg);
    }
}
