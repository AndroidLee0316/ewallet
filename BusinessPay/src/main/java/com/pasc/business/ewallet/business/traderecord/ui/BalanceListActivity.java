package com.pasc.business.ewallet.business.traderecord.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.traderecord.adapter.TradeRecordListAdapter;
import com.pasc.business.ewallet.business.traderecord.net.resp.AvailBalanceBean;
import com.pasc.business.ewallet.business.traderecord.presenter.BalancePresenter;
import com.pasc.business.ewallet.business.traderecord.view.BalanceView;
import com.pasc.business.ewallet.common.customview.CustomLoadMoreView;
import com.pasc.business.ewallet.common.customview.IReTryListener;
import com.pasc.business.ewallet.common.customview.StatusView;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.config.Constants;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.adapter.base.BaseQuickAdapter;
import com.pasc.lib.pay.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public class BalanceListActivity extends EwalletBaseMvpActivity<BalancePresenter> implements BalanceView {
    private final int pageSize = 20;
    private static final int PAGE_START = 1;
    private int pageNo = PAGE_START;
    private boolean isRefresh = true;
    private StatusView statusView;
    private SwipeRefreshLayout refreshLayout;
    CustomLoadMoreView loadMoreView;
    private List<AvailBalanceBean> listItems = new ArrayList<> ();
    private TradeRecordListAdapter recordListAdapter;
    private String accessUserId, inChannelId;


    @Override
    protected BalancePresenter createPresenter() {
        return new BalancePresenter ();
    }

    @Override
    protected void initView() {
        PascToolbar toolbar = findViewById (R.id.ewallet_activity_toolbar);
        toolbar.setTitle ("余额明细");
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        statusView = findViewById (R.id.ewallet_pay_base_statusView);
        refreshLayout = findViewById (R.id.ewallet_pay_base_refreshLayout);
        RecyclerView recyclerView = findViewById (R.id.ewallet_pay_base_recyclerView);
        statusView.setContentView (refreshLayout);
        recordListAdapter = new TradeRecordListAdapter (listItems);
        recyclerView.setLayoutManager (new LinearLayoutManager (this));
        loadMoreView = new CustomLoadMoreView ();
        recordListAdapter.setLoadMoreView (loadMoreView);
        recyclerView.setAdapter (recordListAdapter);

        statusView.setEmpty (R.drawable.ewallet_empty_status_icon, "暂无余额明细信息");
        statusView.setTryListener (new IReTryListener () {
            @Override
            public void tryAgain() {
                pageNo = PAGE_START;
                loadMore (true, PAGE_START);
            }
        });
        refreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                loadMore (true, PAGE_START);
            }
        });
        recordListAdapter.setOnLoadMoreListener (new BaseQuickAdapter.RequestLoadMoreListener () {
            @Override
            public void onLoadMoreRequested() {
                if (refreshLayout.isRefreshing ()) {
                    recordListAdapter.loadMoreComplete ();
                    return;
                }
                int tmpPageNo = pageNo + 1;
                loadMore (false, tmpPageNo);
            }
        }, recyclerView);

        recordListAdapter.setOnItemChildClickListener (new BaseQuickAdapter.OnItemChildClickListener () {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                RouterManager.BalanceBillRouter.gotoBalanceDetail (getActivity (), listItems.get (position));
            }
        });
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_pay_yue_record_activity;
    }

    @Override
    protected void initData(Bundle bundleData) {
        accessUserId = bundleData.getString (BundleKey.Common.key_accessUserId, UserManager.getInstance ().getAccessUserId ());
        inChannelId = bundleData.getString (BundleKey.Common.key_inChannelId, Constants.IN_CHANNEL_ID);

        pageNo = PAGE_START;
        loadMore (true, PAGE_START);
    }

    @Override
    public void availBalanceList(List<AvailBalanceBean> balanceList) {
        refreshLayout.setRefreshing (false);
        recordListAdapter.loadMoreComplete ();
        if (isRefresh) {
            pageNo = PAGE_START;
            listItems.clear ();
        } else {
            pageNo++;
        }
        String lastMonth = null;
        if (listItems.size () > 0) {
            lastMonth = listItems.get (listItems.size () - 1).headerValue;
        }
        if (balanceList.size () > 0 && !TextUtils.isEmpty (lastMonth)) {
            AvailBalanceBean bean = balanceList.get (0);
            String firstMonth = bean.headerValue;
            if (lastMonth.equals (firstMonth)) {
                bean.isHeader = false;//拼接后去除标记header
                listItems.get (listItems.size () - 1).isLastInIt = false;//拼接后去需要添加分割线
            }
        }
        listItems.addAll (balanceList);
        if (balanceList.size () < pageSize) {
            //没有更多
            if (listItems.size () < pageSize) {
                //总共没那么多数据
                loadMoreView.setLoadEndViewVisible (true);
                recordListAdapter.loadMoreEnd (false);
            } else {
                loadMoreView.setLoadEndViewVisible (false);
                recordListAdapter.loadMoreEnd (false);
            }

        } else {
            loadMoreView.setLoadEndViewVisible (false);
            recordListAdapter.loadMoreComplete ();
        }


        recordListAdapter.notifyDataSetChanged ();
        if (listItems.size () > 0) {
            statusView.showContent ();
        } else {
            statusView.showEmpty ();

        }
    }

    @Override
    public void tradeError(String code, String msg) {
        refreshLayout.setRefreshing (false);
        if (listItems.size () > 0) {
            if (isRefresh) {
                ToastUtils.toastMsg (R.string.ewallet_toast_network_error_and_retry);
                recordListAdapter.loadMoreComplete ();
            } else {
                recordListAdapter.loadMoreFail ();
            }
        } else {
            statusView.showError (R.drawable.ewallet_error_status_icon, msg, Color.WHITE);
        }
    }

    void loadMore(boolean isRefresh, int pageNo) {
        this.isRefresh = isRefresh;
        if (listItems.size () == 0) {
            statusView.showLoading ();
        }
        mPresenter.getAvailBalanceList (accessUserId, inChannelId, pageNo, pageSize);
    }
}
