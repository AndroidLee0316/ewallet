package com.pasc.ewallet.dev.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhuangjiguang on 2020/12/17.
 */
public class MiniProgramResp {

  /**
   * path : pages/payment/result?mchOrderNo=jzb35zykmwlsh014512&memberNo=8892009010713
   * appId : wx3e05d6081d9957de
   * skipMiniprogramAppId : gh_4b63a4815c55
   * skipAppId : wxdbfe04b195afaff5
   * miniprogramType : 1
   */

  @SerializedName("path")
  private String path;
  @SerializedName("appId")
  private String appId;
  @SerializedName("skipMiniprogramAppId")
  private String skipMiniprogramAppId;
  @SerializedName("skipAppId")
  private String skipAppId;
  @SerializedName("miniprogramType")
  private int miniprogramType;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getSkipMiniprogramAppId() {
    return skipMiniprogramAppId;
  }

  public void setSkipMiniprogramAppId(String skipMiniprogramAppId) {
    this.skipMiniprogramAppId = skipMiniprogramAppId;
  }

  public String getSkipAppId() {
    return skipAppId;
  }

  public void setSkipAppId(String skipAppId) {
    this.skipAppId = skipAppId;
  }

  public int getMiniprogramType() {
    return miniprogramType;
  }

  public void setMiniprogramType(int miniprogramType) {
    this.miniprogramType = miniprogramType;
  }
}
