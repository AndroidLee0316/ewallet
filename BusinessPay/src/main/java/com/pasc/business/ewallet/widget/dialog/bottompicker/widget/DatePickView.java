package com.pasc.business.ewallet.widget.dialog.bottompicker.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.pasc.business.ewallet.R;

import java.util.GregorianCalendar;

/**
 * @date 2019/7/12
 * @des
 * @modify
 **/
public class DatePickView extends FrameLayout {
    NumberPicker year, month;
    private int currentYear, currentMonth;
    private int defaultStartYear = 2000;
    private int defaultEndYear = 2030;

    public DatePickView(@NonNull Context context) {
        this (context, null);
    }

    public DatePickView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this (context, attrs, 0);
    }

    @SuppressLint("WrongConstant")
    public DatePickView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        GregorianCalendar now = new GregorianCalendar ();
        LayoutInflater.from (context).inflate (R.layout.ewallet_pay_date_pick_layout, this);
        year = findViewById (R.id.year);
        month = findViewById (R.id.month);
        year.setFormatter (mYearFormatter);
        month.setFormatter (mMonthFormatter);

//        month. setWrapSelectorWheel (false);
//        year. setWrapSelectorWheel (false);
//        year.setOnLongPressUpdateInterval (100);
        year.setOnValueChangedListener (new NumberPicker.OnValueChangeListener () {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                update (newVal);
                Log.e ("yzj", "onValueChange: oldVal: " + oldVal + " newVal: " + newVal);
            }
        });


//        month.setOnLongPressUpdateInterval (100);
        month.setOnValueChangedListener (new NumberPicker.OnValueChangeListener () {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentMonth = newVal;
            }
        });
        updateMonthStartEnd (1, 12);
        updateYearStartEnd (defaultStartYear, defaultEndYear);

        currentYear = now.get (GregorianCalendar.YEAR);
        currentMonth = now.get (GregorianCalendar.MONTH) + 1;

        year.setValue (currentYear);
        month.setValue (currentMonth);


    }

    void update(int newValYear){
        if (hasSetLimit ()) {
            if (newValYear == minYearLimit) {
                updateMonthStartEnd (minYearMinMonth, minYearMaxMonth);
                if (minYearLimit==maxYearLimit){
                    month.setValue (minYearMaxMonth);
                }else {
                    month.setValue (minYearMinMonth);
                }
            } else if (newValYear == maxYearLimit) {
                updateMonthStartEnd (maxYearMinMonth, maxYearMaxMonth);
                // 当前年份
                if (minYearLimit==maxYearLimit){
                    month.setValue (minYearMaxMonth);
                }
            }else {
                int max= month.getMaxValue ();
                int min= month.getMaxValue ();
                if (min!=1 || max!=12 ){
                    updateMonthStartEnd (1, 12);
                    month.setValue (1);
                }
            }
        }
        try {

        }catch (Exception e){
            e.printStackTrace ();
        }
        currentMonth=month.getValue ();
        currentYear = newValYear;
    }

    public void setCircle(boolean isCircle){
                month. setWrapSelectorWheel (isCircle);
        year. setWrapSelectorWheel (isCircle);
    }

    private int minYearLimit = -1, minYearMinMonth = -1, minYearMaxMonth = -1,
            maxYearLimit = -1, maxYearMinMonth = -1, maxYearMaxMonth = -1;

    private boolean hasSetLimit() {
        return minYearLimit != -1;
    }

    public void setYearMonthLimit(
            int minYearLimit, int minYearMinMonth, int minYearMaxMonth,
            int maxYearLimit, int maxYearMinMonth, int maxYearMaxMonth) {
        this.minYearLimit = minYearLimit;
        this.minYearMinMonth = minYearMinMonth;
        this.minYearMaxMonth = minYearMaxMonth;

        this.maxYearLimit = maxYearLimit;
        this.maxYearMinMonth = maxYearMinMonth;
        this.maxYearMaxMonth = maxYearMaxMonth;

    }


    public int getCurrentYear() {
        return currentYear;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public void updateMonthStartEnd(int minMonth, int endMonth) {
//        month.setMinValue (minMonth);
//        month.setMaxValue (endMonth);
        month.setMinMaxValue (minMonth,endMonth);
    }

    public void updateMonthValue(int month) {
        this.month.setValue (month);
    }

    public void updateYearValue(int year) {
        update (year);
        this.year.setValue (year);
    }

    public void updateYearStartEnd(int minYear, int endYear) {
//        year.setMinValue (minYear);
//        year.setMaxValue (endYear);
        year.setMinMaxValue (minYear,endYear);

    }


    NumberPicker.Formatter mYearFormatter = new NumberPicker.Formatter () {
        @Override
        public String format(int value) {
            return value + "年";
        }
    };
    NumberPicker.Formatter mMonthFormatter = new NumberPicker.Formatter () {
        @Override
        public String format(int value) {
            if (value <= 9) {
                return "0" + (value) + "月";
            }
            return value + "月";
        }
    };
}
