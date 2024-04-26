package com.olikproject.olik.utils;

import java.util.function.Consumer;

public class NullUtil {
    public static <T> void updateIfPresent(Consumer<T> consumer, T value) {
    if (value != null) {
      consumer.accept(value);
    }
  }
}
