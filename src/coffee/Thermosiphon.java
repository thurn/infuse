package coffee;

import ca.thurn.infuse.Injector;

public class Thermosiphon implements Pump {
  private final Heater heater;

  Thermosiphon(Injector injector) {
    heater = (Heater)injector.get("Heater");
  }

  @Override
  public void pump() {
    if (heater.isHot()) {
      System.out.println("=> => pumping => =>");
    }
  }
}
