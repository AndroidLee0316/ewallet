package com.pasc.business.ewallet.business.rechargewithdraw.view;

import com.pasc.business.ewallet.base.CommonBaseView;
import com.pasc.business.ewallet.business.rechargewithdraw.net.resp.CalcWithdrawFeeResp;
import com.pasc.business.ewallet.business.rechargewithdraw.net.resp.WithdrawResp;
import com.pasc.business.ewallet.business.bankcard.net.resp.SafeCardBean;
import com.pasc.business.ewallet.business.home.net.QueryBalanceResp;

import java.util.List;

/**
 * @date 2019/8/15
 * @des
 * @modify
 **/
public interface WithdrawView extends CommonBaseView {
    void queryBalanceFeeSuccess(long originWithdraw,CalcWithdrawFeeResp feeResp);

    void queryBalanceFeeFail(String code, String msg);

    void queryListSafeBankSuccess(List<SafeCardBean> safeCardBeans);

    void queryBalanceSuccess(QueryBalanceResp balanceResp);

    void queryListSafeBankError(String code, String msg);


    void withdrawSuccess(WithdrawResp resp,long originWithdraw);
    void withdrawError(String code,String msg);


}
