package com.pasc.business.ewallet.business.home.net;

import com.pasc.business.ewallet.business.common.CommonUrl;

/**
 * @date 2019-11-06
 * @des
 * @modify
 **/
public class HomeUrl {

    public static String query_balance(){
        return CommonUrl.getHostAndGateWay ()+CommonUrl.URL_PREFIX + "/query_balance";

    }
}
