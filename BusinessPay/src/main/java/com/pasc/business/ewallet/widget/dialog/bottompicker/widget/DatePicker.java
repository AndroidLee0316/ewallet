package com.pasc.business.ewallet.widget.dialog.bottompicker.widget;
/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.widget.dialog.bottompicker.utils.ChineseCalendar;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DatePicker extends FrameLayout {

    private static final String LOG_TAG = DatePicker.class.getSimpleName ();

    private static final String DATE_FORMAT = "MM/dd/yyyy";

    private static final boolean DEFAULT_ENABLED_STATE = true;

    // tws-start lunar calendar::2014-10-9
    private final NumberPicker mLunarSpinner;
    private boolean mIsLunar = false;
    private boolean isList = false;
    private String[] mChineseDateNames = null;
    private String mYearName = "";
    private String mMonthName = "";
    private String mDayName = "";
    private LinearLayout datePickerRoot;
    // tws-end lunar calendar::2014-10-9

    private final NumberPicker mPickerListSpinner;

    private final NumberPicker mDaySpinner;

    private final NumberPicker mMonthSpinner;

    private final NumberPicker mYearSpinner;

    private final EditText mDaySpinnerInput;

    private final EditText mMonthSpinnerInput;

    private final EditText mYearSpinnerInput;

    private Locale mCurrentLocale;

    private OnDateChangedListener mOnDateChangedListener;

    private final java.text.DateFormat mDateFormat = new SimpleDateFormat (DATE_FORMAT);

    private Calendar mCalendar;

    private ChineseCalendar mMinDate;

    private ChineseCalendar mMaxDate;

    private ChineseCalendar mCurrentDate;

    private boolean mIsEnabled = DEFAULT_ENABLED_STATE;
    private Context mContext;

    // default is false
    private boolean mUnitShown = false;
    private boolean mLunarShown = false;

    int startYear;
    int endYear;
    int startMonth=0;
    int endMonth=11;

    public void setCurrent(int currentYear, int currentMonth, int currentDay) {
        if (currentYear != 0) {

        } else {
            currentYear=mCurrentDate.get (Calendar.YEAR);
        }

        if (currentMonth >-1) {

        } else {
            currentMonth=mCurrentDate.get (Calendar.MONTH);
        }

        if (currentDay != 0) {
        } else {
            currentDay=mCurrentDate.get (Calendar.DAY_OF_MONTH);
        }
        mMonthSpinner.setValue (currentMonth);
        mDaySpinner.setValue (currentDay);
        mYearSpinner.setValue (currentYear);
    }

    /**
     * The callback used to indicate the user changes\d the date.
     */
    public interface OnDateChangedListener {

        /**
         * Called upon a date change.
         *
         * @param view        The view associated with this listener.
         * @param year        The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility with
         *                    {@link Calendar}.
         * @param dayOfMonth  The day of the month that was set.
         */
        void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth);

        void onItemChanged(int position);
    }

    public DatePicker(Context context) {
        this (context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);
        mContext = context;
        mChineseDateNames = context.getResources ().getStringArray (R.array.lunar_day_names);
        String[] lunarCalendars = context.getResources ().getStringArray (R.array.tws_calendar_type);
        mYearName = getContext ().getString (R.string.calendar_year);
        mMonthName = getContext ().getString (R.string.calendar_month);
        mDayName = getContext ().getString (R.string.calendar_day);

        // initialization based on locale
        setCurrentLocale (Locale.getDefault ());

        TypedArray attributesArray = context.obtainStyledAttributes (attrs, R.styleable.EwalletDatePicker, defStyle, 0);
        mUnitShown = attributesArray.getBoolean (R.styleable.EwalletDatePicker_ewallet_unitShown, false);
        mLunarShown = attributesArray.getBoolean (R.styleable.EwalletDatePicker_ewallet_lunarShown, false);

        int DEFAULT_START_YEAR = 1970;
        startYear = attributesArray.getInt (R.styleable.EwalletDatePicker_ewallet_startYear, DEFAULT_START_YEAR);
        int DEFAULT_END_YEAR = 2036;
        endYear = attributesArray.getInt (R.styleable.EwalletDatePicker_ewallet_endYear, DEFAULT_END_YEAR);
        String minDate = attributesArray.getString (R.styleable.EwalletDatePicker_ewallet_minDate);
        String maxDate = attributesArray.getString (R.styleable.EwalletDatePicker_ewallet_maxDate);
        int layoutResourceId = attributesArray.getResourceId (R.styleable.EwalletDatePicker_ewallet_layout, R.layout.ewallet_widget_date_picker);
        attributesArray.recycle ();
        // Log.d(LOG_TAG,"layoutResourceId = "+layoutResourceId);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View dataPickerView = inflater.inflate (layoutResourceId, this, true);
        datePickerRoot = dataPickerView.findViewById (R.id.line_picker);


        NumberPicker.OnValueChangeListener onChangeListener = new NumberPicker.OnValueChangeListener () {
            @SuppressLint("WrongConstant")
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateInputState ();
                mCalendar.setTimeInMillis (mCurrentDate.getTimeInMillis ());
                // take care of wrapping of days and months to update greater
                // fields
                if (picker == mLunarSpinner) {
                    if (newVal == ChineseCalendar.CALENDAR_TYPE_LUNAR) {
                        setIsLunar (true);
                    } else {
                        setIsLunar (false);
                    }
                }

//                if (mIsLunar) {
//                    if (picker == mDaySpinner) {
//                        mCurrentDate.add(ChineseCalendar.CHINESE_DATE, newVal - oldVal);
//                    } else if (picker == mMonthSpinner) {
//                        mCurrentDate.add(ChineseCalendar.CHINESE_MONTH, newVal - oldVal);
//                    } else if (picker == mYearSpinner) {
//                        mCurrentDate.set(ChineseCalendar.CHINESE_YEAR, newVal);
//                    }
//                }

//                else {
                if (picker == mDaySpinner) {
                    mCalendar.add (Calendar.DAY_OF_MONTH, newVal - oldVal);
                } else if (picker == mMonthSpinner) {
                    mCalendar.add (Calendar.MONTH, newVal - oldVal);
                } else if (picker == mYearSpinner) {
                    mCalendar.set (Calendar.YEAR, newVal);
                }
                // now set the date to the adjusted one
                setDate (mCalendar.get (Calendar.YEAR), mCalendar.get (Calendar.MONTH),
                        mCalendar.get (Calendar.DAY_OF_MONTH));
//                }

                updateSpinners ();
                updateCalendarView ();
                notifyDateChanged ();
            }
        };

        // tws-start lunar calendar::2014-10-9
        mLunarSpinner = findViewById (R.id.lunar);
        mLunarSpinner.setTextAlignType (NumberPicker.ALIGN_CENTER_TYPE);
        mLunarSpinner.setOnLongPressUpdateInterval (100);
        mLunarSpinner.setOnValueChangedListener (onChangeListener);
        mLunarSpinner.setMinValue (0);
        mLunarSpinner.setMaxValue (1);
        mLunarSpinner.setValue (ChineseCalendar.CALENDAR_TYPE_GREGORIAN);
        mLunarSpinner.setDisplayedValues (lunarCalendars);
        mLunarSpinner.setSlowScroller (true);
        // tws-end lunar calendar::2014-10-9

        // day
        mDaySpinner = findViewById (R.id.day);
