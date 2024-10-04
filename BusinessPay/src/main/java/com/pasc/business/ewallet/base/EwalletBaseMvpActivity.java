package com.pasc.business.ewallet.base;

import android.os.Bundle;
import com.pasc.business.ewallet.NotProguard;

/**
 * MVP的Activity基类：
 * 纯粹的 MVP 包装，不要增加任何View层基础功能
 * 如果要添加基类功能，请在{@link EwalletBaseActivity} 中添加
 */
@NotProguard
public abstract class EwalletBaseMvpActivity<P extends EwalletIPresenter> extends EwalletBaseActivity
        implements EwalletBaseView{
    protected P mPresenter;

    protected abstract P createPresenter();

    @Override
    protected void setContViewAfter(Bundle savedInstanceState) {
        mPresenter = createPresenter();
        if (mPresenter == null) {
            throw new NullPointerException("Presenter is null! Do you return null in createPresenter()?");
        }
        mPresenter.onMvpAttachView(this, savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onMvpStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onMvpResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onMvpPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.onMvpStop();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null) {
            mPresenter.onMvpSaveInstanceState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onMvpDestroyView();
            mPresenter.onMvpDetachView(false);
            mPresenter.onMvpDestroy();
        }
    }

}
