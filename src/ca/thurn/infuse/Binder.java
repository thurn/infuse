package ca.thurn.infuse;

public interface Binder {
  public void bindKey(String key, Initializer initializer);

  public void bindSingletonKey(String key, Initializer initializer);

  public void includeModule(Module module);
}