//        mDaySpinner.setTextAlignType(NumberPicker.ALIGN_LEFT_TYPE);
        mDaySpinner.setOnLongPressUpdateInterval (100);
        mDaySpinner.setOnValueChangedListener (onChangeListener);
        mDaySpinnerInput = mDaySpinner.findViewById (R.id.numberpicker_input);

        // month
        mMonthSpinner = findViewById (R.id.month);
        mMonthSpinner.setOnLongPressUpdateInterval (100);
        mMonthSpinner.setOnValueChangedListener (onChangeListener);
        mMonthSpinnerInput = mMonthSpinner.findViewById (R.id.numberpicker_input);

        // year
        mYearSpinner = findViewById (R.id.year);
//        mYearSpinner.setTextAlignType(NumberPicker.ALIGN_RIGHT_TYPE);
        mYearSpinner.setOnLongPressUpdateInterval (100);
        mYearSpinner.setOnValueChangedListener (onChangeListener);
        mYearSpinnerInput = mYearSpinner.findViewById (R.id.numberpicker_input);
        mYearSpinner.setFormatter (mYearFormatter);
        mMonthSpinner.setFormatter (mMonthFormatter);
        mDaySpinner.setFormatter (mDayFormatter);

        mPickerListSpinner = findViewById (R.id.picker_list);
        mPickerListSpinner.setOnLongPressUpdateInterval (100);
        mPickerListSpinner.setOnValueChangedListener (onChangeListener);
        EditText pickerListInput = mPickerListSpinner.findViewById (R.id.numberpicker_input);
