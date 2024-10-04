package com.pasc.business.ewallet.business.traderecord.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.business.traderecord.net.resp.BillBean;
import com.pasc.business.ewallet.business.traderecord.presenter.BillListPresenter;
import com.pasc.business.ewallet.business.traderecord.view.BillListView;
import com.pasc.business.ewallet.common.customview.IReTryListener;
import com.pasc.business.ewallet.common.customview.StatusView;
import com.pasc.business.ewallet.common.utils.StatisticsUtils;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.lib.glide.GlideUtil;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;

import java.util.List;

/**
 * @author yangzijian
 * @date 2019/3/22
 * @des
 * @modify
 **/
public class BillDetailActivity extends EwalletBaseMvpActivity<BillListPresenter> implements BillListView {

    private TextView tvDetailTitle;
    private TextView tvDetailMoney;
    private TextView tvDetailStatusName;
    private TextView tvDetailGoodNameValue;
    private View llDetailGoodNameAll,ll_detail_good_all;
    private TextView tvDetailGoodNameAllValue;
    private TextView tvDetailTimeValue;
    View ll_payType;
    private TextView tvDetailPayTypeValue;
    private TextView tvDetailZhangdanValue;
    private ImageView iv_detail_logo;
    private StatusView statusView;
    private View llContent;
    String mchOrderNo;
    String orderNo;
    String tradeType;
    @Override
    protected int layoutResId() {
        return R.layout.ewallet_pay_bill_detail_activity;
    }

    @Override
    protected void initView() {
        PascToolbar toolbar = findViewById(R.id.ewallet_activity_toolbar);
        toolbar.setTitle (getString (R.string.ewallet_bill_detail_title));
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        statusView = findViewById (R.id.ewallet_pay_yue_record_statusView);
        llContent=findViewById (R.id.ll_content);
        statusView.setContentView (llContent);
        iv_detail_logo = findViewById (R.id.iv_detail_logo);
        llDetailGoodNameAll=findViewById (R.id.ll_detail_good_name_all);
        tvDetailTitle = findViewById (R.id.tv_detail_title);
        tvDetailMoney = findViewById (R.id.tv_detail_money);
        tvDetailStatusName = findViewById (R.id.tv_detail_status_name);
        tvDetailGoodNameValue = findViewById (R.id.tv_detail_good_name_value);
        tvDetailGoodNameAllValue = findViewById (R.id.tv_detail_good_name_all_value);
        tvDetailTimeValue = findViewById (R.id.tv_detail_time_value);
        tvDetailPayTypeValue = findViewById (R.id.tv_detail_pay_type_value);
        tvDetailZhangdanValue = findViewById (R.id.tv_detail_zhangdan_value);
        ll_detail_good_all=findViewById (R.id.ll_detail_good_all);
        ll_payType=findViewById (R.id.ll_payType);
        statusView.setTryListener (new IReTryListener () {
            @Override
            public void tryAgain() {
                load ();
            }
        });
        StatisticsUtils.qb_paymentdetail ();
    }

    @Override
    protected void initData(Bundle bundleData) {
        mchOrderNo=bundleData.getString (BundleKey.Pay.key_mchOrderNo);
        orderNo=bundleData.getString (BundleKey.Pay.key_orderNo);
        tradeType=bundleData.getString (BundleKey.Pay.key_tradeType, StatusTable.Trade.PAY);
        load ();
    }

    @Override
    protected BillListPresenter createPresenter() {
        return new BillListPresenter ();
    }

    @Override
    public void billList(List<BillBean> billBeans, boolean hasMore) {

    }

    @Override
    public void billDetail(QueryOrderResp billBean) {
        statusView.showContent ();
        tvDetailMoney.setText (billBean.getPayAmountDetail ());
//        // 显示黑色即可
//        tvDetailMoney.setText (billBean.getPayAmountForDetail());
//
        tvDetailStatusName.setText (billBean.statusDesc);
        tvDetailTimeValue.setText (billBean.getAllTime ());
        int DefaultIcon=billBean.getDefaultIcon ();
        GlideUtil.loadImage (this,iv_detail_logo,billBean.merchantIcon,DefaultIcon,DefaultIcon);
        tvDetailZhangdanValue.setText (billBean.orderNo);
        iv_detail_logo.setVisibility (Util.isEmpty (billBean.goodsName)?View.GONE:View.VISIBLE);

        tvDetailTitle.setText (billBean.goodsName);
        tvDetailGoodNameValue.setText (billBean.goodsName);
        tvDetailGoodNameAllValue.setText (billBean.merchantName);
        tvDetailPayTypeValue.setText (billBean.channelDesc);
        llDetailGoodNameAll.setVisibility (Util.isEmpty (billBean.merchantName)?View.GONE:View.VISIBLE);
        ll_detail_good_all.setVisibility (Util.isEmpty (billBean.goodsName)?View.GONE:View.VISIBLE);
        ll_payType.setVisibility (Util.isEmpty (billBean.channelDesc)?View.GONE:View.VISIBLE);
    }

    @Override
    public void tradeError(String code, String msg) {
//        statusView.setError (R.drawable.ewallet_net_error_icon,msg);
        statusView.showError ();
    }

    void load(){
        statusView.showLoading ();
        mPresenter.getBillDetail (mchOrderNo,orderNo,tradeType);

    }
}
