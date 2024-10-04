package com.pasc.business.ewallet.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;


/**
 * Created by ex-huangzhiyi001 on 2019/2/19.
 */
public abstract class EwalletBaseMvpFragment<P extends EwalletIPresenter> extends EwalletBaseFragment
        implements EwalletBaseView {
    protected P mPresenter;

    protected abstract P createPresenter();

    @Override
    protected void setContViewBefore() {
        mPresenter = createPresenter ();
        if (mPresenter != null) {
            mPresenter.onMvpAttachView (this, getArguments ());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onMvpViewCreated (view, savedInstanceState);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView ();
        if (mPresenter != null) {
            mPresenter.onMvpDetachView (false);
            mPresenter.onMvpDestroyView ();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach (context);
        if (mPresenter != null) {
            mPresenter.onMvpAttach (context);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach ();
        if (mPresenter != null) {
            mPresenter.onMvpDetach ();
        }
    }


    @Override
    public void onStart() {
        super.onStart ();
        if (mPresenter != null) {
            mPresenter.onMvpStart ();
        }
    }

    @Override
    public void onResume() {
        super.onResume ();
        if (mPresenter != null) {
            mPresenter.onMvpResume ();
        }
    }

    @Override
    public void onPause() {
        super.onPause ();
        if (mPresenter != null) {
            mPresenter.onMvpPause ();
        }
    }

    @Override
    public void onStop() {
        super.onStop ();
        if (mPresenter != null) {
            mPresenter.onMvpStop ();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState (outState);
        if (mPresenter != null) {
            mPresenter.onMvpSaveInstanceState (outState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
        if (mPresenter != null) {
            mPresenter.onMvpDestroy ();
        }
    }





}
