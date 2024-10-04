package com.pasc.business.ewallet.business.traderecord.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.traderecord.net.resp.PayMonthBean;
import com.pasc.business.ewallet.business.traderecord.presenter.BillMonthPresenter;
import com.pasc.business.ewallet.business.traderecord.view.BillFailRollBackView;
import com.pasc.business.ewallet.business.traderecord.view.BillMonthView;
import com.pasc.business.ewallet.business.util.DateUtil;
import com.pasc.business.ewallet.common.customview.IReTryListener;
import com.pasc.business.ewallet.common.customview.StatusView;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.widget.dialog.bottompicker.DatePickerDialog;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.pay.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019/7/8
 * @des
 * @modify
 **/
public class BillMonthActivity extends EwalletBaseMvpActivity<BillMonthPresenter> implements BillMonthView, BillFailRollBackView {
    MonthBillView tradeBillView;
    private TextView tvTime;
    private String payMonth, prePayMonth;
    private TextView ewalletPayDateTvDate, tvPayNum, tvPayMoney;
    private StatusView statusView;
    List<PayMonthBean> monthBeans;
    private String memberNo;
    private int year;
    private int month;

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_pay_bill_month_activity;
    }

    @Override
    protected void initView() {
        PascToolbar toolbar = findViewById (R.id.ewallet_activity_toolbar);
        toolbar.setTitle ("月度账单");
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        tradeBillView = findViewById (R.id.ewallet_trade_view);
        tvTime = findViewById (R.id.ewallet_pay_date_tv_date);
        tvTime.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                showTimeDialog ();
            }
        });
        tvPayNum = findViewById (R.id.ewallet_pay_num);
        tvPayMoney = findViewById (R.id.ewallet_pay_money);
        ewalletPayDateTvDate = findViewById (R.id.ewallet_pay_date_tv_date);
        statusView = findViewById (R.id.ewallet_pay_base_statusView);
        statusView.setEmpty (R.drawable.ewallet_pay_bill_empty, "暂无账单信息");
        statusView.setContentView (findViewById (R.id.ewallet_content_view));

        statusView.setTryListener (new IReTryListener () {
            @Override
            public void tryAgain() {
                requestData ();
            }
        });
    }

    void requestData() {
        //当前 + 前5 个月
        List<String> strings = DateUtil.getNextMonths (year, month, -5, "yyyy-MM");
        String startYearOfMonth=payMonth;
        int size=strings.size ();
        if (size>0){
            startYearOfMonth=strings.get (size-1);
        }
        mPresenter.getMonthlyBills2 (memberNo, startYearOfMonth,payMonth, prePayMonth);

    }


    @Override
    protected void initData(Bundle bundleData) {
        year = bundleData.getInt (BundleKey.Trade.key_year, BundleKey.Trade.defaultYear);
        month = bundleData.getInt (BundleKey.Trade.key_month, BundleKey.Trade.defaultMonth);
        memberNo = bundleData.getString (BundleKey.Pay.key_memberNo);
        payMonth = DateUtil.yearMonthStr (year, month);
        prePayMonth = payMonth;
        requestData ();
        ewalletPayDateTvDate.setText (payMonth);
    }

    void showTimeDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog (this, "日期");
        datePickerDialog.setPickerListener (new DatePickerDialog.PickerListener () {
            @Override
            public void confirm(int y, int m, int day) {
                year = y;
                month = m;
                LogUtil.loge ("yzj", "confirm: year: " + year + " month: " + month);
                payMonth = DateUtil.yearMonthStr (year, month);
                requestData ();
            }

            @Override
            public void cancel() {
                LogUtil.loge ("yzj", "cancel: ");

            }

            @Override
            public void beforeShow() {
                datePickerDialog.setValue (year, month);
            }
        });
        datePickerDialog.show ();
    }


    @Override
    protected BillMonthPresenter createPresenter() {
        return new BillMonthPresenter ();
    }

    @Override
    public void getMonthBillSuccess(List<PayMonthBean> monthBeans) {
        this.monthBeans = monthBeans;
        int size=monthBeans.size ();
        if (size>0){
            PayMonthBean payMonthBean=monthBeans.get (size-1);
            tvPayNum.setText ("共支出" + payMonthBean.getFoutqty () + "笔");
            tvPayMoney.setText (payMonthBean.getTotalOutlayStr ());
        }

        List<MonthBillView.BillItem> billItems = new ArrayList<> ();
            for (PayMonthBean bill : monthBeans) {
                billItems.add (new MonthBillView.BillItem (bill.yearOfMonth, bill.getTotalOutlay ()));
            }
        statusView.showContent ();
        tradeBillView.update (year, month, billItems);

    }

    @Override
    public void getMonthBillError(String code, String msg) {
        if (monthBeans == null) {
            statusView.showError ();
        } else {
            ToastUtils.toastMsg (msg);
            statusView.showContent ();
        }
//        tradeBillView.updateError (year, month);
    }

    @Override
    public void showLoading(String msg) {
        if (monthBeans == null) {
            statusView.showLoading ();
        } else {
            super.showLoading (msg);
        }
    }

    @Override
    public void rollback(String orderTypeId, String payYearMonth) {
        this.payMonth = this.prePayMonth = payYearMonth;
        if (!Util.isEmpty (payYearMonth)) {
            tvTime.setText (payYearMonth);
            int yearMonthArr[] = DateUtil.getYearMonth (payYearMonth);
            year = yearMonthArr[0];
            month = yearMonthArr[1];
        }
    }
//暂时不用归位
//    @Override
//    protected void onResume() {
//        super.onResume ();
//
//        if (year<0 || month<0){
//            return;
//        }
//        int currentYearMonth[]=DateUtil.getCurrentYearMonth ();
//        int currentYear=currentYearMonth[0];
//        int currentMonth=currentYearMonth[1];
//        if (year>currentYear){
//            year = currentYear;
//            month = currentMonth;
//            reSetDate ();
//        }else if (year==currentYear && month>currentMonth){
//            year = currentYear;
//            month = currentMonth;
//            reSetDate ();
//        }
//
//    }
//
//    void reSetDate(){
//        payMonth = DateUtil.yearMonthStr (year, month);
//        prePayMonth = payMonth;
//        ewalletPayDateTvDate.setText (payMonth);
//        requestData ();
//
//    }
}
