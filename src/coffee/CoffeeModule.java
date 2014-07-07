package coffee;

import ca.thurn.infuse.Initializer;
import ca.thurn.infuse.Injector;
import ca.thurn.infuse.Module;

public class CoffeeModule extends Module {
  @Override
  public void configure() {
    includeModule(new DripCoffeeModule());
    bindKey(CoffeeApp.class, new Initializer<CoffeeApp>() {
      @Override
      public CoffeeApp initialize(Injector injector) {
        return new CoffeeApp(injector);
      }
    });
    bindKey(CoffeeMaker.class, new Initializer<CoffeeMaker>() {
      @Override
      public CoffeeMaker initialize(Injector injector) {
        return new CoffeeMaker(injector);
      }
    });
  }
}
