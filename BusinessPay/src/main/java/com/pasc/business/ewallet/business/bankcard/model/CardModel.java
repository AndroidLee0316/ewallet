package com.pasc.business.ewallet.business.bankcard.model;

import com.pasc.business.ewallet.business.bankcard.net.CardApi;
import com.pasc.business.ewallet.business.bankcard.net.CardUrl;
import com.pasc.business.ewallet.business.bankcard.net.param.BindCardParam;
import com.pasc.business.ewallet.business.bankcard.net.param.QuickPaySendMsgParam;
import com.pasc.business.ewallet.business.bankcard.net.param.UnBindQuickCardParam;
import com.pasc.business.ewallet.business.bankcard.net.param.ValidBindCardParam;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickCardBean;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickCardResp;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickPaySendMsgResp;
import com.pasc.business.ewallet.business.bankcard.net.resp.SafeCardBean;
import com.pasc.business.ewallet.business.common.param.MemberNoParam;
import com.pasc.business.ewallet.business.common.SecureBiz;
import com.pasc.lib.netpay.ApiGenerator;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.VoidObject;
import com.pasc.lib.netpay.transform.RespV2Transformer;
import com.pasc.lib.sm.SM2Utils;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @date 2019/8/16
 * @des
 * @modify
 **/
public class CardModel {
    public static Single<List<SafeCardBean>> listSafeBank(String memberNo) {
        return ApiGenerator.createApi (CardApi.class)
                .listSafeBank (CardUrl.list_safe_bank (),new BaseV2Param<> (new MemberNoParam (memberNo)))
                .compose (RespV2Transformer.newInstance ())
                .subscribeOn (Schedulers.io ());
    }

    public static Single<List<QuickCardBean>> listQuickBank(String memberNo) {
        return ApiGenerator.createApi (CardApi.class)
                .listQuickBank (CardUrl.list_quick_bank (),new BaseV2Param<> (new MemberNoParam (memberNo)))
                .compose (RespV2Transformer.newInstance ())
                .subscribeOn (Schedulers.io ());
    }

    public static Single<VoidObject> bindCard(String memberNo, String bankAcctNo, String mobile) {
        return SecureBiz.publicKey ().flatMap (new Function<String, SingleSource<VoidObject>> () {
            @Override
            public SingleSource<VoidObject> apply(String publicKey) throws Exception {
                BindCardParam bindCardParam = new BindCardParam (memberNo, SM2Utils.encrypt (bankAcctNo, publicKey), SM2Utils.encrypt (mobile, publicKey));
                return ApiGenerator.createApi (CardApi.class)
                        .bindCard (CardUrl.bind_card_mbr (),new BaseV2Param<> (bindCardParam))
                        .compose (RespV2Transformer.newInstance ());
            }
        }).subscribeOn (Schedulers.io ()).observeOn (AndroidSchedulers.mainThread ());

    }

    public static Single<QuickCardResp> bindCardValid(String memberNo, String bankAcctNo, String smsCode) {
        return SecureBiz.publicKey ().flatMap (new Function<String, SingleSource<? extends QuickCardResp>> () {
            @Override
            public SingleSource<? extends QuickCardResp> apply(String publicKey) throws Exception {
                ValidBindCardParam validBindCardParam = new ValidBindCardParam (memberNo, SM2Utils.encrypt (bankAcctNo, publicKey), smsCode);
                return ApiGenerator.createApi (CardApi.class)
                        .bindCardValid (CardUrl.bind_card_valid_mbr (),new BaseV2Param<> (validBindCardParam))
                        .compose (RespV2Transformer.newInstance ());
            }
        }).subscribeOn (Schedulers.io ()).observeOn (AndroidSchedulers.mainThread ());
    }

    public static Single<String> jumpBindCardPage(String memberNo) {
        return ApiGenerator.createApi (CardApi.class)
                .jumpBindCardPage (CardUrl.jumpBindCardPage (),new BaseV2Param<> (new MemberNoParam (memberNo)))
                .compose (RespV2Transformer.newInstance ())
                .subscribeOn (Schedulers.io ()).observeOn (AndroidSchedulers.mainThread ());
    }

    public static Single<QuickPaySendMsgResp> quickPaySendMsg(String cardKey, String mchOrderNo, String tradeType, long amount) {
        QuickPaySendMsgParam quickPaySendMsgParam = new QuickPaySendMsgParam (cardKey, mchOrderNo, tradeType, amount);
        return ApiGenerator.createApi (CardApi.class)
                .quickPaySendMsg (CardUrl.quick_pay_sendMsg (),new BaseV2Param<> (quickPaySendMsgParam))
                .compose (RespV2Transformer.newInstance ())
                .subscribeOn (Schedulers.io ()).observeOn (AndroidSchedulers.mainThread ());
    }

    public static Single<VoidObject> unBindQuickCard(String cardKey,String memberNo){
        UnBindQuickCardParam param=new UnBindQuickCardParam (memberNo,cardKey);
        return ApiGenerator.createApi (CardApi.class)
                .unbindCard (CardUrl.unbindCard (),new BaseV2Param<> (param))
                .compose (RespV2Transformer.newInstance ())
                .subscribeOn (Schedulers.io ()).observeOn (AndroidSchedulers.mainThread ());
    }
}
