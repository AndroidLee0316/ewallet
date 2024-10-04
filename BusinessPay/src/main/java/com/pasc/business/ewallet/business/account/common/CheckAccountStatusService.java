package com.pasc.business.ewallet.business.account.common;

import com.pasc.business.ewallet.business.account.model.AccountModel;
import com.pasc.business.ewallet.business.account.net.resp.CheckPwdHasSetResp;
import com.pasc.business.ewallet.business.account.net.resp.MemberStatusResp;
import com.pasc.business.ewallet.business.account.net.resp.QueryMemberResp;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;
import com.pasc.lib.pay.common.util.ToastUtils;

import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class CheckAccountStatusService {
    private static Disposable disposable;
    public static void checkMember(String merchantNo, CheckAccountListener listener) {

        if (disposable != null && !disposable.isDisposed ()) {
            try {
                disposable.dispose ();
            }catch (Exception e){
                e.printStackTrace ();
            }
            disposable = null;
        }
        disposable = AccountModel.checkPwdHasSet (merchantNo).subscribe (new Consumer<CheckPwdHasSetResp> () {
            @Override
            public void accept(CheckPwdHasSetResp checkPwdHasSetResp) {
                if (listener!=null){
                    listener.onSuccess (checkPwdHasSetResp);
                }
                disposable=null;
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                if (listener!=null){
                    listener.onFail (code,msg);
                }
                disposable=null;

            }
        });


    }

    public static void checkMemberStatus(String merchantNo, CheckAccountListener listener){
        if (disposable != null && !disposable.isDisposed ()) {
            try {
                disposable.dispose ();
            }catch (Exception e){
                e.printStackTrace ();
            }
            disposable = null;
        }
        disposable = AccountModel.checkMemberStatus (merchantNo).subscribe (new Consumer<MemberStatusResp> () {
            @Override
            public void accept(MemberStatusResp memberStatusResp) {
                if (listener!=null){
                    listener.onSuccess (memberStatusResp);
                }
                disposable=null;
            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                if (listener!=null){
                    listener.onFail (code,msg);
                }
                disposable=null;

            }
        });
    }

}
