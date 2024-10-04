package com.pasc.business.ewallet.business.pay.view;

/**
 * @date 2019/8/7
 * @des
 * @modify
 **/
public interface PayCallbackView {
    void payCancel();

    void payError(String msg);

    String getMerchantName();
    long getOrderAmount();

    String getBankCardName();
    void setBankCardName(String bankCardName);

    String getPayTypeName();
    void setPayTypeName(String payTypeName);

    String getPayType();
    void setPayType(String payType);

    String getChannel();
    void setChannel(String channel);

    String getCardKey();
    void setCardKey(String cardKey);

    String getQuickCardPhone();
    void setQuickCardPhone(String bindPhone);

    String getUnionOrderId();
    void setUnionOrderId(String unionOrderId);

    String getPayDate();
    void setPayDate(String payDate);

    void closePayActivity(boolean needFinishActivity);

    void closePayActivityDelay();

    void finishPayActivity();

    void switchToPage(int position);
}
