package com.pasc.business.ewallet.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.pasc.business.ewallet.NotProguard;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangzijian
 * @date 2019/2/18
 * @des   多个 P 对应多个 V
 * @modify
 **/
@NotProguard
public class MultiPresenter<V extends EwalletBaseView> extends EwalletBasePresenter<V> {

    private List<EwalletIPresenter> presenters = new ArrayList<> ();

    @SafeVarargs
    public final <Q extends EwalletIPresenter<V>> void requestPresenter(Q... cls) {
        for (Q cl : cls) {
            presenters.add (cl);
        }
    }

    @Override
    public void onMvpAttachView(V view, Bundle savedInstanceState) {
        for (EwalletIPresenter presenter : presenters) {
            presenter.onMvpAttachView (view,savedInstanceState);
        }
    }

    @Override
    public void onMvpStart() {
        for (EwalletIPresenter presenter : presenters) {
            presenter.onMvpStart ();
        }
    }

    @Override
    public void onMvpResume() {
        for (EwalletIPresenter presenter : presenters) {
            presenter.onMvpResume ();
        }
    }

    @Override
    public void onMvpPause() {
        for (EwalletIPresenter presenter : presenters) {
            presenter.onMvpPause ();
        }
    }

    @Override
    public void onMvpStop() {
        for (EwalletIPresenter presenter : presenters) {
            presenter.onMvpStop ();
        }
    }

    @Override
    public void onMvpSaveInstanceState(Bundle savedInstanceState) {
        for (EwalletIPresenter presenter : presenters) {
            presenter.onMvpSaveInstanceState (savedInstanceState);
        }
    }

    @Override
    public void onMvpDetachView(boolean retainInstance) {
        for (EwalletIPresenter presenter : presenters) {
            presenter.onMvpDetachView (retainInstance);
        }
    }

    @Override
    public void onMvpDestroy() {
        for (EwalletIPresenter presenter : presenters) {
            presenter.onMvpDestroy ();
        }
    }

    @Override
    public void onMvpViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        for (EwalletIPresenter presenter : presenters) {
            presenter.onMvpViewCreated(view, savedInstanceState);
        }
    }

    @Override
    public void onMvpDestroyView () {
        for (EwalletIPresenter presenter : presenters) {
            presenter.onMvpDestroyView();
        }
    }

    @Override
    public void onMvpAttach (Context context) {
        for (EwalletIPresenter presenter : presenters) {
            presenter.onMvpAttach(context);
        }
    }

    @Override
    public void onMvpDetach () {
        for (EwalletIPresenter presenter : presenters) {
            presenter.onMvpDetach();
        }
    }
}

