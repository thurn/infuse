package coffee;

import ca.thurn.infuse.Initializer;
import ca.thurn.infuse.Injector;
import ca.thurn.infuse.Module;

public class DripCoffeeModule extends Module {
  @Override
  public void configure() {
    includeModule(new PumpModule());
    bindSingletonKey(Heater.class, new Initializer<Heater>() {
      @Override
      public Heater initialize(Injector injector) {
        return new ElectricHeater();
      }
    });
  }
}
