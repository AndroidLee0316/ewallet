package com.pasc.business.ewallet.business.traderecord.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.business.traderecord.adapter.TradeBillListAdapter;
import com.pasc.business.ewallet.business.traderecord.net.resp.BillBean;
import com.pasc.business.ewallet.business.traderecord.presenter.BillListPresenter;
import com.pasc.business.ewallet.business.traderecord.view.BillFailRollBackView;
import com.pasc.business.ewallet.business.traderecord.view.BillListView;
import com.pasc.business.ewallet.business.util.DateUtil;
import com.pasc.business.ewallet.common.customview.CustomLoadMoreView;
import com.pasc.business.ewallet.common.customview.IReTryListener;
import com.pasc.business.ewallet.common.customview.MySwipeRefreshLayout;
import com.pasc.business.ewallet.common.customview.StatusView;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.lib.adapter.base.BaseQuickAdapter;
import com.pasc.lib.pay.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019/7/8
 * @des
 * @modify
 **/
public class BillListFragment extends BaseBillListFragment<BillListPresenter> implements BillListView, BillFailRollBackView {
    private final int pageSize = 20;
    private StatusView statusView;
    private MySwipeRefreshLayout refreshLayout;
    CustomLoadMoreView loadMoreView;
    private List<BillBean> listItems = new ArrayList<> ();
    private TradeBillListAdapter recordListAdapter;
    private boolean isRefresh = true;
    private boolean isSearch = false;
    private String keyword;
    private BillFailRollBackView billRollBackView;

    private String memberNo;
    private String tradeTypeId;
    private String payYearMonth;
    private String preTradeTypeId,  prePayYearMonth;

    public void setBillRollBackView(BillFailRollBackView billRollBackView) {
        this.billRollBackView = billRollBackView;
    }

    public void updateKeyword(String keyword) {
        if (!Util.isEmpty (keyword)
                && keyword.equals (this.keyword)) {
            return;
        }
        listItems.clear ();
        if (recordListAdapter != null) {
            recordListAdapter.notifyLoadMoreToLoading ();
        }

        this.keyword = keyword;
        this.tradeTypeId = null;
        this.payYearMonth = null;
        loadMore (true);
    }

    public void updateType(String orderTypeId) {
        this.tradeTypeId = orderTypeId;
        this.payYearMonth = null;
        this.keyword = null;
        loadMore (true);
    }

    public void updateYearMonth(String payYearMonth) {
        this.tradeTypeId = null;
        this.keyword = null;
        this.payYearMonth = payYearMonth;
        loadMore (true);
    }

    public void updateAll(){
        this.tradeTypeId = null;
        this.keyword = null;
        this.payYearMonth = null;
        loadMore (true);
    }

    @Override
    protected BillListPresenter createPresenter() {
        return new BillListPresenter ();
    }

    @Override
    protected void initView() {
        statusView = findViewById (R.id.ewallet_pay_base_statusView);
        refreshLayout = findViewById (R.id.ewallet_pay_base_refreshLayout);
        RecyclerView recyclerView = findViewById (R.id.ewallet_pay_base_recyclerView);
        statusView.setContentView (refreshLayout);
        recordListAdapter = new TradeBillListAdapter (getActivity (), listItems);
        recyclerView.setLayoutManager (new LinearLayoutManager (getActivity ()));
        loadMoreView = new CustomLoadMoreView ();
        recordListAdapter.setLoadMoreView (loadMoreView);
        recyclerView.setAdapter (recordListAdapter);
        statusView.setEmpty (R.drawable.ewallet_pay_bill_empty, "没有相关账单信息");
        statusView.dismissAll ();
        statusView.setTryListener (new IReTryListener () {
            @Override
            public void tryAgain() {
                loadMore (true);
            }
        });
        refreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                loadMore (true);
            }
        });
        recordListAdapter.setOnLoadMoreListener (new BaseQuickAdapter.RequestLoadMoreListener () {
            @Override
            public void onLoadMoreRequested() {
                if (refreshLayout.isRefreshing ()) {
                    recordListAdapter.loadMoreComplete ();
                    return;
                }
                loadMore (false);
            }
        }, recyclerView);

        recordListAdapter.setOnItemChildClickListener (new BaseQuickAdapter.OnItemChildClickListener () {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BillBean billBean = listItems.get (position);
                if (view.getId () == R.id.ewallet_pay_month_bill_tv) {
                    int yearMonth[] = DateUtil.getYearMonth (billBean.getHeaderValue ());
                    RouterManager.BalanceBillRouter.gotoBillMonth (getActivity (), memberNo,yearMonth[0], yearMonth[1]);
                } else {
                    String mchOrderNo=billBean.mchOrderNo;
                    String orderNo=billBean.orderNo;
                    String tradeType=billBean.tradeType;
                    RouterManager.BalanceBillRouter.gotoBillDetail(getActivity (), mchOrderNo,orderNo,tradeType);
                }
            }
        });
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_pay_base_recycler_include;
    }

    @Override
    protected void initData(Bundle bundleData) {
        isSearch = bundleData.getBoolean (BundleKey.Trade.key_search_flag);
        memberNo = bundleData.getString (BundleKey.Pay.key_memberNo, UserManager.getInstance ().getMemberNo ());
        recordListAdapter.setNeedShowHeader (!isSearch);
        loadMore (true);
    }

    @Override
    public void billList(List<BillBean> billBeans, boolean hasMore) {
        refreshLayout.setRefreshing (false);
        recordListAdapter.loadMoreComplete ();
        if (isRefresh) {
            listItems.clear ();
        } else {
        }
        String lastMonth = null;
        if (listItems.size () > 0) {
            lastMonth = listItems.get (listItems.size () - 1).getHeaderValue ();
        }
        if (billBeans.size () > 0 && !Util.isEmpty (lastMonth)) {
            BillBean billBean = billBeans.get (0);
            String firstMonth = billBean.getHeaderValue ();
            if (lastMonth.equals (firstMonth)) {
                billBean.isHeader = false;//拼接后去除标记header
                listItems.get (listItems.size () - 1).isLastInIt = false;//拼接后去需要添加分割线
            }
        }
        listItems.addAll (billBeans);
        if (billBeans.size () < pageSize) {
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
    public void billDetail(QueryOrderResp data) {

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
            statusView.showError ();
        }
    }

    void loadMore(boolean isRefresh) {
        if (isSearch) {
            if (Util.isEmpty (keyword)) {
                return;
            }
        }
        this.isRefresh = isRefresh;
        if (listItems.size () == 0) {
            statusView.showLoading ();
        } else if (listItems.size () > 0 && isRefresh) {
            refreshLayout.autoRefresh ();
        }
        String lastOrderNo = null;
        int size = listItems.size ();
        if (isRefresh) {
            lastOrderNo = null;
        } else if (size > 0) {
            lastOrderNo = listItems.get (size - 1).rownum;
        }

        mPresenter.getBillList (memberNo, tradeTypeId,preTradeTypeId, lastOrderNo, pageSize, payYearMonth,prePayYearMonth, keyword);
    }

    // 叼毛 ios 非得要这样，失败了要回到之前的状态
    @Override
    public void rollback(String orderTypeId, String payYearMonth) {
        this.tradeTypeId=  this.preTradeTypeId=orderTypeId;
        this.payYearMonth=this.prePayYearMonth=payYearMonth;
        if (billRollBackView != null) {
            billRollBackView.rollback (orderTypeId, payYearMonth);
        }
    }


}
