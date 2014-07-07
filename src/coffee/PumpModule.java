package coffee;

import ca.thurn.infuse.Initializer;
import ca.thurn.infuse.Injector;
import ca.thurn.infuse.Module;

public class PumpModule extends Module {
  @Override
  public void configure() {
    bindKey(Pump.class, new Initializer<Pump>() {
      @Override
      public Pump initialize(Injector injector) {
        return new Thermosiphon(injector);
      }
    });
  }
}
