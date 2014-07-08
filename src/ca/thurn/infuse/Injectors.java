package ca.thurn.infuse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Injectors {
  private static class CachingInitializer implements Initializer {
    private final Initializer initializer;
    private Object cached = null;

    private CachingInitializer(Initializer initializer) {
      this.initializer = initializer;
    }

    @Override
    public Object initialize(Injector injector) {
      if (cached == null) {
        cached = initializer.initialize(injector);
      }
      return cached;
    }
  }

  private static class InjectorImpl implements Binder, Injector {
    private final Map<String, Initializer> initializers = new HashMap<String, Initializer>();

    private InjectorImpl(List<Module> modules) {
      for (Module module : modules) {
        module.configure(this);
      }
    }

    @Override
    public void bindKey(String key, Initializer initializer) {
      checkForDuplicate(key);
      initializers.put(key, initializer);
    }

    @Override
    public void bindSingletonKey(String key, Initializer initializer) {
      initializers.put(key, new CachingInitializer(initializer));
    }

    @Override
    public void includeModule(Module module) {
      module.configure(this);
    }

    private void checkForDuplicate(String key) {
      if (initializers.containsKey(key)) {
        throw new IllegalStateException("Attempted to bind duplicate key '" + key + "'");
      }
    }

    @Override
    public Object get(String key) {
      if (initializers.containsKey(key)) {
        return initializers.get(key).initialize(this);
      } else {
        throw new IllegalArgumentException("No instance bound for key '" + key + "'");
      }
    }
  }

  public static Injector newInjectorFromList(List<Module> modules) {
    return new InjectorImpl(modules);
  }

  public static Injector newInjector(Module... modules) {
    return Injectors.newInjectorFromList(Arrays.asList(modules));
  }
}
