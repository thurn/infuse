package com.tinlib.infuse;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class InjectorImplTest {
  private static interface NumberClass {
    public int value();
  }

  private static class TwoClass implements NumberClass {
    public int value() {
      return 2;
    }
  }

  private static class FourClass implements NumberClass {
    public int value() {
      return 4;
    }
  }

  private static class EightClass implements NumberClass {
    private final TwoClass twoClass;
    private final FourClass fourClass;

    public EightClass(Injector injector) {
      twoClass = injector.get(TwoClass.class);
      fourClass = injector.get(FourClass.class);
    }

    public int value() {
      return fourClass.value() * twoClass.value();
    }
  }

  private static class BooleanInvoker {
    public BooleanInvoker(AtomicBoolean atomicBoolean) {
      atomicBoolean.set(true);
    }
  }

  @Test
  public void testBindAndInject() {
    Module module = new Module() {
      @Override
      public void configure(Binder binder) {
        binder.bindClass(EightClass.class, new Initializer<EightClass>() {
          @Override
          public EightClass initialize(Injector injector) {
            return new EightClass(injector);
          }
        });
        binder.bindClass(FourClass.class, Initializers.returnValue(new FourClass()));
        binder.bindClass(TwoClass.class, Initializers.returnValue(new TwoClass()));
      }
    };
    Injector injector = Injectors.newInjector(module);
    assertEquals(8, injector.get(EightClass.class).value());
  }

  @Test
  public void testMultibind() {
    Module module = new Module() {
      @Override
      public void configure(Binder binder) {
        binder.multibindClass(NumberClass.class,
            Initializers.<NumberClass>returnValue(new FourClass()));
        binder.multibindClass(NumberClass.class,
            Initializers.<NumberClass>returnValue(new TwoClass()));
      }
    };
    Injector injector = Injectors.newInjector(module);
    Set<NumberClass> numberClasses = injector.getMultiple(NumberClass.class);
    assertEquals(2, numberClasses.size());
  }

  @Test
  public void testOverridingInjector() {
    Module module1 = new Module() {
      @Override
      public void configure(Binder binder) {
        binder.bindClass(NumberClass.class,
            Initializers.<NumberClass>returnValue(new TwoClass()));
      }
    };
    Module module2 = new Module() {
      @Override
      public void configure(Binder binder) {
        binder.bindClass(NumberClass.class,
            Initializers.<NumberClass>returnValue(new FourClass()));
      }
    };
    Injector injector = Injectors.newOverridingTestInjector(ImmutableList.of(module1, module2));
    assertEquals(4, injector.get(NumberClass.class).value());
  }

  @Test(expected = RuntimeException.class)
  public void testDuplicateException() {
    Module module1 = new Module() {
      @Override
      public void configure(Binder binder) {
        binder.bindClass(NumberClass.class,
            Initializers.<NumberClass>returnValue(new TwoClass()));
      }
    };
    Module module2 = new Module() {
      @Override
      public void configure(Binder binder) {
        binder.bindClass(NumberClass.class,
            Initializers.<NumberClass>returnValue(new FourClass()));
      }
    };
    Injectors.newInjector(ImmutableList.of(module1, module2));
  }

  @Test(expected = RuntimeException.class)
  public void testSingleBindThenMultibindException() {
    Module module = new Module() {
      @Override
      public void configure(Binder binder) {
        binder.bindClass(NumberClass.class,
            Initializers.<NumberClass>returnValue(new TwoClass()));
        binder.multibindClass(NumberClass.class,
            Initializers.<NumberClass>returnValue(new FourClass()));
      }
    };
    Injectors.newInjector(module);
  }

  @Test(expected = RuntimeException.class)
  public void testMultibindThenSingleBindException() {
    Module module = new Module() {
      @Override
      public void configure(Binder binder) {
        binder.multibindClass(NumberClass.class,
            Initializers.<NumberClass>returnValue(new FourClass()));
        binder.bindClass(NumberClass.class,
            Initializers.<NumberClass>returnValue(new TwoClass()));
      }
    };
    Injectors.newInjector(module);
  }

  @Test
  public void testIncludeModule() {
    final Module module1 = new Module() {
      @Override
      public void configure(Binder binder) {
        binder.multibindClass(NumberClass.class,
            Initializers.<NumberClass>returnValue(new TwoClass()));
      }
    };
    Module module2 = new Module() {
      @Override
      public void configure(Binder binder) {
        binder.multibindClass(NumberClass.class,
            Initializers.<NumberClass>returnValue(new FourClass()));
        binder.includeModule(module1);
      }
    };
    assertEquals(2, Injectors.newInjector(module2).getMultiple(NumberClass.class).size());
  }

  @Test
  public void testConstructorRuns() {
    final AtomicBoolean fired = new AtomicBoolean(false);
    Injectors.newInjector(new Module() {
      @Override
      public void configure(Binder binder) {
        binder.bindClass(BooleanInvoker.class, new Initializer<BooleanInvoker>() {
          @Override
          public BooleanInvoker initialize(Injector injector) {
            return new BooleanInvoker(fired);
          }
        });
      }
    });
    assertTrue(fired.get());
  }

  @Test(expected = RuntimeException.class)
  public void testNoInstanceBoundException() {
    Injectors.newInjector(new EmptyModule()).get(NumberClass.class);
  }
}