//        if (mUnitShown) {
//            mYearSpinner.setFormatter(mYearFormatter);
//            mMonthSpinner.setFormatter(mMonthFormatter);
//            mDaySpinner.setFormatter(mDayFormatter);
//        } else {
//            mMonthSpinner.setFormatter(mMonthNoUnitFormatter);
//            mDaySpinner.setFormatter(mDayNoUnitFormatter);
//        }

        initData (startYear, endYear,startMonth,endMonth, minDate, maxDate);

        // set content descriptions
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) context.getSystemService (Context.ACCESSIBILITY_SERVICE);

        assert accessibilityManager != null;
        if (accessibilityManager.isEnabled ()) {
            setContentDescriptions ();
        }
    }

    private void initData(int startYear, int endYear,int startMonth,int endMonth, String minDate, String maxDate) {
        // set the min date giving priority of the minDate over startYear
        mCalendar.clear ();
        if (!TextUtils.isEmpty (minDate)) {
            if (!parseDate (minDate, mCalendar)) {
                mCalendar.set (startYear, 0, 1);
            }
        } else {
            mCalendar.set (startYear, 0, 1);
        }
        setMinDate (mCalendar.getTimeInMillis ());

        // set the max date giving priority of the maxDate over endYear
        mCalendar.clear ();
        if (!TextUtils.isEmpty (maxDate)) {
            if (!parseDate (maxDate, mCalendar)) {
                mCalendar.set (endYear, 11, 31);
            }
        } else {
            mCalendar.set (endYear, 11, 31);
        }
        setMaxDate (mCalendar.getTimeInMillis ());

        // initialize to current date
        mCurrentDate.setTimeInMillis (System.currentTimeMillis ());
        init (mCurrentDate.get (Calendar.YEAR), mCurrentDate.get (Calendar.MONTH),
                mCurrentDate.get (Calendar.DAY_OF_MONTH), null);
    }

    public void showList(String[] displayedValues, int CurrentPostion, boolean isCircling) {
        isList = true;
        mYearSpinner.setVisibility (View.GONE);
        mMonthSpinner.setVisibility (View.GONE);
        mDaySpinner.setVisibility (View.GONE);
        mPickerListSpinner.setVisibility (View.VISIBLE);
        mPickerListSpinner.setDisplayedValues (displayedValues);
        mPickerListSpinner.setMinValue (0);
        mPickerListSpinner.setMaxValue (displayedValues.length - 1);
        mPickerListSpinner.setValue (CurrentPostion);
        if (isCircling) {
            mPickerListSpinner.setWrapSelectorWheel (true);
        } else {
            mPickerListSpinner.setWrapSelectorWheel (false);
        }

        datePickerRoot.setWeightSum (1);
        invalidate ();
    }

    public void showYearMonthDay(boolean isCircling) {
        mYearSpinner.setVisibility (View.VISIBLE);
        mMonthSpinner.setVisibility (View.VISIBLE);
        mDaySpinner.setVisibility (View.VISIBLE);
        mPickerListSpinner.setVisibility (View.GONE);
        datePickerRoot.setWeightSum (3);
        if (isCircling) {
            mYearSpinner.setWrapSelectorWheel (true);
            mMonthSpinner.setWrapSelectorWheel (true);
            mDaySpinner.setWrapSelectorWheel (true);

        } else {
            mYearSpinner.setWrapSelectorWheel (false);
            mMonthSpinner.setWrapSelectorWheel (false);
            mDaySpinner.setWrapSelectorWheel (false);
        }
        invalidate ();

    }

    public void setStartEndYear(int mStartYear,int mEndYear) {
        startYear = mStartYear;
        endYear = mEndYear;
        initData (startYear, endYear, startMonth,endMonth,null, null);

    }


    public void setStartEndMonth(int startMonth,int endMonth) {
        this.startMonth = startMonth;
        this.endMonth=endMonth;
//        initData (startYear, endYear, null, null);
        initData (startYear, endYear, startMonth,endMonth,null, null);

    }


    public void showYearMonth(boolean isCircling) {
        mYearSpinner.setVisibility (View.VISIBLE);
        mMonthSpinner.setVisibility (View.VISIBLE);
        mDaySpinner.setVisibility (View.GONE);
        mPickerListSpinner.setVisibility (View.GONE);
        datePickerRoot.setWeightSum (2);
        if (isCircling) {
            mYearSpinner.setWrapSelectorWheel (true);
            mMonthSpinner.setWrapSelectorWheel (true);

        } else {
            mYearSpinner.setWrapSelectorWheel (false);
            mMonthSpinner.setWrapSelectorWheel (false);
        }
        invalidate ();

    }

    public void showMonthDay(boolean isCircling) {
        mYearSpinner.setVisibility (View.GONE);
        mMonthSpinner.setVisibility (View.VISIBLE);
        mDaySpinner.setVisibility (View.VISIBLE);
        mPickerListSpinner.setVisibility (View.GONE);
        datePickerRoot.setWeightSum (2);
        if (isCircling) {
            mDaySpinner.setWrapSelectorWheel (true);
            mMonthSpinner.setWrapSelectorWheel (true);

        } else {
            mDaySpinner.setWrapSelectorWheel (false);
            mMonthSpinner.setWrapSelectorWheel (false);
        }
        invalidate ();
    }

    public void setLunarShown(boolean lunarShown) {
        if (mLunarSpinner == null || lunarShown == mLunarShown) {
            return;
        }

        mLunarShown = lunarShown;
        mLunarSpinner.setEnabled (mLunarShown);
        if (mLunarShown) {
            mLunarSpinner.setVisibility (View.VISIBLE);
        } else {
            mLunarSpinner.setVisibility (View.GONE);
        }

        invalidate ();
    }

    public void setUnitShown(boolean unitShown) {
        if (mUnitShown == unitShown) {
            return;
        }

        mUnitShown = unitShown;
        if (mUnitShown) {
            mYearSpinner.setFormatter (mYearFormatter);

            if (mIsLunar) {
                mDaySpinner.setFormatter (mLunarDayFormatter);
                mMonthSpinner.setFormatter (null);
            } else {
                mMonthSpinner.setFormatter (mMonthFormatter);
                mDaySpinner.setFormatter (mDayFormatter);
            }
        } else {
            mYearSpinner.setFormatter (null);
            mMonthSpinner.setFormatter (mMonthNoUnitFormatter);
            mDaySpinner.setFormatter (mDayNoUnitFormatter);
        }

        mYearSpinner.invalidate ();
        mMonthSpinner.invalidate ();
        mDaySpinner.invalidate ();
    }

    /**
     * Gets the minimal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     * <p>
     * Note: The default minimal date is 01/01/1900.
     * <p>
     *
     * @return The minimal supported date.
     */
    public long getMinDate() {
        return mMinDate.getTimeInMillis ();
        // return mCalendarView.getMinDate();
    }

    /**
     * Sets the minimal date supported by this {@link NumberPicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param minDate The minimal supported date.
     */
    public void setMinDate(long minDate) {
        mCalendar.setTimeInMillis (minDate);
        if (mCalendar.get (Calendar.YEAR) == mMinDate.get (Calendar.YEAR)
                && mCalendar.get (Calendar.DAY_OF_YEAR) != mMinDate.get (Calendar.DAY_OF_YEAR)) {
            return;
        }
        mMinDate.setTimeInMillis (minDate);
        // mCalendarView.setMinDate(minDate);
        if (mCurrentDate.before (mMinDate)) {
            mCurrentDate.setTimeInMillis (mMinDate.getTimeInMillis ());
            updateCalendarView ();
        }
        updateSpinners ();
    }

    /**
     * Gets the maximal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     * <p>
     * Note: The default maximal date is 12/31/2100.
     * <p>
     *
     * @return The maximal supported date.
     */
    public long getMaxDate() {
        return mMaxDate.getTimeInMillis ();
        // return mCalendarView.getMaxDate();
    }

    /**
     * Sets the maximal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param maxDate The maximal supported date.
     */
    public void setMaxDate(long maxDate) {
        mCalendar.setTimeInMillis (maxDate);
        if (mCalendar.get (Calendar.YEAR) == mMaxDate.get (Calendar.YEAR)
                && mCalendar.get (Calendar.DAY_OF_YEAR) != mMaxDate.get (Calendar.DAY_OF_YEAR)) {
            return;
        }
        mMaxDate.setTimeInMillis (maxDate);
        // mCalendarView.setMaxDate(maxDate);
        if (mCurrentDate.after (mMaxDate)) {
            mCurrentDate.setTimeInMillis (mMaxDate.getTimeInMillis ());
            updateCalendarView ();
        }
        updateSpinners ();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (mIsEnabled == enabled) {
            return;
        }
        super.setEnabled (enabled);
        mDaySpinner.setEnabled (enabled);
        mMonthSpinner.setEnabled (enabled);
        mYearSpinner.setEnabled (enabled);
        // mCalendarView.setEnabled(enabled);
        mIsEnabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return mIsEnabled;
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        onPopulateAccessibilityEvent (event);
        return true;
    }

    @Override
    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.onPopulateAccessibilityEvent (event);

        final int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR;
        String selectedDateUtterance = DateUtils.formatDateTime (mContext, mCurrentDate.getTimeInMillis (), flags);
        event.getText ().add (selectedDateUtterance);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged (newConfig);
        setCurrentLocale (newConfig.locale);
    }

    /**
     * Gets whether the {@link CalendarView} is shown.
     *
     * @return True if the calendar view is shown.
     * @see #getCalendarView()
     */
    public boolean getCalendarViewShown() {
        return false;
        // return mCalendarView.isShown();
    }

    /**
     * Gets the {@link CalendarView}.
     *
     * @return The calendar view.
     * @see #getCalendarViewShown()
     */
    public CalendarView getCalendarView() {
        return null;
        // return mCalendarView;
    }

    /**
     * Sets whether the {@link CalendarView} is shown.
     *
     * @param shown True if the calendar view is to be shown.
     */
    public void setCalendarViewShown(boolean shown) {
        // mCalendarView.setVisibility(shown ? VISIBLE : GONE);
        // if (shown) {
        // mCalendarView.setOnDateChangeListener(mCalendarChange);
        // } else {
        // mCalendarView.setOnDateChangeListener(null);
        // }
    }

    /**
     * Sets the current locale.
     *
     * @param locale The current locale.
     */
    private void setCurrentLocale(Locale locale) {
        if (locale.equals (mCurrentLocale)) {
            return;
        }

        mCurrentLocale = locale;

        mCalendar = getCalendarForLocale (mCalendar, locale);
        // mMinDate = getCalendarForLocale(mMinDate, locale);
        // mMaxDate = getCalendarForLocale(mMaxDate, locale);
        // mCurrentDate = (ChineseCalendar) getCalendarForLocale(mCurrentDate,
        // locale);
        mMinDate = new ChineseCalendar (getContext ());
        mMaxDate = new ChineseCalendar (getContext ());
        mCurrentDate = new ChineseCalendar (getContext ());

        int numberOfMonths = mCalendar.getActualMaximum (Calendar.MONTH) + 1;
        String[] shortMonths = new DateFormatSymbols ().getShortMonths ();

        if (usingNumericMonths ()) {
            // We're in a locale where a date should either be all-numeric, or
            // all-text.
            // All-text would require custom NumberPicker formatters for day and
            // year.
            shortMonths = new String[numberOfMonths];
            for (int i = 0; i < numberOfMonths; ++i) {
                shortMonths[i] = String.format ("%d", i + 1);
            }
        }
    }

    /**
     * Tests whether the current locale is one where there are no real month
     * names, such as Chinese, Japanese, or Korean locales.
     */
    private boolean usingNumericMonths() {
        // tws-start using NumericMonth::2014-8-22
        // return Character.isDigit(mShortMonths[Calendar.JANUARY].charAt(0));
        return true;
        // tws-end using NumericMonth::2014-8-22
    }

    /**
     * Gets a calendar for locale bootstrapped with the value of a given
     * calendar.
     *
     * @param oldCalendar The old calendar.
     * @param locale      The locale.
     */
    private Calendar getCalendarForLocale(Calendar oldCalendar, Locale locale) {
        if (oldCalendar == null) {
            return Calendar.getInstance (locale);
        } else {
            final long currentTimeMillis = oldCalendar.getTimeInMillis ();
            Calendar newCalendar = Calendar.getInstance (locale);
            newCalendar.setTimeInMillis (currentTimeMillis);
            return newCalendar;
        }
    }

    /**
     * Updates the current date.
     *
     * @param year       The year.
     * @param month      The month which is <strong>starting from zero</strong>.
     * @param dayOfMonth The day of the month.
     */
    public void updateDate(int year, int month, int dayOfMonth) {
        if (!isNewDate (year, month, dayOfMonth)) {
            return;
        }
        setDate (year, month, dayOfMonth);
        updateSpinners ();
        updateCalendarView ();
        notifyDateChanged ();
    }

    // Override so we are in complete control of save / restore for this widget.
    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly (container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState ();
        return new SavedState (superState, getYear (), getMonth (), getDayOfMonth ());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState (ss.getSuperState ());
        setDate (ss.mYear, ss.mMonth, ss.mDay);
        updateSpinners ();
        updateCalendarView ();
    }

    /**
     * Initialize the state. If the provided values designate an inconsistent
     * date the values are normalized before updating the spinners.
     *
     * @param year                  The initial year.
     * @param monthOfYear           The initial month <strong>starting from zero</strong>.
     * @param dayOfMonth            The initial day of the month.
     * @param onDateChangedListener How user is notified date is changed by user, can be null.
     */
    public void init(int year, int monthOfYear, int dayOfMonth, OnDateChangedListener onDateChangedListener) {
        setDate (year, monthOfYear, dayOfMonth);
        updateSpinners ();
        updateCalendarView ();
        mOnDateChangedListener = onDateChangedListener;
    }


    public void setOnDateChangedListener(OnDateChangedListener listener) {
        mOnDateChangedListener = listener;
    }

    /**
     * Parses the given <code>date</code> and in case of callBack sets the result
     * to the <code>outDate</code>.
     *
     * @return True if the date was parsed.
     */
    private boolean parseDate(String date, Calendar outDate) {
        try {
            outDate.setTime (mDateFormat.parse (date));
            return true;
        } catch (ParseException e) {
            Log.w (LOG_TAG, "Date: " + date + " not in format: " + DATE_FORMAT);
            return false;
        }
    }

    private boolean isNewDate(int year, int month, int dayOfMonth) {
        return (mCurrentDate.get (Calendar.YEAR) != year || mCurrentDate.get (Calendar.MONTH) != dayOfMonth || mCurrentDate
                .get (Calendar.DAY_OF_MONTH) != month);
    }

    private void setDate(int year, int month, int dayOfMonth) {
        mCurrentDate.set (year, month, dayOfMonth);
        if (mCurrentDate.before (mMinDate)) {
            mCurrentDate.setTimeInMillis (mMinDate.getTimeInMillis ());
        } else if (mCurrentDate.after (mMaxDate)) {
            mCurrentDate.setTimeInMillis (mMaxDate.getTimeInMillis ());
        }
    }

    @SuppressLint("WrongConstant")
    private void updateSpinners() {
        // year spinner range does not change based on the current date
        mYearSpinner.setMinValue (mMinDate.get (Calendar.YEAR));
        mYearSpinner.setMaxValue (mMaxDate.get (Calendar.YEAR));
//        mYearSpinner.setWrapSelectorWheel(true);
        mYearSpinner.setFormatter (mYearFormatter);
//        if (mUnitShown) {
//            mYearSpinner.setFormatter(mYearFormatter);
//        }

//        if (mIsLunar) {
//            mDaySpinner.setFormatter(mLunarDayFormatter);
//            mMonthSpinner.setFormatter(null);
//            mMonthSpinner.setDisplayedValues(null);
//            String[] months = mCurrentDate.getChinesMonths(mCurrentDate.get(ChineseCalendar.CHINESE_YEAR));
//
//            if (mCurrentDate.get(ChineseCalendar.CHINESE_YEAR) == mMinDate.get(ChineseCalendar.CHINESE_YEAR)) {
//                int minMonth = mMinDate.get(ChineseCalendar.CHINESE_MONTH) - 1;
//                int maxMonth = mMinDate.getActualMaximum(ChineseCalendar.CHINESE_MONTH);
//                int monthCount = maxMonth - minMonth + 1;
//                String[] tempMonths = new String[monthCount];
//                for (int i = 0; i < tempMonths.length; i++) {
//                    tempMonths[i] = months[minMonth + i];
//                }
//                months = tempMonths;
//                mMonthSpinner.setMinValue(minMonth);
//                mMonthSpinner.setMaxValue(maxMonth);
//                mMonthSpinner.setWrapSelectorWheel(false);
//                if (mCurrentDate.get(ChineseCalendar.CHINESE_MONTH) == mMinDate.get(ChineseCalendar.CHINESE_MONTH)) {
//                    mDaySpinner.setMinValue(mMinDate.get(ChineseCalendar.CHINESE_DATE));
//                    mDaySpinner.setMaxValue(mMinDate.getActualMaximum(ChineseCalendar.CHINESE_DATE));
//                    mDaySpinner.setWrapSelectorWheel(false);
//                } else {
//                    mDaySpinner.setMinValue(1);
//                    mDaySpinner.setMaxValue(mCurrentDate.getActualMaximum(ChineseCalendar.CHINESE_DATE));
//                    mDaySpinner.setWrapSelectorWheel(true);
//                }
//
//            } else if (mCurrentDate.get(ChineseCalendar.CHINESE_YEAR) == mMaxDate.get(ChineseCalendar.CHINESE_YEAR)) {
//                mMonthSpinner.setMinValue(0);
//                mMonthSpinner.setMaxValue(mMaxDate.get(ChineseCalendar.CHINESE_MONTH));
//                mMonthSpinner.setWrapSelectorWheel(false);
//                if (mCurrentDate.get(ChineseCalendar.CHINESE_MONTH) == mMaxDate.get(ChineseCalendar.CHINESE_MONTH)) {
//                    mDaySpinner.setMinValue(1);
//                    mDaySpinner.setMaxValue(mMaxDate.get(ChineseCalendar.CHINESE_DATE));
//                    mDaySpinner.setWrapSelectorWheel(false);
//                } else {
//                    mDaySpinner.setMinValue(1);
//                    mDaySpinner.setMaxValue(mCurrentDate.getActualMaximum(ChineseCalendar.CHINESE_DATE));
//                    mDaySpinner.setWrapSelectorWheel(true);
//                }
//            } else {
//                mDaySpinner.setMinValue(1);
//                mDaySpinner.setMaxValue(mCurrentDate.getActualMaximum(ChineseCalendar.CHINESE_DATE));
//                mDaySpinner.setWrapSelectorWheel(true);
//
//                mMonthSpinner.setMinValue(0);
//                mMonthSpinner.setMaxValue(mCurrentDate.getActualMaximum(ChineseCalendar.CHINESE_MONTH));
//                mMonthSpinner.setWrapSelectorWheel(true);
//            }
//
//            mMonthSpinner.setDisplayedValues(months);
//            int leapMonth = ChineseCalendar.getChineseLeapMonth(mCurrentDate.get(ChineseCalendar.CHINESE_YEAR));
//
//            int lunarMonth = mCurrentDate.get(ChineseCalendar.CHINESE_MONTH);
//            int index = lunarMonth;
//            if (leapMonth > 0) {
//                if (lunarMonth < 0 || lunarMonth > leapMonth) {
//                    index = Math.abs(lunarMonth) + 1;
//                }
//            }
//            mMonthSpinner.setValue(index - 1);
//            mYearSpinner.setValue(mCurrentDate.get(ChineseCalendar.CHINESE_YEAR));
//            mDaySpinner.setValue(mCurrentDate.get(ChineseCalendar.CHINESE_DATE));
//
//        }
//        else {
//            mMonthSpinner.setDisplayedValues(null);
//            if (mUnitShown) {
//                mMonthSpinner.setFormatter(mMonthFormatter);
//                mDaySpinner.setFormatter(mDayFormatter);
//            } else {
//                mMonthSpinner.setFormatter(mMonthNoUnitFormatter);
//                mDaySpinner.setFormatter(mDayNoUnitFormatter);
//            }
        mMonthSpinner.setFormatter (mMonthFormatter);
        mDaySpinner.setFormatter (mDayFormatter);
        mDaySpinner.setMinValue (1);
        mDaySpinner.setMaxValue (mCurrentDate.getActualMaximum (Calendar.DAY_OF_MONTH));
//            mDaySpinner.setWrapSelectorWheel(true);
        mMonthSpinner.setMinValue (0);
        mMonthSpinner.setMaxValue (11);
//            mMonthSpinner.setWrapSelectorWheel(true);
        mMonthSpinner.setValue (mCurrentDate.get (Calendar.MONTH));
        mDaySpinner.setValue (mCurrentDate.get (Calendar.DAY_OF_MONTH));
        mYearSpinner.setValue (mCurrentDate.get (Calendar.YEAR));

        if (isList) {
            if (mOnDateChangedListener != null) {
                mOnDateChangedListener.onItemChanged (mPickerListSpinner.getValue ());
            }
        } else {
            if (mOnDateChangedListener != null) {
                mOnDateChangedListener.onDateChanged (this, mYearSpinner.getValue (), mMonthSpinner.getValue (), mDaySpinner.getValue ());
            }
        }

    }
