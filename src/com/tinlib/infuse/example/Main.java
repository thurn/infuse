package com.tinlib.infuse.example;

import com.tinlib.infuse.Injector;
import com.tinlib.infuse.Injectors;

public class Main {
  public static void main(String[] args) {
    Injector injector = Injectors.newInjector(new CoffeeModule());
    CoffeeApp coffeeApp = injector.get(CoffeeApp.class);
    coffeeApp.run();
  }
}
