package com.tinlib.infuse.example;

import com.tinlib.infuse.Binder;
import com.tinlib.infuse.Initializer;
import com.tinlib.infuse.Injector;
import com.tinlib.infuse.Module;

public class PumpModule implements Module {
  @Override
  public void configure(Binder binder) {
    binder.bindClass(Pump.class, new Initializer<Pump>() {
      @Override
      public Pump initialize(Injector injector) {
        return new Thermosiphon(injector);
      }
    });
  }
}