//    }

    /**
     * Updates the calendar view with the current date.
     */
    private void updateCalendarView() {
        // mCalendarView.setDate(mCurrentDate.getTimeInMillis(), false, false);
    }

    /**
     * @return The selected year.
     */
    public int getYear() {
        return mCurrentDate.get (Calendar.YEAR);
    }

    /**
     * @return The selected month.
     */
    public int getMonth() {
        return mCurrentDate.get (Calendar.MONTH);
    }

    /**
     * @return The selected day of month.
     */
    public int getDayOfMonth() {
        return mCurrentDate.get (Calendar.DAY_OF_MONTH);
    }

    /**
     * Notifies the listener, if such, for a change in the selected date.
     */
    private void notifyDateChanged() {
        // sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged (this, getYear (), getMonth (), getDayOfMonth ());
        }
    }

    /**
     * Sets the IME options for a spinner based on its ordering.
     *
     * @param spinner      The spinner.
     * @param spinnerCount The total spinner count.
     * @param spinnerIndex The index of the given spinner.
     */
    private void setImeOptions(NumberPicker spinner, int spinnerCount, int spinnerIndex) {
        final int imeOptions;
        if (spinnerIndex < spinnerCount - 1) {
            imeOptions = EditorInfo.IME_ACTION_NEXT;
        } else {
            imeOptions = EditorInfo.IME_ACTION_DONE;
        }
        TextView input = spinner.findViewById (R.id.numberpicker_input);
        input.setImeOptions (imeOptions);
    }

    private void setContentDescriptions() {
        // Day
        trySetContentDescription (mDaySpinner, R.id.increment, R.string.date_picker_increment_day_button);
        trySetContentDescription (mDaySpinner, R.id.decrement, R.string.date_picker_decrement_day_button);
        // Month
        trySetContentDescription (mMonthSpinner, R.id.increment, R.string.date_picker_increment_month_button);
        trySetContentDescription (mMonthSpinner, R.id.decrement, R.string.date_picker_decrement_month_button);
        // Year
        trySetContentDescription (mYearSpinner, R.id.increment, R.string.date_picker_increment_year_button);
        trySetContentDescription (mYearSpinner, R.id.decrement, R.string.date_picker_decrement_year_button);
    }

    private void trySetContentDescription(View root, int viewId, int contDescResId) {
        View target = root.findViewById (viewId);
        if (target != null) {
            target.setContentDescription (mContext.getString (contDescResId));
        }
    }

    private void updateInputState() {
        // Make sure that if the user changes the value and the IME is active
        // for one of the inputs if this widget, the IME is closed. If the user
        // changed the value via the IME and there is a next input the IME will
        // be shown, otherwise the user chose another means of changing the
        // value and having the IME up makes no sense.
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService (Activity.INPUT_METHOD_SERVICE);

//		InputMethodManager inputMethodManager = InputMethodManager.peekInstance();
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive (mYearSpinnerInput)) {
                mYearSpinnerInput.clearFocus ();
                inputMethodManager.hideSoftInputFromWindow (getWindowToken (), 0);
            } else if (inputMethodManager.isActive (mMonthSpinnerInput)) {
                mMonthSpinnerInput.clearFocus ();
                inputMethodManager.hideSoftInputFromWindow (getWindowToken (), 0);
            } else if (inputMethodManager.isActive (mDaySpinnerInput)) {
                mDaySpinnerInput.clearFocus ();
                inputMethodManager.hideSoftInputFromWindow (getWindowToken (), 0);
            }
        }
    }

    /**
     * Class for managing state storing/restoring.
     */
    private static class SavedState extends BaseSavedState {

        private final int mYear;

        private final int mMonth;

        private final int mDay;

        /**
         * Constructor called from {@link DatePicker#onSaveInstanceState()}
         */
        private SavedState(Parcelable superState, int year, int month, int day) {
            super (superState);
            mYear = year;
            mMonth = month;
            mDay = day;
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super (in);
            mYear = in.readInt ();
            mMonth = in.readInt ();
            mDay = in.readInt ();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel (dest, flags);
            dest.writeInt (mYear);
            dest.writeInt (mMonth);
            dest.writeInt (mDay);
        }

        @SuppressWarnings("all")
        // suppress unused and hiding
        public static final Creator<SavedState> CREATOR = new Creator<SavedState> () {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState (in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    OnDateChangeListener mCalendarChange = new OnDateChangeListener () {
        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int monthDay) {
            setDate (year, month, monthDay);
            updateSpinners ();
            notifyDateChanged ();
        }
    };

    public void setIsLunar(boolean isLunar) {
        mIsLunar = isLunar;
        updateSpinners ();
        mLunarSpinner.setValue (isLunar ? ChineseCalendar.CALENDAR_TYPE_LUNAR : ChineseCalendar.CALENDAR_TYPE_GREGORIAN);
        // Log.d(LOG_TAG,
        // "setIsLunar=" + isLunar + ",lunarYear=" +
        // mCurrentDate.get(ChineseCalendar.CHINESE_YEAR)
        // + ",Year=" + mCurrentDate.get(Calendar.YEAR));
    }

    public boolean isLunar() {
        return mIsLunar;
    }

    public void setLunarSpinnerVisibility(boolean isLunar) {
        mLunarSpinner.setVisibility (isLunar ? View.VISIBLE : View.GONE);
        if (!isLunar) {
            mLunarSpinner.setValue (ChineseCalendar.CALENDAR_TYPE_GREGORIAN);
            setIsLunar (isLunar);
        }
        invalidate ();
    }

    public void setYearSpinnerVisibility(boolean isShow) {
        mYearSpinner.setVisibility (isShow ? View.VISIBLE : View.GONE);
        invalidate ();
    }

    NumberPicker.Formatter mYearFormatter = new NumberPicker.Formatter () {
        @Override
        public String format(int value) {
            return value + mYearName;
        }
    };
    NumberPicker.Formatter mMonthFormatter = new NumberPicker.Formatter () {
        @Override
        public String format(int value) {
            if (value < 9) {
                return "0" + (value + 1) + mMonthName;
            }

            return (value + 1) + mMonthName;
        }
    };
    NumberPicker.Formatter mMonthNoUnitFormatter = new NumberPicker.Formatter () {
        @Override
        public String format(int value) {
            if (value < 9) {
                return "0" + (value + 1);
            }

            return (value + 1) + "";
        }
    };
    NumberPicker.Formatter mDayFormatter = new NumberPicker.Formatter () {
        @Override
        public String format(int value) {
            if (value < 10) {
                return "0" + value + mDayName;
            }

            return value + mDayName;
        }
    };

    NumberPicker.Formatter mDayNoUnitFormatter = new NumberPicker.Formatter () {
        @Override
        public String format(int value) {
            if (value < 10) {
                return "0" + value;
            }

            return value + "";
        }
    };
    NumberPicker.Formatter mLunarDayFormatter = new NumberPicker.Formatter () {
        @Override
        public String format(int value) {
            return mChineseDateNames[value];
        }
    };
}
