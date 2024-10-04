package com.pasc.ewallet.dev.pay.unionpay.view;

import com.pasc.business.ewallet.base.CommonBaseView;
import com.pasc.ewallet.dev.pay.unionpay.model.BankCard;
import java.util.List;

/**
 * @date 2021/03/09
 * @des
 * @modify
 **/
public interface QueryBankCardListView extends CommonBaseView {
    void queryBankCardSuccess(List<BankCard> bankCardList);
    void queryBankCardError(String code, String msg);

}
