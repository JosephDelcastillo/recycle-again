package org.teamresistance.frc.util;

import java.util.function.BooleanSupplier;

public class FakeBooleanSupplier implements BooleanSupplier {
  private boolean value;

  public FakeBooleanSupplier(boolean value) {
    this.value = value;
  }

  @Override
  public boolean getAsBoolean() {
    return value;
  }

  public void setValue(boolean value) {
    this.value = value;
  }
}
