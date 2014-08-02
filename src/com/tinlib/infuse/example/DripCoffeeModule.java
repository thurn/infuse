package com.tinlib.infuse.example;

import com.tinlib.infuse.Binder;
import com.tinlib.infuse.Initializer;
import com.tinlib.infuse.Injector;
import com.tinlib.infuse.Module;

public class DripCoffeeModule implements Module {
  @Override
  public void configure(Binder binder) {
    binder.includeModule(new PumpModule());
    binder.bindClass(Heater.class, new Initializer<Heater>() {
      @Override
      public Heater initialize(Injector injector) {
        return new ElectricHeater();
      }
    });
  }
}
