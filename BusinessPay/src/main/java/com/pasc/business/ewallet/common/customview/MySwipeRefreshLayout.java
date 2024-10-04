package com.pasc.business.ewallet.common.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

import com.pasc.business.ewallet.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author yangzijian
 * @date 2019/3/13
 * @des
 * @modify
 **/
public class MySwipeRefreshLayout extends SwipeRefreshLayout {
    public MySwipeRefreshLayout(@NonNull Context context) {
        this (context,null);
    }

    public MySwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super (context, attrs);
        int color=getResources ().getColor (R.color.ewallet_theme_color);
        setColorSchemeColors (color,color,color);
    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        try {
            Field mCircleView = SwipeRefreshLayout.class.getDeclaredField("mCircleView");
            mCircleView.setAccessible(true);
            View progress = (View) mCircleView.get(this);
            progress.setVisibility(VISIBLE);
            Method setRefreshing = SwipeRefreshLayout.class.getDeclaredMethod("setRefreshing", boolean.class, boolean.class);
            setRefreshing.setAccessible(true);
            setRefreshing.invoke(this, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
