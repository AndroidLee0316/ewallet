package com.pasc.business.ewallet.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.pasc.business.ewallet.NotProguard;
import java.lang.ref.WeakReference;

/**
 * 控制器基类：
 * Presenter生命周期包装、View的绑定和解除，P层实现的基类
 */
@NotProguard
public class EwalletBasePresenter<V extends EwalletBaseView> implements EwalletIPresenter<V> {

    private WeakReference<V> viewRef;

    protected V getView() {
        return viewRef.get();
    }

    protected boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    private void _attach(V view, Bundle savedInstanceState) {
        viewRef = new WeakReference<V>(view);
    }

    @Override
    public void onMvpAttachView(V view, Bundle savedInstanceState) {
        _attach(view, savedInstanceState);
    }

    @Override
    public void onMvpStart() {

    }

    @Override
    public void onMvpResume() {

    }

    @Override
    public void onMvpPause() {

    }

    @Override
    public void onMvpStop() {

    }

    @Override
    public void onMvpSaveInstanceState(Bundle savedInstanceState) {

    }

    private void _detach(boolean retainInstance) {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    @Override
    public void onMvpDetachView(boolean retainInstance) {
        _detach(retainInstance);
    }

    @Override
    public void onMvpDestroy() {

    }

    @Override
    public void onMvpViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onMvpDestroyView () {

    }

    @Override
    public void onMvpAttach (Context context) {

    }

    @Override
    public void onMvpDetach () {

    }

}
