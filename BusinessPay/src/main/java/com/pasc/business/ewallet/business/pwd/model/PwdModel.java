package com.pasc.business.ewallet.business.pwd.model;

import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.business.common.RespTransformer;
import com.pasc.business.ewallet.business.common.SecureBiz;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.business.common.param.JihuoParam;
import com.pasc.business.ewallet.business.pwd.net.PwdApi;
import com.pasc.business.ewallet.business.pwd.net.PwdUrl;
import com.pasc.business.ewallet.business.pwd.net.param.SetPwdParam;
import com.pasc.business.ewallet.business.pwd.net.param.ValidPwdParam;
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
 * @date 2019/7/24
 * @des
 * @modify
 **/
@NotProguard
public class PwdModel {

    public static Single<VoidObject> addCert(String memberNo, String certificateNo, String memberName) {
        return   SecureBiz.publicKey ().flatMap (new Function<String, SingleSource<VoidObject>> () {
            @Override
            public SingleSource<VoidObject> apply(String publicKey) throws Exception {
                JihuoParam param = new JihuoParam (memberNo, SM2Utils.encrypt (certificateNo,publicKey), SM2Utils.encrypt (memberName,publicKey));
                return ApiGenerator.createApi (PwdApi.class).addCert (PwdUrl.add_cert (),new BaseV2Param<> (param))
                        .subscribeOn (Schedulers.io ())
                        .compose (RespTransformer.newInstance ());
            }
        }).observeOn (AndroidSchedulers.mainThread ());

    }


    /***
     * 设置密码
     * @param validateCode
     * @param password
     * @return
     */
    public static Single<VoidObject> setPassword(String validateCode, String password) {
        SetPwdParam setPwdParam = new SetPwdParam (UserManager.getInstance ().getMemberNo (), password, validateCode);
        return ApiGenerator.createApi (PwdApi.class)
                .setPassWord (PwdUrl.set_password (),new BaseV2Param<> (setPwdParam))
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }

    /***
     * 校验密码
     * @param confirmPassword
     * @return
     */
    public static Single<ValidateCodeResp> verifyPassword(String confirmPassword) {
        ValidPwdParam param = new ValidPwdParam (UserManager.getInstance ().getMemberNo (), confirmPassword);
        return ApiGenerator.createApi (PwdApi.class)
                .validPwd (PwdUrl.valid_pwd (),new BaseV2Param<> (param))
                .compose (RespTransformer.newInstance ())
                .subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ());
    }
}
