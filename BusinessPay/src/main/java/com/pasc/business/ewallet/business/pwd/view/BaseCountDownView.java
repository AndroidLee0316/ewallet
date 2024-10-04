package com.pasc.business.ewallet.business.pwd.view;

import com.pasc.business.ewallet.base.EwalletBaseView;

/**
 * @date 2019/7/25
 * @des
 * @modify
 **/
public interface BaseCountDownView extends EwalletBaseView {
    void showElapseTime(int count);
    void showElapseTimeUp();
}
