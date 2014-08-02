package com.tinlib.infuse;

public interface Binder {
  public <T> void bindClass(Class<T> classObject, Initializer<T> initializer);

  public <T> void multibindClass(Class<T> classObject, Initializer<T> initializer);

  public void includeModule(Module module);
}
