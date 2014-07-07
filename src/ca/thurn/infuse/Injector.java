package ca.thurn.infuse;

import java.util.Arrays;
import java.util.List;

/**
 * A manager for a set of {@link Module}s which is used to resolve
 * dependencies. Typically, your classes will accept an Injector as their first
 * constructor argument and use it to request other objects which they wish to
 * collaborate with.
 */
public class Injector {
  private static class WrapperModule extends Module {
    @Override
    public void configure() {}
  }

  private final Module wrapperModule;

  private Injector(List<Module> modules) {
    wrapperModule = new WrapperModule();
    for (Module module : modules) {
      wrapperModule.includeModule(module);
    }
  }

  /**
   * Creates a new Injector to manage the provided Modules. Dependencies
   * configured in the supplied Modules will have this Injector passed to their
   * {@link Initializer#initialize(Injector)} method.
   *
   * @param modules Modules to manage. Typically you'll want to supply newly-
   *     created Modules. Sharing Modules between multiple Injectors is
   *     allowed, but Initializers are Module-scoped, so things like singletons
   *     will still only be created once.
   * @return A newly created Injector wrapping the supplied modules.
   */
  public static Injector create(Module... modules) {
    return new Injector(Arrays.asList(modules));
  }

  public <T> T get(Class<T> key) {
    return wrapperModule.runInitializer(this, key);
  }
}
