package coffee;

import ca.thurn.infuse.Binder;
import ca.thurn.infuse.Initializer;
import ca.thurn.infuse.Injector;
import ca.thurn.infuse.Module;

public class CoffeeModule implements Module {
  @Override
  public void configure(Binder binder) {
    binder.includeModule(new DripCoffeeModule());
    binder.bindKey("CoffeeApp", new Initializer() {
      @Override
      public Object initialize(Injector injector) {
        return new CoffeeApp(injector);
      }
    });
    binder.bindKey("CoffeeMaker", new Initializer() {
      @Override
      public Object initialize(Injector injector) {
        return new CoffeeMaker(injector);
      }
    });
  }
}
