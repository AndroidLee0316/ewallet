package com.pasc.business.ewallet.business.bankcard.adapter;

import java.io.Serializable;

/**
 * @date 2019/8/16
 * @des
 * @modify
 **/
public interface IBankCardItem extends Serializable{

    String cardKey();

    boolean isSafeCard();

    String logo();

    String cardNo();

    String bankName();
    String getBankNameAndCard();

    String cardTypeName();

    String bankBackground(); // 背景

    String bankWaterMark(); // 水印

    String userName();

    String singleLimit();

    String singleDayLimit();

}
