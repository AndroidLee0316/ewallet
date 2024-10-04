package com.pasc.business.ewallet.common.event;

import com.pasc.business.ewallet.NotProguard;

/**
 * Created by zhuangjiguang on 2021/3/17.
 */
@NotProguard
public class OpenUnifyEvent implements BaseEventType {
  private String openStatus;

  public OpenUnifyEvent(String openStatus) {
    this.openStatus = openStatus;
  }
}
