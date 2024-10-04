package com.pasc.business.ewallet.base;

import android.support.v4.app.FragmentActivity;
import com.pasc.business.ewallet.NotProguard;

/**
 * @date 2019/7/13
 * @des
 * @modify
 **/
@NotProguard
public interface CommonBaseView extends EwalletBaseView {
    void showLoading(String msg);
    void dismissLoading();

    FragmentActivity getActivity();

    void showErrorTip(String msg);

}
