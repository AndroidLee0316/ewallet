package com.pasc.business.ewallet.business.traderecord.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.pay.net.param.QueryOrderParam;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.business.traderecord.model.TradeRecordModel;
import com.pasc.business.ewallet.business.traderecord.net.param.BillListParam;
import com.pasc.business.ewallet.business.traderecord.net.param.BillListParamYC;
import com.pasc.business.ewallet.business.traderecord.net.resp.BillBean;
import com.pasc.business.ewallet.business.traderecord.net.resp.BillListResp;
import com.pasc.business.ewallet.business.traderecord.view.BillFailRollBackView;
import com.pasc.business.ewallet.business.traderecord.view.BillListView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public class BillListPresenter extends EwalletBasePresenter<BillListView> {
    CompositeDisposable compositeDisposable=new CompositeDisposable ();

    /**
     * @param memberNo    会员号  必须
     * @param tradeTypeId   交易类型；RECHARGE：充值，REFUND：退款；PAY：消费；WITHDRAW：提现 非必须
     * @param lastOrderNo 最后一笔订单的序号（rownum）非必须
     * @param pageSize    条数 必须
     * @param yearOfMonth 年月，例： 2019-01 非必须
     * @param keyWord     搜索关键字 非必须
     */
    public void getBillList(
            String memberNo, String tradeTypeId,String preTradeTypeId,
            String lastOrderNo, int pageSize, String yearOfMonth,String preYearOfMonth, String keyWord) {

        BillListParam param2 = new BillListParam ();
        param2.memberNo = memberNo;
        if (StatusTable.Trade.ALL.equalsIgnoreCase (tradeTypeId)){
            param2.tradeType =null;
        }else {
            param2.tradeType = tradeTypeId;
        }
        param2.lastOrderNo = lastOrderNo;
        param2.limit = pageSize;
        param2.yearOfMonth = yearOfMonth;
        param2.keyWord = keyWord;

       Disposable billListDisposable = TradeRecordModel.getBillList (param2)
                .map (new Function<BillListResp, BillListResp> () {
                    @Override
                    public BillListResp apply(BillListResp billListResp) {
                        List<BillBean> orderList = billListResp.getOrders ();
                        int size = orderList.size ();
                        String preDate = null;
                        for (int i = 0; i < size; i++) {
                            BillBean bean2 = orderList.get (i);
                            if (i == 0) {
                                bean2.isHeader = true;
                                preDate = bean2.getHeaderValue ();
                            } else {
                                String date = bean2.getHeaderValue ();
                                if (!date.equalsIgnoreCase (preDate)) {
                                    bean2.isHeader = true;
                                    preDate = date;
                                }
                            }
                        }
                        return billListResp;
                    }
                })
                .observeOn (AndroidSchedulers.mainThread ())
                .subscribe (new Consumer<BillListResp> () {
                    @Override
                    public void accept(BillListResp billListResp) {
                        getView ().billList (billListResp.getOrders (), billListResp.hasMore ());
                        if (getView () instanceof BillFailRollBackView){
                            ((BillFailRollBackView) getView ()).rollback (tradeTypeId,yearOfMonth);
                        }
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        getView ().tradeError (code, msg);
                        if (getView () instanceof BillFailRollBackView){
                            ((BillFailRollBackView) getView ()).rollback (preTradeTypeId,preYearOfMonth);
                        }
                    }
                });
        compositeDisposable.add (billListDisposable);
    }

    public void getBillDetail( String mchOrderNo, String orderNo,String tradeType) {
        QueryOrderParam param = new QueryOrderParam (mchOrderNo,orderNo,tradeType);
        compositeDisposable.add(TradeRecordModel.getBillDetail (param).subscribe (new Consumer<QueryOrderResp> () {
            @Override
            public void accept(QueryOrderResp s) {
                getView ().billDetail (s);
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String errorCode, String errorMsg) {
                getView ().tradeError (errorCode, errorMsg);
            }
        }));

    }

    public void getBillListYC(
            String memberNo, String tradeTypeId,String preTradeTypeId,
            int pageNum, int pageSize, String yearOfMonth,String preYearOfMonth, String keyWord) {

        BillListParamYC param2 = new BillListParamYC (memberNo);
        if (StatusTable.Trade.ALL.equalsIgnoreCase (tradeTypeId)){
            param2.tradeType =null;
        }else {
            param2.tradeType = tradeTypeId;
        }
        param2.pageNum = pageNum;
        param2.pageSize = pageSize;
        param2.yearOfMonth = yearOfMonth;
        param2.keyWord = keyWord;

        Disposable billListDisposable = TradeRecordModel.getBillListYC (param2)
                .map (new Function<BillListResp, BillListResp> () {
                    @Override
                    public BillListResp apply(BillListResp billListResp) {
                        billListResp.isYc=true;
                        List<BillBean> orderList = billListResp.getOrders ();
                        int size = orderList.size ();
                        String preDate = null;
                        for (int i = 0; i < size; i++) {
                            BillBean bean2 = orderList.get (i);
                            if (i == 0) {
                                bean2.isHeader = true;
                                preDate = bean2.getHeaderValue ();
                            } else {
                                String date = bean2.getHeaderValue ();
                                if (!date.equalsIgnoreCase (preDate)) {
                                    bean2.isHeader = true;
                                    preDate = date;
                                }
                            }
                        }
                        return billListResp;
                    }
                })
                .observeOn (AndroidSchedulers.mainThread ())
                .subscribe (new Consumer<BillListResp> () {
                    @Override
                    public void accept(BillListResp billListResp) {
                        getView ().billList (billListResp.getOrders (), billListResp.hasMore ());
                        if (getView () instanceof BillFailRollBackView){
                            ((BillFailRollBackView) getView ()).rollback (tradeTypeId,yearOfMonth);
                        }
                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg) {
                        getView ().tradeError (code, msg);
                        if (getView () instanceof BillFailRollBackView){
                            ((BillFailRollBackView) getView ()).rollback (preTradeTypeId,preYearOfMonth);
                        }
                    }
                });
        compositeDisposable.add (billListDisposable);
    }


    @Override
    public void onMvpDestroyView() {
        compositeDisposable.clear ();
        super.onMvpDestroyView ();
    }
}
