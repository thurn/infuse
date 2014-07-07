package ca.thurn.infuse;

import java.util.HashMap;
import java.util.Map;

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

  public abstract void configure();

  @SuppressWarnings("unchecked")
  <T> T runInitializer(Injector injector, Class<T> key) {
    try {
      return (T) initializers.get(key).getResult(injector);
    } catch (NullPointerException exception) {
      throw new IllegalArgumentException("No instance bound for key '" + key + "'");
    } catch (ClassCastException exception) {
      throw new IllegalStateException("Initializer for key '" + key +
          "' did not return an instance of the correct type.\n" + exception.getMessage());
    }
  }

  public void includeModule(Module module) {
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

  public final <T> void bindSingletonKey(Class<T> key, Initializer<T> initializer) {
    checkForDuplicate(key);
    initializers.put(key, new CachingInitializer<T>(initializer, true /* isSingleton */));
  }

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
