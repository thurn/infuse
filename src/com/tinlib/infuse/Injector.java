package com.tinlib.infuse;

import com.google.common.collect.ImmutableSet;

public interface Injector {
  public <T> T get(Class<T> classObject);

  public <T> ImmutableSet<T> getMultiple(Class<T> classObject);
}
