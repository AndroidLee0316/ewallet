package com.pasc.business.ewallet.business.traderecord.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.util.DateUtil;
import com.pasc.business.ewallet.common.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019/7/8
 * @des
 * @modify
 **/
public class MonthBillView extends View {
    private List<BillItem> billItems = new ArrayList<> ();
    private float paddingLR = 30f;
    private float textHeight = 40f;
    private float minHeight = 10f;
    private float maxPercent = 1f;
    private int lineDashNum = 4;
    private float dashH;
    private float lineWidth = 2;
    private float textSize = 24;
    private int bgColor = Color.parseColor ("#FFFFFF");
    private int dashColor = Color.parseColor ("#E8E8E8");
    private int defaultRectColor = Color.parseColor ("#90E3EB");
    private int lastRectColor = Color.parseColor ("#22C8D8");
    private int defaultDateColor = Color.parseColor ("#999999");
    private int lastDateColor = Color.parseColor ("#333333");
    // 数量不足的情况下 ，是否优先显示在左边 ，否则就是在右边
    private boolean showLeftPriority = true;
    private int defaultSize = 6; // 建议6个的
    private int height;
    private int width;
    private float itemWidth;
    private RectF drawRect = new RectF ();

    public void update(int year, int month, List<BillItem> items) {
        billItems.clear ();
        List<String> strings = DateUtil.getNextMonths (year, month, -(defaultSize - 1), "yyyy-MM");
        for (int i = strings.size () - 1; i >= 0; i--) {
            billItems.add (new MonthBillView.BillItem (strings.get (i), 0));
        }
        for (MonthBillView.BillItem billItem : billItems) {
            for (MonthBillView.BillItem tmp : items) {
                if (billItem.equals (tmp)) {
                    billItem.update (tmp);
                }
            }
        }
        invalidate ();
    }

    {
//        billItems.add (new BillItem ("2018-01", 0));
//        billItems.add (new BillItem ("2018-02", 1.2));
//        billItems.add (new BillItem ("2019-03", 2500));
//        billItems.add (new BillItem ("2019-04", 4000));
//        billItems.add (new BillItem ("2019-05", 8000));
//        billItems.add (new BillItem ("2019-06", 3500));

//        billItems.add (new BillItem ("2018-01", 0));
//        billItems.add (new BillItem ("2018-02", 1.2));
//        billItems.add (new BillItem ("2019-03", 3));
//        billItems.add (new BillItem ("2019-04", 4));
//        billItems.add (new BillItem ("2019-05", 9));
//        billItems.add (new BillItem ("2019-06", 8));

    }

    public void updateError(int year, int month) {
        this.billItems.clear ();
        List<String> strings = DateUtil.getNextMonths (year, month, -(defaultSize - 1), "yyyy-MM");
        for (int i = strings.size () - 1; i >= 0; i--) {
            billItems.add (new MonthBillView.BillItem (strings.get (i), 0));
        }
        invalidate ();
    }


    private Paint paint,boldPaint;
    private Paint dashPaint;
    private Paint bottomPaint;

    public MonthBillView(Context context) {
        this (context, null);
    }

    public MonthBillView(Context context, @Nullable AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public MonthBillView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        //虚线需要开启硬件加速
        setLayerType (View.LAYER_TYPE_SOFTWARE, null);

        textSize = sp2px (context, 10);
        textHeight = dp2px (context, 20);
        paint = new Paint (Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize (textSize);
        paint.setStrokeWidth (lineWidth);

        boldPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
        boldPaint.setTextSize (textSize);
        boldPaint.setStrokeWidth (lineWidth);
        boldPaint.setTypeface(Typeface.DEFAULT_BOLD);

        dashPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
        dashPaint.setStrokeWidth (lineWidth);
        dashPaint.setColor (dashColor);
        dashPaint.setStyle (Paint.Style.STROKE);
        dashPaint.setPathEffect (new DashPathEffect (new float[]{8, 8}, 0));

        bottomPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
        bottomPaint.setStrokeWidth (lineWidth);
        bottomPaint.setColor (dashColor);

        defaultRectColor=context.getResources().getColor (R.color.ewallet_dark_theme_color);
        lastRectColor=context.getResources().getColor (R.color.ewallet_theme_color);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        height = h;
        width = w;
        //减去地下
        dashH = (height - 2 * textHeight) / lineDashNum;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getVisibility () == GONE) {
            return;
        }
        if (width == 0 || height == 0) {
            return;
        }
        int size = billItems.size ();
        if (size == 0) {
            return;
        }
        //bg
        drawRect.left = 0;
        drawRect.right = width;
        drawRect.top = 0;
        drawRect.bottom = height;
        paint.setColor (bgColor);
        canvas.drawRect (drawRect, paint);


        if (size > defaultSize) {
            itemWidth = (width - paddingLR * 2) / (size * 2 + 1);
        } else {
            itemWidth = (width - paddingLR * 2) / (defaultSize * 2 + 1);
        }
        //画虚线 0 - (height - textHeight)
        for (int i = 0; i < lineDashNum; i++) {
            float startX = itemWidth + paddingLR;
            float startY = i * dashH + lineWidth;
            float stopX = width - itemWidth - paddingLR;
            float stopY = startY;
            canvas.drawLine (startX, startY, stopX, stopY, dashPaint);
        }
        updatePercent ();
        canvas.drawLine (itemWidth + paddingLR, height - 2 * textHeight + lineWidth / 2, width - itemWidth - paddingLR, height - 2 * textHeight + lineWidth / 2, bottomPaint);
        for (int i = 0; i < size; i++) {
            drawBill (canvas, billItems.get (i), i, size);
        }


    }


