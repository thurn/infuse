package com.tinlib.infuse;

public interface Initializer<T> {
  public T initialize(Injector injector);
}
