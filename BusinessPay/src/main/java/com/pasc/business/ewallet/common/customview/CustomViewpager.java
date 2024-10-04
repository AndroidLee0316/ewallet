package com.pasc.business.ewallet.common.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/3/11
 */
public class CustomViewpager extends ViewPager{
    private static final int VIEW_PAGER_DURATION = 200;
    public CustomViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setSliderTransformDuration (this,VIEW_PAGER_DURATION);
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    /**
     * set the duration between two slider changes.
     *  设置viewpager动画切换时间
     * @param period
     */
    public void setSliderTransformDuration(ViewPager viewPager, int period) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller (viewPager.getContext(), new AccelerateDecelerateInterpolator (), period);
            mScroller.set(viewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * viewpager切换动画时间设置类
     */
    static class FixedSpeedScroller extends Scroller {

        private int mDuration = 1000;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, int period){
            this(context,interpolator);
            mDuration = period;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