    private void drawBill(Canvas canvas, BillItem billItem, int index, int size) {
        boolean isLast = (size - index == 1);
        float left = 0;
        float right = 0;
        float top = (float) (height - 2*textHeight-   (billItem.getPercent ()  * (height - 3* textHeight)));
        float bottom = height - 2 * textHeight;

        if (bottom-top<=minHeight ){
            top=bottom-minHeight;
        }

        if (showLeftPriority) {
            left = (2 * index + 1) * itemWidth + paddingLR;
            right = left + itemWidth;
        } else {
            left = width - (2 * (size - index)) * itemWidth - paddingLR;
            right = left + itemWidth;
        }
        //计算baseline
        Paint.FontMetrics fontMetrics = boldPaint.getFontMetrics ();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;

        float baseline  = top - textHeight / 2 + distance;
//        if (billItem.getPercent () == 0) {
//            baseline = height - 2 * textHeight - textHeight / 2 + distance - minHeight;
//        } else {
//            baseline = top - textHeight / 2 + distance - 1.5f * minHeight;
//        }
        String money = billItem.getMoneyText ();
        float moneyW = boldPaint.measureText (money);
        // 中心点位置 - 文本宽度/2
        float textX = left + (right - left) / 2 - moneyW / 2;
        // 画钱
        if (isLast) {
            boldPaint.setColor (lastRectColor);
            paint.setColor (lastRectColor);

        } else {
            boldPaint.setColor (defaultRectColor);
            paint.setColor (defaultRectColor);

        }

        canvas.drawText (money, textX, baseline, boldPaint);
        //画圆柱
        drawRect.left = left;
        drawRect.right = right;
//        if (billItem.getPercent () == 0) {
//            drawRect.top = height - 2 * textHeight - minHeight;
//        } else {
//            drawRect.top = top - 1.5f * minHeight;
//        }
        drawRect.top = top;
        drawRect.bottom = bottom;
//        canvas.drawRect (drawRect, paint);
        canvas.drawRoundRect (drawRect,5,5,paint);

        String month = billItem.getMonth ();
        //画月
        if (!isEmpty (month)) {
            float dateW = paint.measureText (month);
            // 中心点位置 - 文本宽度/2
            textX = left + (right - left) / 2 - dateW / 2;
            baseline = bottom + textHeight / 2 + distance;
            if (isLast) {
                paint.setColor (lastDateColor);
            } else {
                paint.setColor (defaultDateColor);
            }
            canvas.drawText (month, textX, baseline, paint);
        }
        if (billItem.showYear) {
            String year = billItem.getYear ();
            if (!isEmpty (year)) {
                float dateW = paint.measureText (year);
                // 中心点位置 - 文本宽度/2
                textX = left + (right - left) / 2 - dateW / 2;
                baseline = bottom + textHeight + textHeight / 2 + distance;
                if (isLast) {
                    paint.setColor (lastDateColor);
                } else {
                    paint.setColor (defaultDateColor);
                }
                canvas.drawText (year, textX, baseline, paint);
            }
        }

        if (billItem.showDivider) {
            float xx = left - itemWidth / 2;
            canvas.drawLine (xx, bottom, xx, height, bottomPaint);
        }

    }

    private void updatePercent() {
        double maxMoney = 0f;

        for (BillItem billItem : billItems) {
            billItem.reset ();
            maxMoney = billItem.money > maxMoney ? billItem.money : maxMoney;
        }

        for (BillItem billItem : billItems) {
            if (maxMoney == 0) {
                billItem.setPercent (0);
            } else {
                billItem.setPercent (billItem.money / maxMoney * maxPercent);
            }
        }
        //跨年 最多两年
        BillItem pre = null;
        boolean hasKuaNian=false;
        for (BillItem billItem : billItems) {
            if (pre == null) {
                pre = billItem;
            } else {
                if (pre.year == billItem.year) {
                    pre=billItem;
                } else {
                    hasKuaNian=true;
                    pre.showYear=true;
                    billItem.showYear=true;
                    billItem.showDivider=true;
                    break;
                }
            }
        }

        if (!hasKuaNian && billItems.size ()>0){
            BillItem billItem=billItems.get (0);
            billItem.showYear=true;
            billItem.showDivider=false;
        }


    }

    private boolean isEmpty(String text) {
        return text == null || text.length () == 9;
    }

    public static class BillItem {
        public String date;
        public int year;
        public int month;
        public double money;
        private double percent;
        public boolean showYear = false;
        public boolean showDivider = false;

        public void reset() {
            showYear = false;
            showDivider = false;
        }

        public String getYear() {
            return year+"年";
        }


        public void update(BillItem billItem) {
            this.money = billItem.money;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof BillItem) {
                BillItem billItem = (BillItem) obj;
                return (billItem.year == this.year) && (billItem.month == this.month);
            }
            return super.equals (obj);
        }

        public BillItem(String date, double money) {
            int arr[] = DateUtil.getYearMonth (date);
            year = arr[0];
            month = arr[1];
            this.date = date;
            this.money = money;
        }

        public BillItem(int year, int month, float money, float percent) {
            this.year = year;
            this.month = month;
            this.money = money;
            this.percent = percent;
            this.date = year + "-" + month;

        }

        public String getMonth() {
            return month > 9 ? (month + "月") : ("0" + month +"月");
        }


        public String getMoneyText() {
            return "¥" + Util.stringPoint (money, 2);
        }

        public double getPercent() {
            return percent;
        }

        public void setPercent(double percent) {
            this.percent = percent;
        }
    }

    private int sp2px(Context context, float spValue) {
        float fontScale = context.getResources ().getDisplayMetrics ().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
