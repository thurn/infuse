package ca.thurn.infuse;

import java.util.HashMap;
import java.util.Map;

/**
 * The base class which you need to extend from in order to bind keys to
 * initializers for your application. Only one initializer should be supplied
 * for each key.
 *
 * <p>Example module:</p>
 *
 * <pre>{@code
 * final class PumpModule extends Module {
 *   @Override public void configure() {
 *     bindKey(Pump.class, new Initializer<Pump>() {
 *       @Override public Pump initialize(Injector injector) {
 *         return new Thermosiphon(injector);
 *       }
 *     });
 *   }
 * }
 * }</pre>
 *
 * <p>This is an example of a module which binds a concrete value
 * (Thermosiphon) to an interface (Pump). A new Thermosiphon will be created
 * every time a Pump is requested.</p>
 */
public abstract class Module {
  private static final class CachingInitializer<T> {
    private final Initializer<T> initializer;
    private final boolean isSingleton;
    private T cached = null;

    private CachingInitializer(Initializer<T> initializer, boolean isSingleton) {
      this.initializer = initializer;
      this.isSingleton = isSingleton;
    }

    private T getResult(Injector injector) {
      if (isSingleton) {
        if (cached == null) {
          cached = initializer.initialize(injector);
        }
        return cached;
      } else {
        return initializer.initialize(injector);
      }
    }
  }

  private final Map<Class<?>, CachingInitializer<?>> initializers =
      new HashMap<Class<?>, CachingInitializer<?>>();

  /**
   * Binds keys to values for later retrieval by an {@link Injector}. Keys
   * should be associated with an initializer function via
   * {@link Module#bindKey(Class, Initializer)} and related methods. Modules
   * can also import all of the bindings defined in another module by calling
   * {@link Module#includeModule(Module)}.
   */
  public abstract void configure();

  @SuppressWarnings("unchecked")
  <T> T runInitializer(Injector injector, Class<T> key) {
    try {
      // This unchecked cast should be safe because it's only possible to bind
      // a key of type T to an initializer of type T in bindKey.
      return (T) initializers.get(key).getResult(injector);
    } catch (NullPointerException exception) {
      throw new IllegalArgumentException("No instance bound for key '" + key + "'");
    }
  }

  /**
   * Merges all of the bindings defined in another module into this module.
   * This will invoke the other module's {@link Module#configure()} method.
   *
   * @param module The module to configure and then merge bindings from.
   * @throws IllegalStateException If this module defines a key which a binding
   *     has already been configured for.
   */
  public final void includeModule(Module module) {
    module.configure();
    for (Class<?> key : module.initializers.keySet()) {
      if (initializers.containsKey(key)) {
        throw new IllegalStateException("Module '" + module +
            "' supplied a duplicate key '" + key + "'");
      } else {
        initializers.put(key, module.initializers.get(key));
      }
    }
  }

  /**
   * Similar to {@link Module#bindKey(Class, Initializer)}, except the
   * initializer function will only be invoked once. Subsequent calls to the
   * {@link Injector#get(Class)} method will return the same value.
   *
   * @see Module#bindKey(Class, Initializer)
   */
  public final <T> void bindSingletonKey(Class<T> key, Initializer<T> initializer) {
    checkForDuplicate(key);
    initializers.put(key, new CachingInitializer<T>(initializer, true /* isSingleton */));
  }

  /**
   * Associates a key with an initializer function. This initializer function
   * will be invoked every time a value is requested for the provided key via
   * {@link Injector#get(Class)}.
   *
   * @param key The key to bind.
   * @param initializer An initializer function which will be invoked every
   *     time a value is requested for the provided key.
   *  @throws IllegalStateException If an initializer has already been bound to
   *      this key.
   */
  public final <T> void bindKey(Class<T> key, Initializer<T> initializer) {
    checkForDuplicate(key);
    initializers.put(key, new CachingInitializer<T>(initializer, false /* isSingleton */));
  }

  private <T> void checkForDuplicate(Class<T> key) {
    if (initializers.containsKey(key)) {
      throw new IllegalStateException("Attempted to bind duplicate key '" + key + "'");
    }
  }
}
