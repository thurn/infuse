package com.tinlib.infuse;

import java.util.Arrays;
import java.util.List;

public final class Injectors {
  private Injectors() {}

  public static Injector newInjector(List<Module> modules) {
    return new InjectorImpl(modules);
  }

  public static Injector newInjector(Module... modules) {
    return Injectors.newInjector(Arrays.asList(modules));
  }

  public static Injector newOverridingTestInjector(List<Module> modules) {
    return new InjectorImpl(modules, true /* allowDuplicates */);
  }

  public static Injector newOverridingTestInjector(Module... modules) {
    return Injectors.newOverridingTestInjector(Arrays.asList(modules));
  }
}
