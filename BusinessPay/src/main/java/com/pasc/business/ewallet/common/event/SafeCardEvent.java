package com.pasc.business.ewallet.common.event;

import com.pasc.business.ewallet.business.bankcard.net.resp.QuickCardBean;

import java.util.List;

/**
 * @date 2019/8/20
 * @des
 * @modify
 **/
public class SafeCardEvent implements BaseEventType{
    public List<QuickCardBean> list;

    public SafeCardEvent(List<QuickCardBean> list) {
        this.list = list;
    }
}
