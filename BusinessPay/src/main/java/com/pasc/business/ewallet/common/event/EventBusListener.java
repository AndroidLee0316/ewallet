package com.pasc.business.ewallet.common.event;

import com.pasc.business.ewallet.NotProguard;

/**
 * @date 2019/8/6
 * @des
 * @modify
 **/
@NotProguard
public interface EventBusListener {
    void handleMessage(BaseEventType eventType);

}
