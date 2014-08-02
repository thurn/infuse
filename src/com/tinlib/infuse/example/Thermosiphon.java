package com.tinlib.infuse.example;

import com.tinlib.infuse.Injector;

public class Thermosiphon implements Pump {
  private final Heater heater;

  Thermosiphon(Injector injector) {
    heater = injector.get(Heater.class);
  }

  @Override
  public void pump() {
    if (heater.isHot()) {
      System.out.println("=> => pumping => =>");
    }
  }
}
