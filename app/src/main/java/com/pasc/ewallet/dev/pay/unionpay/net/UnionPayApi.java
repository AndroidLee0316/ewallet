package com.pasc.ewallet.dev.pay.unionpay.net;

import com.pasc.ewallet.dev.pay.unionpay.model.BankCard;
import com.pasc.ewallet.dev.pay.unionpay.model.BankCardOpenStatus;
import com.pasc.ewallet.dev.pay.unionpay.model.param.BankCardListParam;
import com.pasc.ewallet.dev.pay.unionpay.model.param.BankCardOpenStatusParam;
import com.pasc.ewallet.dev.pay.unionpay.model.param.SendSmsParam;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.netpay.resp.BaseV2Resp;
import com.pasc.lib.netpay.resp.VoidObject;
import io.reactivex.Single;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by zhuangjiguang on 2021/3/9.
 */
public interface UnionPayApi {
  @POST
  Single<BaseV2Resp<List<BankCard>>> queryBankCardList(@Url String url, @Body
      BaseV2Param<BankCardListParam> param);

  @POST
  Single<BaseV2Resp<BankCardOpenStatus>> checkOpenStatus(@Url String url, @Body
      BaseV2Param<BankCardOpenStatusParam> param);

  @POST
  Single<BaseV2Resp<VoidObject>> sendSmsVerifyCode(@Url String url, @Body
      BaseV2Param<SendSmsParam> param);
}
