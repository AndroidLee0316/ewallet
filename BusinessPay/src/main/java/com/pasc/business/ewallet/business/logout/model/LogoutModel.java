package com.pasc.business.ewallet.business.logout.model;

import com.pasc.business.ewallet.business.common.param.MemberNoParam;
import com.pasc.business.ewallet.business.logout.net.LogoutApi;
import com.pasc.business.ewallet.business.logout.net.LogoutUrl;
import com.pasc.business.ewallet.business.logout.net.param.MemberResetParam;
import com.pasc.business.ewallet.business.logout.net.resp.MemberValidResp;
import com.pasc.business.ewallet.business.common.RespTransformer;
import com.pasc.lib.netpay.ApiGenerator;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.VoidObject;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @date 2019-11-06
 * @des
 * @modify
 **/
public class LogoutModel {

    public static Single<VoidObject> memberReset(String memberNo,String returnCode){
        return ApiGenerator.createApi (LogoutApi.class)
                .memberReset (LogoutUrl.memberReset (),new BaseV2Param<> (new MemberResetParam (memberNo,returnCode)))
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    public static Single<VoidObject> memberCancel(String memberNo){
        return ApiGenerator.createApi (LogoutApi.class)
                .memberCancel (LogoutUrl.memberCancel (),new BaseV2Param<> (new MemberNoParam (memberNo)))
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    public static Single<MemberValidResp> memberCancelValid(String memberNo){
        return ApiGenerator.createApi (LogoutApi.class)
                .memberCancelValid (LogoutUrl.memberCancelValid (),new BaseV2Param<> (new MemberNoParam (memberNo)))
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }
}
