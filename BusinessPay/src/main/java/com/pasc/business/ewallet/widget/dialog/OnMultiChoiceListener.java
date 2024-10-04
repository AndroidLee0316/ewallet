package com.pasc.business.ewallet.widget.dialog;

import android.support.v4.app.DialogFragment;

import java.util.List;

/**
 * 多选监听.
 *
 * @param <T> DialogFragment泛型.
 * @author chenruihan410
 */
public abstract class OnMultiChoiceListener<T extends DialogFragment> extends BaseListener {

    /**
     * 选中了某一项.
     *
     * @param dialogFragment 对话框Fragment对象.
     * @param position       选中项的位置.
     */
    public abstract void onMultiChoice(T dialogFragment, List<Integer> position);
}
