package com.pasc.business.ewallet.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * 控制器接口：
 * 定义P层生命周期与 V层同步
 */
public interface EwalletIPresenter<V extends EwalletBaseView> {

    void onMvpAttachView(V view, Bundle savedInstanceState);

    void onMvpStart();

    void onMvpResume();

    void onMvpPause();

    void onMvpStop();

    void onMvpSaveInstanceState(Bundle savedInstanceState);

    void onMvpDetachView(boolean retainInstance);

    void onMvpDestroy();

    //for fragment
    void onMvpViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState);

    void onMvpDestroyView ();

    void onMvpAttach (Context context);

    void onMvpDetach ();

}
