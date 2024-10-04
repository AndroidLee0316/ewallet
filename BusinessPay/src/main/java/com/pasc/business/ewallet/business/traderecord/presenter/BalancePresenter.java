package com.pasc.business.ewallet.business.traderecord.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.traderecord.net.resp.AvailBalanceBean;
import com.pasc.business.ewallet.business.traderecord.net.resp.AvailBalanceListResp;
import com.pasc.business.ewallet.business.traderecord.model.TradeRecordModel;
import com.pasc.business.ewallet.business.traderecord.net.param.AvailBalanceListParam;
import com.pasc.business.ewallet.business.traderecord.view.BalanceView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public class BalancePresenter extends EwalletBasePresenter<BalanceView> {
    private final CompositeDisposable disposables = new CompositeDisposable ();

    public void getAvailBalanceList(String accessUserId,String inChannelId,int pageNum,int pageSize){
        AvailBalanceListParam param=new AvailBalanceListParam (accessUserId,inChannelId,pageNum,pageSize);
        Disposable disposable= TradeRecordModel.getAvailBalanceList (param).subscribe (new Consumer<AvailBalanceListResp> () {
            @Override
            public void accept(AvailBalanceListResp availBalanceListResponse) {
                if (availBalanceListResponse.balanceList==null){
                    availBalanceListResponse.balanceList=new ArrayList<> ();
                }else {
                    List<AvailBalanceBean> balanceList = availBalanceListResponse.balanceList;
                    if (balanceList!=null && balanceList.size ()>0) {
                        balanceList.get (0).isHeader = true;
                        balanceList.get (0).isLastInIt = true;
                        balanceList.get (0).headerValue = balanceList.get (0).getYearAndMonth ();
                        for (int i = 1; i < balanceList.size (); i++) {
                            balanceList.get (i).headerValue = balanceList.get (i).getYearAndMonth ();
                            if (balanceList.get (i - 1).headerValue.equals (balanceList.get (i).headerValue)) {
                                //同一个月
                                balanceList.get (i - 1).isLastInIt = false;
                                balanceList.get (i).isLastInIt = true;
                            } else {
                                //不同一个月
                                balanceList.get (i - 1).isLastInIt = true;
                                balanceList.get (i).isLastInIt = true;
                                balanceList.get (i).isHeader = true;
                            }
                        }
                    }
                }
                getView ().availBalanceList (availBalanceListResponse.balanceList);
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String errorCode, String errorMsg) {
                getView ().tradeError (errorCode,errorMsg);
            }
        });
        disposables.add (disposable);
    }


    @Override
    public void onMvpDetachView(boolean retainInstance) {
        disposables.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
