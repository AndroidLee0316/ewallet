package com.pasc.business.ewallet.business.traderecord.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.util.FragmentUtils;
import com.pasc.business.ewallet.common.customview.ClearEditText;
import com.pasc.business.ewallet.common.utils.KeyBoardUtils;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * @date 2019/7/8
 * @des
 * @modify
 **/
public class BillListSearchActivity extends EwalletBaseActivity {
    ClearEditText etSearch;
    View container;
    private BaseBillListFragment billFragment;

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_pay_bill_search_activity;
    }

    private String getKeyword() {
        return etSearch.getText ().toString ().replace (" ","");
    }

    @Override
    protected void initView() {
        etSearch = findViewById (R.id.ewallet_edit_search);
        etSearch.setOnEditorActionListener (new TextView.OnEditorActionListener () {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtils.closeKeybord (etSearch, getActivity ());
                    String keyword = getKeyword ();
                    if (Util.isEmpty (keyword)) {
                        ToastUtils.toastMsg ("请输入搜索内容");
                        return true;
                    }
                    if (billFragment!=null){
                        billFragment.updateKeyword (keyword);
                    }
                }
                return false;
            }
        });
        etSearch.setEditTextChangeListener (new ClearEditText.EditTextChangeListener () {
            @Override
            public void afterChange(String keyword) {
            }
        });
        container = findViewById (R.id.ewallet_pay_bill_container);
        billFragment = (BaseBillListFragment) getSupportFragmentManager ()
                .findFragmentById (R.id.ewallet_pay_bill_container);

        if (billFragment == null) {
            billFragment = new BillListFragmentYC ();
            Bundle bundle = null;
            if (getIntent () != null) {
                Intent intent = getIntent ();
                bundle = intent.getExtras ();
            }
            if (bundle == null) {
                bundle = new Bundle ();
            }
            bundle.putBoolean (BundleKey.Trade.key_search_flag,true);
            billFragment.setArguments (bundle);
            FragmentUtils.showTargetFragment (getSupportFragmentManager (),
                    billFragment, R.id.ewallet_pay_bill_container);
        }

        findViewById (R.id.ewallet_pay_search_cancel).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
    }

    @Override
    protected void initData(Bundle bundleData) {

    }

}
