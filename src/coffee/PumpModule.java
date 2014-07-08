package coffee;

import ca.thurn.infuse.Binder;
import ca.thurn.infuse.Initializer;
import ca.thurn.infuse.Injector;
import ca.thurn.infuse.Module;

public class PumpModule implements Module {
  @Override
  public void configure(Binder binder) {
    binder.bindKey("Pump", new Initializer() {
      @Override
      public Object initialize(Injector injector) {
        return new Thermosiphon(injector);
      }
    });
  }
}
