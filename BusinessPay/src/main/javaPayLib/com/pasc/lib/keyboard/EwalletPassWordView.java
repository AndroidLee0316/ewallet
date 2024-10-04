package com.pasc.lib.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author yangzijian
 * @date 2019/3/4
 * @des
 * @modify
 **/
public class EwalletPassWordView extends View {
    Context context;
    private Paint bordPaint; //外框画笔
    private Paint linePaint; //线 的画笔
    private Paint passTextPaint; //密码画笔
    private float width;
    private float height;

    private float lineWidth = 1.5f;
    private int currentLength = 0;
    private int totalLength = 6;
    RectF rectContent = new RectF (0f, 0f, 1, 1);

    public EwalletPassWordView(Context context) {
        this (context, null);
    }

    public EwalletPassWordView(Context context, @Nullable AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public EwalletPassWordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        this.context = context;
        initPaint ();
        // 不设置的默认
        int heightDp = 50;
        height = dip2px (getContext (), heightDp) + lineWidth * 2;
        width = dip2px (getContext (), heightDp * totalLength) + lineWidth * (totalLength + 1);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure (widthMeasureSpec, heightMeasureSpec);
        int tmpW = getMeasuredWidth ();
        int tmpH = getMeasuredHeight ();
        if (tmpW > 0) {
            width = tmpW;
        }
        if (tmpH > 0) {
            height = tmpH;
        }

        /****保持正方形****/
        float suggestW = (height - 2 * lineWidth) * totalLength + (totalLength + 1) * lineWidth;
        /*** 高度太高，用宽度为基准 ****/
        if (suggestW > width) {
            height = (width - (totalLength + 1) * lineWidth) / totalLength;
        } else {
            /*****宽度太宽，用高度为基准***/
            width = suggestW;
        }

        setMeasuredDimension ((int) width, (int) height);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw (canvas);
        canvas.drawColor (Color.WHITE);
        drawRoundRect (canvas);
        drawLine (canvas);
        drawTextPass (canvas);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        setFocusable (true);
        bordPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
        bordPaint.setStrokeWidth (lineWidth);
        bordPaint.setColor (Color.parseColor ("#999999"));
        bordPaint.setStyle (Paint.Style.STROKE);

        linePaint = new Paint (Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor (Color.parseColor ("#999999"));
        linePaint.setStrokeWidth (lineWidth);

        passTextPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
        passTextPaint.setColor (Color.parseColor ("#000000"));
        passTextPaint.setStrokeWidth (12);

    }

    /**
     * 绘制密码
     *
     * @param canvas
     */
    private void drawTextPass(Canvas canvas) {
        float cx, cy = height / 2;
        float half = width / totalLength / 2;
        for (int i = 0; i < currentLength; i++) {
            cx = width * i / totalLength + half;
            int radius = 15;
            canvas.drawCircle (cx, cy, radius, passTextPaint);
        }
    }

    /**
     * 绘制线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        for (int i = 1; i < totalLength; i++) {
            float x = width * i / totalLength;
            canvas.drawLine (x, lineWidth, x, height - lineWidth, linePaint);
        }
    }

    /**
     * 绘制背景
     *
     * @param canvas
     */
    private void drawRoundRect(Canvas canvas) {
        rectContent.left = lineWidth;
        rectContent.top = lineWidth;
        rectContent.right = width - lineWidth;
        rectContent.bottom = height - lineWidth;
        canvas.drawRoundRect (rectContent, 0, 0, bordPaint);
    }


    public void refresh(int currentIndex, int totalLength) {
        this.currentLength = currentIndex;
        this.totalLength = totalLength;
        invalidate ();
    }

}
