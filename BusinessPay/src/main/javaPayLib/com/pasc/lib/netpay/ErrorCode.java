package com.pasc.lib.netpay;

/**
 * Created by duyuan797 on 16/7/19.
 */
public class ErrorCode {

  public static final int SUCCESS = 200; // 成功
  public static final int SUCCESS_MULTI_STATUS = 207; // Multi-Status
  public static final int SUCCESS_MULTIPLE_CHOICES = 300; // 重定向
  public static final int ERROR = -1; // 错误
  public static final int UNLEGEL_TOKE = 101; // token不合法
  public static final int ERROR_TOKE = 102; // token和当前用户不匹配
  public static final int INVALID_TOKEN = 103; // token失效
  public static final int NULL_TOKEN = 104; // token到期
  public static final int EDGE_OUT = 109; // 被其他设备挤掉
  public static final int AULTH_OUT = 110; // 实名信息绑定到其他账号上了---实名被踢

  public static final int DEFAULT_TOKEN_ERROR_CODE = 101; //默认值  主动校验token失效的情形
}
