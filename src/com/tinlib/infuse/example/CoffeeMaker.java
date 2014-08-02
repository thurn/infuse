package com.tinlib.infuse.example;

import com.tinlib.infuse.Injector;

public class CoffeeMaker {
  private final Heater heater;
  private final Pump pump;

  CoffeeMaker(Injector injector) {
    heater = injector.get(Heater.class);
    pump = injector.get(Pump.class);
  }

  public void brew() {
    heater.on();
    pump.pump();
    System.out.println(" [_]P coffee! [_]P ");
    heater.off();
  }
}
