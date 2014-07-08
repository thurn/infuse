package coffee;

import ca.thurn.infuse.Injector;

public class CoffeeApp {
  private final CoffeeMaker coffeeMaker;

  CoffeeApp(Injector injector) {
    coffeeMaker = (CoffeeMaker)injector.get("CoffeeMaker");
  }

  public void run() {
    coffeeMaker.brew();
  }
}
