package ca.thurn.infuse;

/**
 * A function which describes how to obtain a value. Typically, this function
 * will be used by a {@link Module} to register an instance with a given key.
 */
public interface Initializer<T> {
  /**
   * Creates a value of the type associated with this initializer. Typically
   * this will involve allocating a new object, but this is not required. For
   * example, an Initializer might return values from an object cache or return
   * the same instance every time for an immutable singleton object (although
   * clients should employ {@link Module#bindSingletonKey(Class, Initializer)}
   * in that case).
   *
   * @param injector An {@link Injector} to use to retrieve dependencies of
   *     the instance under construction.
   * @return The desired instance.
   */
  public T initialize(Injector injector);
}
