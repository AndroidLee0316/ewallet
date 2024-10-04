package com.pasc.business.ewallet.business.account.model;

import com.pasc.business.ewallet.business.account.net.CreateAccountApi;
import com.pasc.business.ewallet.business.account.net.CreateAccountUrl;
import com.pasc.business.ewallet.business.account.net.param.SendSmsParam;
import com.pasc.business.ewallet.business.account.net.param.ValidCertParam;
import com.pasc.business.ewallet.business.account.net.param.ValidSmsParam;
import com.pasc.business.ewallet.business.account.net.resp.CheckPwdHasSetResp;
import com.pasc.business.ewallet.business.account.net.resp.MemberStatusResp;
import com.pasc.business.ewallet.business.account.net.resp.QueryMemberResp;
import com.pasc.business.ewallet.business.common.RespTransformer;
import com.pasc.business.ewallet.business.common.SecureBiz;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.business.common.param.MemberNoParam;
import com.pasc.business.ewallet.business.pwd.net.resp.ValidateCodeResp;
import com.pasc.lib.netpay.ApiGenerator;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.VoidObject;
import com.pasc.lib.sm.SM2Utils;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @date 2019/7/23
 * @des
 * @modify
 **/
public class AccountModel {

    /***
     * 检查是否设置密码
     * @param memberNo
     * @return
     */
    public static Single<MemberStatusResp> checkMemberStatus(String memberNo) {
        MemberNoParam memberNoParam = new MemberNoParam (memberNo);
        return ApiGenerator.createApi (CreateAccountApi.class)
                .memberStatus (CreateAccountUrl.memberStatus (),new BaseV2Param<> (memberNoParam))
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());

    }


    /***
     * 检查是否设置密码
     * @param memberNo
     * @return
     */
    public static Single<CheckPwdHasSetResp> checkPwdHasSet(String memberNo) {
        MemberNoParam memberNoParam = new MemberNoParam (memberNo);
        return ApiGenerator.createApi (CreateAccountApi.class)
                .checkPwdHasSet (CreateAccountUrl.checkPayPassword (),new BaseV2Param<> (memberNoParam))
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());

    }

    /***
     * 查询会员信息
     * @param memberNo
     * @return
     */
    public static Single<QueryMemberResp> queryMemberByMemberNo(String memberNo) {
        MemberNoParam memberNoParam = new MemberNoParam (memberNo);
        return ApiGenerator.createApi (CreateAccountApi.class)
                .queryMemberByMemberNo (CreateAccountUrl.queryMemberByMemberNo (),new BaseV2Param<> (memberNoParam))
                .compose (RespTransformer.newInstance ())
                .map (new Function<QueryMemberResp, QueryMemberResp> () {
                    @Override
                    public QueryMemberResp apply(QueryMemberResp memberResp) {
                        UserManager.getInstance ().setPhoneNum (memberResp.phone);
                        UserManager.getInstance ().setName (memberResp.memberName);
                        return memberResp;
                    }
                })
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    /***
     * 身份校验
     * @return
     */
    public static Single<VoidObject> validCert(String memberNo, String certificateNo, String memberName) {
        return SecureBiz.publicKey ().flatMap (new Function<String, SingleSource<? extends VoidObject>> () {
            @Override
            public SingleSource<? extends VoidObject> apply(String publicKey) throws Exception {
                ValidCertParam param = new ValidCertParam (memberNo, SM2Utils.encrypt (certificateNo, publicKey), SM2Utils.encrypt (memberName, publicKey));
                return ApiGenerator.createApi (CreateAccountApi.class)
                        .validCert (CreateAccountUrl.validCert (),new BaseV2Param<> (param))
                        .compose (RespTransformer.newInstance ());
            }
        })
                .subscribeOn (Schedulers.io ());
    }

    /***
     * 发送验证码
     * @return
     */
    public static Single<VoidObject> sendSms(String memberNo, String scene) {
        SendSmsParam param = new SendSmsParam ();
        param.memberNo = memberNo;
        param.scene = scene;
        return ApiGenerator.createApi (CreateAccountApi.class)
                .sendSms (CreateAccountUrl.sendSmsMbr (),new BaseV2Param<> (param))
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    /***
     * 校验验证码
     * @return
     */
    public static Single<ValidateCodeResp> validSms(String memberNo, String smsCode, String scene) {
        ValidSmsParam param = new ValidSmsParam ();
        param.memberNo = memberNo;
        param.smsCode = smsCode;
        param.scene = scene;
        return ApiGenerator.createApi (CreateAccountApi.class)
                .validSms (CreateAccountUrl.validSmsMbr (),new BaseV2Param<> (param))
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }



}
