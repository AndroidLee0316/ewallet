package com.pasc.business.ewallet.widget.dialog.bottompicker;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.util.DateUtil;
import com.pasc.business.ewallet.widget.dialog.bottompicker.utils.ChineseCalendar;
import com.pasc.business.ewallet.widget.dialog.bottompicker.widget.DatePickView;

import java.util.GregorianCalendar;

/**
 * @date 2019/7/12
 * @des
 * @modify
 **/
public class DatePickerDialog extends Dialog implements View.OnClickListener {
    private TextView closeTv;
    private TextView titleTv;
    private TextView confirmTv;
    private DatePickView datePicker;
    private final View contentView;

    private void assignViews() {
        closeTv = contentView.findViewById (R.id.close_tv);
        titleTv = contentView.findViewById (R.id.title_tv);
        confirmTv = contentView.findViewById (R.id.confirm_tv);
        datePicker = contentView.findViewById (R.id.date_picker);
    }

    @SuppressLint("WrongConstant")
    public DatePickerDialog(@NonNull Context context, String title) {
        super (context, R.style.Dialog_NoTitle);
        GregorianCalendar now = new GregorianCalendar ();

        contentView = View.inflate (context, R.layout.ewallet_pay_date_picker_dialog, null);
        setContentView (contentView);
        assignViews ();
        titleTv.setText (title);
        WindowManager.LayoutParams attributes = getWindow ().getAttributes ();
        attributes.gravity = Gravity.BOTTOM;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow ().setAttributes (attributes);

        int currentYear = now.get (ChineseCalendar.YEAR);
        int currentMonth = now.get (ChineseCalendar.MONTH)+1;


        int limitStartYear1 = 2019; // 开始年份
        int limitStartMonth1=6; // 开始年份最小月
        int limitEndMonth1=12; // 开始年份 最大月，如果当前年份和开始年份相等的话，则开始年份最大月当前当前月 ，反之为12

        int limitStartYear2 = currentYear; // 当前年份
        int limitStartMonth2=1; // 当前年份最小月，如果 开始年份和 当前年份相等的话， 当前年份最小月 = 开始年份最小月
        int limitEndMonth2=currentMonth; // 当前年份最大月，即当前月

        if (currentYear>limitStartYear1){
            // 当前年份大于 开始年份的呢
            limitStartMonth2=1;
            limitEndMonth1=12;
        }else {
            //当前年份相等
            limitStartMonth2=limitStartMonth1;
            limitEndMonth1=currentMonth;

        }

        datePicker.setYearMonthLimit (limitStartYear1,limitStartMonth1,limitEndMonth1,limitStartYear2,limitStartMonth2,limitEndMonth2);

        updateYearStartEnd (limitStartYear1, limitStartYear2);
        updateMonthStartEnd (limitStartMonth2, limitEndMonth2);


        datePicker.updateMonthValue (currentMonth);
        datePicker.updateYearValue (currentYear);
        closeTv.setOnClickListener (this);
        confirmTv.setOnClickListener (this);

        datePicker.setCircle (false);

    }

    public void updateMonthStartEnd(int minMonth, int endMonth) {
        datePicker.updateMonthStartEnd (minMonth, endMonth);
    }

    public void updateYearStartEnd(int minYear, int endYear) {
        datePicker.updateYearStartEnd (minYear, endYear);
    }

    @Override
    public void onClick(View v) {
        if (pickerListener != null) {
            if (v == closeTv) {
                pickerListener.cancel ();
            } else if (v == confirmTv) {
                pickerListener.confirm (datePicker.getCurrentYear (), datePicker.getCurrentMonth (), 1);

            }
        }
        dismiss ();

    }

    private PickerListener pickerListener;

    public void setPickerListener(PickerListener pickerListener) {
        this.pickerListener = pickerListener;
    }

    @Override
    public void show() {
        if (pickerListener!=null){
            pickerListener.beforeShow ();
        }
        super.show ();
    }

    public interface PickerListener {
        void confirm(int year, int month, int day);

        void cancel();

        void beforeShow();

    }

    public void setValue(int year,int month){
        if (year<1900){
            return;
        }

        int currentYearMonth[]= DateUtil.getCurrentYearMonth ();
        int currentYear=currentYearMonth[0];
        int currentMonth=currentYearMonth[1];
        if (year>currentYear){
            return;
        }else if (year==currentYear && month>currentMonth){
            return;
        }

        datePicker.updateYearValue (year);
        datePicker.updateMonthValue (month);
    }

}
