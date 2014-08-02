package com.tinlib.infuse.example;

public class ElectricHeater implements Heater {
  boolean heating;

  @Override
  public void on() {
    System.out.println("~ ~ ~ heating ~ ~ ~");
    heating = true;
  }

  @Override
  public void off() {
    heating = false;
  }

  @Override
  public boolean isHot() {
    return heating;
  }
}
