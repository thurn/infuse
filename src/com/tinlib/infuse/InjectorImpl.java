package com.tinlib.infuse;

import com.google.common.collect.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

class InjectorImpl implements Binder, Injector {
  private final Map<Class<?>, Initializer<?>> initializers = Maps.newHashMap();
  private final SetMultimap<Class<?>, Initializer<?>> multiInitializers = HashMultimap.create();
  private final Map<Class<?>, Object> valueCache = Maps.newHashMap();
  private final SetMultimap<Class<?>, Object> multivalueCache = HashMultimap.create();
  private final boolean allowDuplicates;

  InjectorImpl(List<Module> modules) {
    this(modules, false /* allowDuplicates */);
  }

  InjectorImpl(List<Module> modules, boolean allowDuplicates) {
    this.allowDuplicates = allowDuplicates;

    for (Module module : modules) {
      module.configure(this);
    }

    for (Class<?> key : initializers.keySet()) {
      get(key);
    }

    for (Class<?> key : multiInitializers.keySet()) {
      getMultiple(key);
    }
  }

  @Override
  public <T> void bindClass(Class<T> classObject, Initializer<T> initializer) {
    if (initializers.containsKey(classObject) && !allowDuplicates) {
      throw new RuntimeException("Attempted to bind previously bound class " + classObject);
    }
    if (multiInitializers.containsKey(classObject)) {
      throw new RuntimeException("Attempted to single bind previously multibound class "
          + classObject);
    }
    initializers.put(classObject, initializer);
  }

  @Override
  public <T> void multibindClass(Class<T> classObject, Initializer<T> initializer) {
    if (initializers.containsKey(classObject)) {
      throw new RuntimeException("Attempted to multibind previously single-bound class "
          + classObject);
    }
    multiInitializers.put(classObject, initializer);
  }

  @Override
  public void includeModule(Module module) {
    module.configure(this);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T get(Class<T> classObject) {
    if (valueCache.containsKey(classObject)) {
      return (T) valueCache.get(classObject);
    } else if (initializers.containsKey(classObject)) {
      T result = (T) initializers.get(classObject).initialize(this);
      valueCache.put(classObject, result);
      return result;
    } else {
      throw new RuntimeException("No instance bound for class " + classObject);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> ImmutableSet<T> getMultiple(Class<T> classObject) {
    if (!multivalueCache.containsKey(classObject)) {
      for (Initializer<?> initializer : multiInitializers.get(classObject)) {
        multivalueCache.put(classObject, initializer.initialize(this));
      }
    }
    return ImmutableSet.copyOf((Set<T>) multivalueCache.get(classObject));
  }
}
