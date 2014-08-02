package com.tinlib.infuse;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Initializers {
  private Initializers() {}

  public static <T> Initializer<T> returnValue(final T object) {
    checkNotNull(object);
    return new Initializer<T>() {
      @Override
      public T initialize(Injector injector) {
        return object;
      }
    };
  }
}
