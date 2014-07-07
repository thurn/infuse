package coffee;

import ca.thurn.infuse.Injector;

public class Main {
  public static void main(String[] args) {
    Injector injector = Injector.create(new CoffeeModule());
    CoffeeApp coffeeApp = injector.get(CoffeeApp.class);
    coffeeApp.run();
  }
}
