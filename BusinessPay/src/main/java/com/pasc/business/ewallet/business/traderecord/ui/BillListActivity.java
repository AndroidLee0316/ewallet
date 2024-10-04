package com.pasc.business.ewallet.business.traderecord.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.traderecord.net.resp.BillTypeResp;
import com.pasc.business.ewallet.business.traderecord.presenter.BillHomePresenter;
import com.pasc.business.ewallet.business.traderecord.view.BillFailRollBackView;
import com.pasc.business.ewallet.business.traderecord.view.BillHomeView;
import com.pasc.business.ewallet.business.util.DateUtil;
import com.pasc.business.ewallet.business.util.FragmentUtils;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.widget.dialog.OnCloseListener;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;
import com.pasc.business.ewallet.widget.dialog.bottompicker.DatePickerDialog;
import com.pasc.business.ewallet.widget.dialog.bottompicker.ListPickerDialogFragment;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.pay.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public class BillListActivity extends EwalletBaseMvpActivity<BillHomePresenter> implements View.OnClickListener, BillHomeView {
    private TextView tvType, tvTime;
    private int prePosition = 0;
    private List<BillTypeResp.PayBillTypeBean> payBillTypeList=new ArrayList<> ();
    {
        //RECHARGE：充值，REFUND：退款；PAY：消费；WITHDRAW：提现
        payBillTypeList.add (new BillTypeResp.PayBillTypeBean (StatusTable.Trade.ALL,"全部"));
        payBillTypeList.add (new BillTypeResp.PayBillTypeBean (StatusTable.Trade.RECHARGE,"充值"));
        payBillTypeList.add (new BillTypeResp.PayBillTypeBean (StatusTable.Trade.REFUND,"退款"));
        payBillTypeList.add (new BillTypeResp.PayBillTypeBean (StatusTable.Trade.PAY,"消费"));
        payBillTypeList.add (new BillTypeResp.PayBillTypeBean (StatusTable.Trade.WITHDRAW,"提现"));



    }
    private BaseBillListFragment billFragment;
    private int year = -1;
    private int month = -1;
    private String memberNo;

    @Override
    protected BillHomePresenter createPresenter() {
        return new BillHomePresenter ();
    }

    @Override
    protected void initView() {
        PascToolbar toolbar = findViewById (R.id.ewallet_activity_toolbar);
        toolbar.setTitle (getString (R.string.ewallet_bill_list_title));
        toolbar.addRightImageButton (R.drawable.ewallet_search_nav).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                RouterManager.BalanceBillRouter.gotoBillSearch (getActivity (),memberNo);
            }
        });
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        tvType = findViewById (R.id.ewallet_tv_type);
        tvTime = findViewById (R.id.ewallet_tv_time);
        tvType.setOnClickListener (this);
        tvTime.setOnClickListener (this);

        billFragment = (BaseBillListFragment) getSupportFragmentManager ()
                .findFragmentById (R.id.ewallet_pay_bill_container);

        if (billFragment == null) {
            billFragment = new BillListFragmentYC ();
            Bundle bundle = null;
            if (getIntent () != null) {
                Intent intent = getIntent ();
                bundle = intent.getExtras ();
            }
            if (bundle == null) {
                bundle = new Bundle ();
            }
            bundle.putBoolean (BundleKey.Trade.key_search_flag, false);
            billFragment.setArguments (bundle);
            FragmentUtils.showTargetFragment (getSupportFragmentManager (),
                    billFragment, R.id.ewallet_pay_bill_container);
        }
        billFragment.setBillRollBackView (new BillFailRollBackView () {
            @Override
            public void rollback(String orderTypeId, String payYearMonth) {
                if (!Util.isEmpty (orderTypeId)) {
                    if (payBillTypeList != null) {
                        for (int position = 0; position < payBillTypeList.size (); position++) {
                            BillTypeResp.PayBillTypeBean typeBean = payBillTypeList.get (position);
                            if (orderTypeId.equals (typeBean.id)) {
                                prePosition = position;
                                tvType.setText (typeBean.typeName);
                                break;
                            }
                        }
                    }
                } else {
                    tvType.setText (R.string.ewallet_trade_bill_type);
                }

                if (!Util.isEmpty (payYearMonth)) {
                    tvTime.setText (payYearMonth);
                    int yearMonthArr[] = DateUtil.getYearMonth (payYearMonth);
                    year = yearMonthArr[0];
                    month = yearMonthArr[1];
                } else {
                    tvTime.setText (R.string.ewallet_trade_bill_time);
                }
            }
        });
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_pay_bill_record_activity;
    }

    @Override
    protected void initData(Bundle bundleData) {
        memberNo = bundleData.getString (BundleKey.Pay.key_memberNo, UserManager.getInstance ().getMemberNo ());
        mPresenter.getPayTypeList (false);
    }

    @Override
    public void onClick(View v) {
        if (v == tvType) {
            if (payBillTypeList == null || payBillTypeList.size () == 0) {
                if (!Util.isNetworkAvailable (this)) {
                    ToastUtils.toastMsg (R.string.ewallet_network_err);
                    return;
                }
                mPresenter.getPayTypeList (true);
            } else {
                showTypeDialog ();
            }
        } else if (v == tvTime) {
            showTimeDialog ();
        }
    }

    void showTimeDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog (this, "日期");
        datePickerDialog.setPickerListener (new DatePickerDialog.PickerListener () {
            @Override
            public void confirm(int y, int m, int day) {
                year = y;
                month = m;
                LogUtil.loge ("yzj", "confirm: year: " + year + " month: " + month);
                String payYearMonth = DateUtil.yearMonthStr (year, month);
//                tvTime.setText (payYearMonth);
//                tvType.setText (R.string.ewallet_trade_bill_type);
                if (billFragment != null) {
                    billFragment.updateYearMonth (payYearMonth);
                }
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

    void showTypeDialog() {
        if (payBillTypeList == null) {
            return;
        }
        int size = payBillTypeList.size ();
        if (size == 0) {
            return;
        }
        String[] arr = new String[size];
        for (int i = 0; i < size; i++) {
            arr[i] = payBillTypeList.get (i).typeName;
        }
        ListPickerDialogFragment mListPickerDialogFragment = new ListPickerDialogFragment.Builder ()
                .setOnCloseListener (new OnCloseListener<ListPickerDialogFragment> () {
                    @Override
                    public void onClose(ListPickerDialogFragment dialogFragment) {
                        dialogFragment.dismiss ();
                    }
                })
                .setOnConfirmListener (new OnConfirmListener<ListPickerDialogFragment> () {
                    @Override
                    public void onConfirm(ListPickerDialogFragment dialogFragment) {
                        prePosition = dialogFragment.getPosition ();
                        BillTypeResp.PayBillTypeBean typeBean = payBillTypeList.get (prePosition);
                        String typeId = typeBean.id;
                        //tvType.setText (typeBean.typeName);
//                        tvTime.setText (R.string.ewallet_trade_bill_time);
                        if (billFragment != null) {
                            billFragment.updateType (typeId);
                        }
                        dialogFragment.dismiss ();
                    }
                })
                .setTitle ("请选择账单类型")
                .setCloseText (getString (R.string.ewallet_cancel))
                .setConfirmText (getString (R.string.ewallet_confirm))
                .setItems (arr, 0)
                .setCircling (false)
                .build ();

        if (prePosition >= 0 && prePosition < size) {
            mListPickerDialogFragment.setPosition (prePosition);
        }

        mListPickerDialogFragment.show (getSupportFragmentManager (), "typeDialogTag");
    }


    @Override
    public void getPayTypeListSuccess(List<BillTypeResp.PayBillTypeBean> payBillTypeList, boolean needShow) {
        this.payBillTypeList = payBillTypeList;
        if (needShow) {
            showTypeDialog ();
        }
    }

    @Override
    public void getPayTypeListError(String code, String msg) {
        ToastUtils.toastMsg (msg);
    }

    @Override
    public void hideLoading() {
        super.dismissLoading ();
    }

    @Override
    public void showLoading(String msg) {
        super.showLoading (msg);
    }
 //暂时不用归位
//    @Override
//    protected void onResume() {
//        super.onResume ();
//        if (year<0 || month<0){
//            return;
//        }
//        int currentYearMonth[]=DateUtil.getCurrentYearMonth ();
//        int currentYear=currentYearMonth[0];
//        int currentMonth=currentYearMonth[1];
//        if (year>currentYear){
//            reSetDate ();
//        }else if (year==currentYear && month>currentMonth){
//            reSetDate ();
//        }
//
//    }
//
//    void reSetDate(){
//       year = -1;
//       month = -1;
//       tvTime.setText (R.string.ewallet_trade_bill_time);
//        if (billFragment != null) {
//            billFragment.updateAll ();
//        }
//    }
}
