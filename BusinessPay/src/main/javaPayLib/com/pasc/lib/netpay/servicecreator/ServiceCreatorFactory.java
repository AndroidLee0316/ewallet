package com.pasc.lib.netpay.servicecreator;

import com.pasc.lib.netpay.ServiceCreator;

/**
 * Created by huanglihou519 on 2018/9/4.
 * 通过 base url 生成不同的服务创建器
 */
public interface ServiceCreatorFactory {
  ServiceCreator get(String url);
}
