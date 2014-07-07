package coffee;

import ca.thurn.infuse.Injector;

public class CoffeeApp {
  private final CoffeeMaker coffeeMaker;

  CoffeeApp(Injector injector) {
    coffeeMaker = injector.get(CoffeeMaker.class);
  }

  public void run() {
    coffeeMaker.brew();
  }
}
