package com.pasc.business.ewallet.business.pwd.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.pwd.view.BaseCountDownView;
import com.pasc.business.ewallet.common.utils.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @date 2019/7/25
 * @des
 * @modify
 **/
public class BaseCountDownPresenter extends EwalletBasePresenter<BaseCountDownView> {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable ();

    protected Disposable countDownDispose;
    private  int maxNum=60;
    public BaseCountDownPresenter(int maxNum){
        this.maxNum=maxNum;
    }
    public void countDownStart() {
        if (isRunning ()){
            return;
        }
        getView ().showElapseTime (maxNum-1);
        countDownDispose = Observable
                .interval (1, TimeUnit.SECONDS)
                //取消任务时取消定时唤醒
                .doOnDispose (new Action () {
                    @Override
                    public void run() {
                    }
                })
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ())
                .subscribe (new Consumer<Long> () {
                    @Override
                    public void accept(Long count) {
                        int value=count.intValue ();
                        int lessValue=maxNum-value-2;
                        getView ().showElapseTime (lessValue);
                        LogUtil.loge ("lessValue ="+lessValue +", count ="+count);
                        if (lessValue<=0){
                            countDownCancel();
                        }
                    }
                }, new Consumer<Throwable> () {
                    @Override
                    public void accept(Throwable throwable) {
                        getView ().showElapseTimeUp ();
                    }
                });

    }
    protected boolean isRunning(){
        return countDownDispose != null && !countDownDispose.isDisposed ();
    }
    protected void countDownCancel(){
        if (isRunning ()) {
            countDownDispose.dispose ();
        }
        countDownDispose=null;
        getView ().showElapseTimeUp ();

    }

    @Override
    public void onMvpDetachView(boolean retainInstance) {
        countDownCancel ();
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
