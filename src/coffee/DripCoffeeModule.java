package coffee;

import ca.thurn.infuse.Binder;
import ca.thurn.infuse.Initializer;
import ca.thurn.infuse.Injector;
import ca.thurn.infuse.Module;

public class DripCoffeeModule implements Module {
  @Override
  public void configure(Binder binder) {
    binder.includeModule(new PumpModule());
    binder.bindSingletonKey("Heater", new Initializer() {
      @Override
      public Object initialize(Injector injector) {
        return new ElectricHeater();
      }
    });
  }
}
