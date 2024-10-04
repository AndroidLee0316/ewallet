package com.pasc.business.ewallet.business.pay.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.pay.model.PayModel;
import com.pasc.business.ewallet.business.pay.net.resp.SignStatusResp;
import com.pasc.business.ewallet.business.pay.view.QuerySignStatusView;
import com.pasc.lib.netpay.ErrorCode;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @date 2019/12/15
 * @des
 * @modify
 **/
public class QuerySignStatusPresenter extends EwalletBasePresenter<QuerySignStatusView> {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable ();
    private Disposable delayDisposable;
    //查询支付结果最短时间
    private static final int QUERY_PAY_RESULT_MIN_TIME = 2000;
    //查询支付结果失败最短时间，如果在此时间内，需要重复查询
    private static final int QUERY_PAY_RESULT_FAILED_MIN_TIME = 4000;
    long queryStartTime = 0;

    public void querySignStatus(String memberNo, String channel, String sceneId, boolean isThirdPaySuccess, boolean isFirst) {
        if (isFirst) {
            queryStartTime = System.currentTimeMillis ();
        }
//        RespTransformer<SignStatusResp> respTransformer = new RespTransformer<SignStatusResp> () {
//            @Override
//            public boolean interceptReturnData(BaseV2Resp<SignStatusResp> baseResp) {
//                boolean notIntercept =
//                        (baseResp.data != null && (
//                                StatusTable.PayStatus.SUCCESS.equalsIgnoreCase (baseResp.data.getStatus())
//                                        || StatusTable.PayStatus.PROCESSING.equalsIgnoreCase (baseResp.data.status)
//                                        || StatusTable.PayStatus.UNPAID.equalsIgnoreCase (baseResp.data.status)
//
//                        ));
//                if (!notIntercept) {
//                    //支付失败
//                    throw new ApiV2Error (baseResp.code, baseResp.data!=null?baseResp.data.statusDesc:"");
////                    throw new ApiV2Error (baseResp.code, baseResp.msg);
//                }
//                return false;
//            }
//        };
        if (isFirst) {
            //String tip= PayManager.getInstance ().getApplication ().getString (R.string.ewallet_pay_waiting_tip);
            getView ().showLoading ("");
        }
        Disposable disposable = PayModel.querySignStatus (memberNo, channel, sceneId, QUERY_PAY_RESULT_FAILED_MIN_TIME)
                //.compose (respTransformer)
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ())
                .subscribe (new Consumer<SignStatusResp> () {
                    @Override
                    public void accept(SignStatusResp signStatusResp) {
                        long delay = QUERY_PAY_RESULT_MIN_TIME - (System.currentTimeMillis () - queryStartTime);
                        long delay2 = QUERY_PAY_RESULT_FAILED_MIN_TIME - (System.currentTimeMillis () - queryStartTime);

                        if (signStatusResp.hasSign()) {
                            //已签约
                            queryStatusSuccess(signStatusResp, delay);
                        } else {
                            // 未签约,延迟一秒再去查询
                            if (delay2 > 0) {
                                delayQueryStatus (1000, new IDelayCallback () {
                                    @Override
                                    public void continueSuccess() {
                                        querySignStatus (memberNo, channel, sceneId, isThirdPaySuccess, false);
                                    }

                                    @Override
                                    public void continueError() {
                                        getView ().dismissLoading ();
                                    }
                                });
                            } else {
                                if (isThirdPaySuccess) {
                                    queryStatusSuccess(signStatusResp, delay);
                                } else {
                                    getView ().queryNoSignStatusError ();
                                    getView().dismissLoading();
                                }
                            }
                        }

                    }
                }, new BaseRespThrowableObserver () {
                    @Override
                    public void onV2Error(String code, String msg, Throwable throwable) {
                        long delay = QUERY_PAY_RESULT_MIN_TIME - (System.currentTimeMillis () - queryStartTime);
                        if (throwable instanceof TimeoutException) {
                            // 超时挑战等待基金到账,并且支付成功的
                            if (isThirdPaySuccess) {
                                SignStatusResp signStatusResp = new SignStatusResp ();
                                signStatusResp.updateHasSign ();
                                queryStatusSuccess (signStatusResp, 0);
                            } else {
                                getView().dismissLoading ();
                                getView().querySignStatusTimeOut ();
                            }
                        } else if (String.valueOf (ErrorCode.ERROR).equals (code)) {
                            getView().dismissLoading ();
                            //4s 超时 或者其他错误
                            getView().querySignStatusTimeOut ();
                        } else {
                            if (isThirdPaySuccess) {
                                SignStatusResp signStatusResp = new SignStatusResp ();
                                signStatusResp.updateHasSign ();
                                queryStatusSuccess (signStatusResp, 0);
                            }else {
                                getView ().querySignStatusError (code, msg);
                                getView().dismissLoading ();
                            }
                        }
                    }
                });
        compositeDisposable.add (disposable);
    }

    void queryStatusSuccess(SignStatusResp signStatusResp, long delay) {
        delayQueryStatus (delay, new IDelayCallback () {
            @Override
            public void continueSuccess() {
                getView ().dismissLoading ();
                getView ().querySignStatusSuccess (signStatusResp);
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
                getView ().querySignStatusError (code, msg);
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
