package com.pasc.lib.netpay;

import android.support.annotation.NonNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangwen881 on 17/2/24.
 */

public class HttpCommonParams {
  private final Map<String, String> PARAMS = new HashMap<>();
  private final Map<String, String> HEADERS = new HashMap<>();
  private InjectCommonHeadersHandler injectHandler;

  private HttpCommonParams() {
  }

  private static class SingletonHolder {
    private static final HttpCommonParams INSTANCE = new HttpCommonParams();
  }

  public static HttpCommonParams getInstance() {
    return SingletonHolder.INSTANCE;
  }

  /**
   * get params
   *
   * @return common params
   */
  public Map<String, String> getParams() {
    return PARAMS;
  }

  /**
   * addParam params to common params
   *
   * @param key params key
   * @param value params value
   * @return common params
   */
  public Map<String, String> addParam(@NonNull String key, @NonNull String value) {
    PARAMS.put(key, value);
    return PARAMS;
  }

  /**
   * clearParam params
   */
  public void clearParam() {
    PARAMS.clear();
  }

  /**
   * put header
   */
  public Map<String, String> addHeader(@NonNull String key, @NonNull String value) {
    HEADERS.put(key, value);
    return HEADERS;
  }

  public Map<String, String> addHeaders(Map<String, String> headers) {
    HEADERS.putAll(headers);
    return HEADERS;
  }

  public void setInjectHandler(InjectCommonHeadersHandler injectHandler) {
    this.injectHandler = injectHandler;
  }

  /**
   * get headers
   */
  public Map<String, String> getHeaders() {
    if (injectHandler != null) {
      injectHandler.onInjectCommonHeaders(HEADERS);
    }
    return HEADERS;
  }

  /**
   * clear headers
   */
  public void clearHeaders() {
    HEADERS.clear();
  }

  public static class Builder {

    public Builder addParam(String key, String value) {
      HttpCommonParams.getInstance().addParam(key, value);
      return this;
    }

    public Builder addHeader(String key, String value) {
      HttpCommonParams.getInstance().addHeader(key, value);
      return this;
    }

    public HttpCommonParams build() {
      return HttpCommonParams.getInstance();
    }
  }

  public interface InjectCommonHeadersHandler {
    void onInjectCommonHeaders(Map<String, String> headers);
  }
}