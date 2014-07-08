package coffee;

import ca.thurn.infuse.Injector;
import ca.thurn.infuse.Injectors;

public class Main {
  public static void main(String[] args) {
    Injector injector = Injectors.newInjector(new CoffeeModule());
    CoffeeApp coffeeApp = (CoffeeApp)injector.get("CoffeeApp");
    coffeeApp.run();
  }
}
