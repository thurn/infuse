package com.tinlib.infuse.example;

import com.tinlib.infuse.Injector;

public class CoffeeApp {
  private final CoffeeMaker coffeeMaker;

  CoffeeApp(Injector injector) {
    coffeeMaker = injector.get(CoffeeMaker.class);
  }

  public void run() {
    coffeeMaker.brew();
  }
}
