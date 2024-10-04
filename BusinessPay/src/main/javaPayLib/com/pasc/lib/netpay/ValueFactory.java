package com.pasc.lib.netpay;

/**
 * Created by huanglihou519 on 2018/9/2.
 */
public final class ValueFactory<T> implements Factory {
  public static <T> ValueFactory<T> of(T instance) {
    return new ValueFactory<>(instance);
  }

  private final T value;

  private ValueFactory(T value) {
    this.value = value;
  }

  @Override
  public T get() {
    return value;
  }
}
