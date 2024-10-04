package com.pasc.business.ewallet.business.bankcard.net.param;

import com.google.gson.annotations.SerializedName;
import com.pasc.business.ewallet.business.common.param.MemberNoParam;

/**
 * @date 2019-08-28
 * @des
 * @modify
 **/
public class UnBindQuickCardParam extends MemberNoParam {
    @SerializedName ("cardKey")
    public String cardKey;

    public UnBindQuickCardParam(String memberNo, String cardKey) {
        super (memberNo);
        this.cardKey = cardKey;
    }
}
