package com.pasc.business.ewallet.business.util;

import android.annotation.SuppressLint;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class FragmentUtils {
    @SuppressLint({"CommitTransaction", "ResourceType"})
    public static void showTargetFragment(android.app.FragmentManager manager, android.app.Fragment curFragment, android.app.Fragment targetFragment, @IdRes int resId) {
        if (manager == null || targetFragment == null || resId < 0) {
            return;
        }
        if (curFragment != null) {
            if (targetFragment.isAdded()) {
                commit(manager.beginTransaction().show(targetFragment).hide(curFragment));
            } else {
                commit(manager.beginTransaction().add(resId, targetFragment).hide(curFragment));
            }
        } else {
            if (targetFragment.isAdded()) {
                commit(manager.beginTransaction().show(targetFragment));
            } else {
                commit(manager.beginTransaction().add(resId, targetFragment));
            }
        }
    }

    @SuppressLint({"CommitTransaction", "ResourceType"})
    public static void showTargetFragment(FragmentManager manager, Fragment curFragment, Fragment targetFragment, @IdRes int resId) {
        if (manager == null || targetFragment == null || resId < 0) {
            return;
        }
        if (curFragment != null && curFragment != targetFragment) {
            if (targetFragment.isAdded()) {
                commit(manager.beginTransaction().show(targetFragment).hide(curFragment));
            } else {
                commit(manager.beginTransaction().add(resId, targetFragment).hide(curFragment));
            }
        } else {
            if (targetFragment.isAdded()) {
                commit(manager.beginTransaction().show(targetFragment));
            } else {
                commit(manager.beginTransaction().add(resId, targetFragment));
            }
        }
    }

    /**
     * 这种方式与showTargetFragment的区别是这种方式显示targetFragment的同时从宿主Activity中移除当前Fragment
     */
    @SuppressLint({"CommitTransaction", "ResourceType"})
    public static void popTargetFragment(FragmentManager manager, Fragment curFragment, Fragment targetFragment, @IdRes int resId) {
        if (manager == null || targetFragment == null || resId < 0) {
            return;
        }
        if (curFragment != null && curFragment != targetFragment) {
            if (targetFragment.isAdded()) {
                commit(manager.beginTransaction().show(targetFragment).remove(curFragment));
            } else {
                commit(manager.beginTransaction().add(resId, targetFragment).remove(curFragment));
            }
        } else {
            if (targetFragment.isAdded()) {
                commit(manager.beginTransaction().show(targetFragment));
            } else {
                commit(manager.beginTransaction().add(resId, targetFragment));
            }
        }
    }

    @SuppressLint({"CommitTransaction", "ResourceType"})
    public static void showTargetFragment(FragmentManager manager, Fragment targetFragment, @IdRes int resId) {
        if (manager == null || targetFragment == null || resId < 0) {
            return;
        }
        if (targetFragment.isAdded()) {
            commit(manager.beginTransaction().show(targetFragment));
        } else {
            commit(manager.beginTransaction().add(resId, targetFragment));
        }
    }

    private static void commit(FragmentTransaction ft) {
        ft.commitAllowingStateLoss();
    }

    private static void commit(android.app.FragmentTransaction ft) {
        ft.commitAllowingStateLoss();
    }

    public static void showTargetFragment(FragmentActivity fragmentActivity, Fragment curFragment, Fragment targetFragment, @IdRes int resId) {
        if (fragmentActivity == null) {
            return;
        }
        showTargetFragment(fragmentActivity.getSupportFragmentManager(), curFragment, targetFragment, resId);
    }


    private final static class Single{
        private final static  FragmentUtils instance=new FragmentUtils ();
    }
    public static FragmentUtils getInstance() {
        return Single.instance;
    }



}
