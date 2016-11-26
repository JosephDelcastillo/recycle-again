package org.teamresistance.frc.util;

import java.util.function.BooleanSupplier;

public class FakeBooleanSupplier extends FakeSupplier<Boolean> implements BooleanSupplier {

  public FakeBooleanSupplier(boolean value) {
    super(value);
  }

  @Override
  public boolean getAsBoolean() {
    return this.getValue();
  }
}
