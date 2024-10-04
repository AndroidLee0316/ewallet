package com.pasc.business.ewallet.common.customview;

import android.content.Context;

import android.graphics.Canvas;

import android.graphics.drawable.Drawable;

import android.util.AttributeSet;

import android.widget.TextView;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/6/4
 */

public class DrawableTextView extends android.support.v7.widget.AppCompatTextView {

    public DrawableTextView(Context context) {

        this(context, null);

    }

    public DrawableTextView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);

    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[0];
            if (drawableLeft != null) {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            }
        }
        super.onDraw(canvas);
    }

}
