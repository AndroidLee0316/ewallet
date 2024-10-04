package com.pasc.business.ewallet.business.pay.presenter;

import android.text.TextUtils;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.common.RespTransformer;
import com.pasc.business.ewallet.business.pay.model.PayModel;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.business.pay.view.QueryOrderView;
import com.pasc.business.ewallet.inner.PayManager;
import com.pasc.lib.netpay.ApiV2Error;
import com.pasc.lib.netpay.ErrorCode;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.netpay.resp.BaseV2Resp;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @date 2019-09-03
 * @des
 * @modify
 **/
public class QueryOrderPresenter extends EwalletBasePresenter<QueryOrderView> {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable ();
    private Disposable delayDisposable;
    //查询支付结果最短时间
    private static final int QUERY_PAY_RESULT_MIN_TIME = 2000;
    //查询支付结果失败最短时间，如果在此时间内，需要重复查询
    private static final int QUERY_PAY_RESULT_FAILED_MIN_TIME = 4000;
    long queryStartTime = 0;

    public void queryOrderStatus(String mchOrderNo,String orderNo, String payType,String tradeType, boolean isThirdPaySuccess, boolean isFirst) {
        if (isFirst) {
            queryStartTime = System.currentTimeMillis ();
        }
        RespTransformer<QueryOrderResp> respTransformer = new RespTransformer<QueryOrderResp> () {
            @Override
            public boolean interceptReturnData(BaseV2Resp<QueryOrderResp> baseResp) {
                boolean notIntercept =
                        (baseResp.data != null && (
                                StatusTable.PayStatus.SUCCESS.equalsIgnoreCase (baseResp.data.status)
                                        || StatusTable.PayStatus.PROCESSING.equalsIgnoreCase (baseResp.data.status)
                                        || StatusTable.PayStatus.UNPAID.equalsIgnoreCase (baseResp.data.status)

                        ));
                if (!notIntercept) {
                    String msg;
                    //支付失败
                    if (baseResp.data != null && !TextUtils.isEmpty(baseResp.data.statusDesc)) {
                        msg = baseResp.data.statusDesc;
                    } else {
                        msg = baseResp.msg;
                    }
                    throw new ApiV2Error (baseResp.code, msg);
//                    throw new ApiV2Error (baseResp.code, baseResp.msg);
                }
                return false;
            }
        };
        if (isFirst) {
            String tip= PayManager.getInstance ().getApplication ().getString (R.string.ewallet_pay_waiting_tip);
            getView ().showLoading (tip);
        }
        Disposable disposable = PayModel.queryOrderDtl (mchOrderNo,orderNo,tradeType, QUERY_PAY_RESULT_FAILED_MIN_TIME)
                .compose (respTransformer)
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ())
                .subscribe (new Consumer<QueryOrderResp> () {
                    @Override
                    public void accept(QueryOrderResp queryOrderResp) {
                        long delay = QUERY_PAY_RESULT_MIN_TIME - (System.currentTimeMillis () - queryStartTime);
                        long delay2 = QUERY_PAY_RESULT_FAILED_MIN_TIME - (System.currentTimeMillis () - queryStartTime);

                        if (StatusTable.PayStatus.SUCCESS.equalsIgnoreCase (queryOrderResp.status)) {
                            //支付成功
                            queryStatusSuccess (queryOrderResp, delay);
                        } else if (StatusTable.PayStatus.UNPAID.equalsIgnoreCase (queryOrderResp.status)
                                || StatusTable.PayStatus.PROCESSING.equalsIgnoreCase (queryOrderResp.status)
                        ) {
                            // 未支付,延迟一秒再去查询
                            if (delay2 > 0) {
                                delayQueryStatus (1000, new IDelayCallback () {
                                    @Override
                                    public void continueSuccess() {
                                        queryOrderStatus (mchOrderNo,orderNo, payType, tradeType,isThirdPaySuccess, false);
                                    }

                                    @Override
                                    public void continueError() {
                                        getView ().dismissLoading ();
                                    }
                                });
                            } else {
                                if (isThirdPaySuccess){
                                    queryStatusSuccess (queryOrderResp, delay);
                                }else {
                                    getView ().dismissLoading ();
                                }
                            }
                        } else {
                            getView ().dismissLoading ();
                            //支付失败, 在 RespTransformer Exception
                        }

                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg, Throwable throwable) {
                        long delay = QUERY_PAY_RESULT_MIN_TIME - (System.currentTimeMillis () - queryStartTime);
                        if (throwable instanceof TimeoutException) {
                            // 超时挑战等待基金到账,并且支付成功的
                            if (isThirdPaySuccess) {
                                QueryOrderResp queryOrderResp = new QueryOrderResp ();
                                queryOrderResp.status = StatusTable.PayStatus.PROCESSING;
                                queryOrderResp.channel = payType;
                                queryStatusSuccess (queryOrderResp, 0);
                            } else {
                                getView ().dismissLoading ();
                                getView ().queryOrderStatusTimeOut ();
                            }
                        } else if (String.valueOf (ErrorCode.ERROR).equals (code)) {
                            getView ().dismissLoading ();
                            //4s 超时 或者其他错误
                            getView ().queryOrderStatusTimeOut ();
                        } else {
                            if (isThirdPaySuccess) {
                                queryStatusError (code, msg, delay);
                            }else {
                                getView ().dismissLoading ();
                            }
                        }
                    }
                });
        compositeDisposable.add (disposable);
    }

    void queryStatusSuccess(QueryOrderResp queryOrderResp, long delay) {
        delayQueryStatus (delay, new IDelayCallback () {
            @Override
            public void continueSuccess() {
                getView ().dismissLoading ();
                getView ().queryOrderStatusSuccess (queryOrderResp);
            }

            @Override
            public void continueError() {
                getView ().dismissLoading ();

            }
        });

    }

    void queryStatusError(String code, String msg, long delay) {
        delayQueryStatus (delay, new IDelayCallback () {
            @Override
            public void continueSuccess() {
                getView ().dismissLoading ();
                getView ().queryOrderStatusError (code, msg);
            }

            @Override
            public void continueError() {
                getView ().dismissLoading ();
            }
        });

    }

    public void delayQueryStatus(long delay, IDelayCallback iDelayCallback) {
        dispose (delayDisposable);
        if (delay <= 100) {
            if (iDelayCallback != null) {
                iDelayCallback.continueSuccess ();
            }
            return;
        }

        delayDisposable = Single.timer (delay, TimeUnit.MILLISECONDS)
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ())
                .subscribe (new Consumer<Long> () {
                    @Override
                    public void accept(Long aLong) {
                        if (iDelayCallback != null) {
                            iDelayCallback.continueSuccess ();
                        }

                    }
                }, new Consumer<Throwable> () {
                    @Override
                    public void accept(Throwable throwable) {
                        if (iDelayCallback != null) {
                            iDelayCallback.continueError ();
                        }
                    }
                });
    }
    public void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed ()) {
            disposable.dispose ();
        }
    }
    public interface IDelayCallback {
        void continueSuccess();

        void continueError();
    }

    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        dispose (delayDisposable);
        delayDisposable=null;
        super.onMvpDetachView (retainInstance);
    }
}
