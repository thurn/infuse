package com.tinlib.infuse.example;

import com.tinlib.infuse.Binder;
import com.tinlib.infuse.Initializer;
import com.tinlib.infuse.Injector;
import com.tinlib.infuse.Module;

public class CoffeeModule implements Module {
  @Override
  public void configure(Binder binder) {
    binder.includeModule(new DripCoffeeModule());
    binder.bindClass(CoffeeApp.class, new Initializer<CoffeeApp>() {
      @Override
      public CoffeeApp initialize(Injector injector) {
        return new CoffeeApp(injector);
      }
    });
    binder.bindClass(CoffeeMaker.class, new Initializer<CoffeeMaker>() {
      @Override
      public CoffeeMaker initialize(Injector injector) {
        return new CoffeeMaker(injector);
      }
    });
  }
}
