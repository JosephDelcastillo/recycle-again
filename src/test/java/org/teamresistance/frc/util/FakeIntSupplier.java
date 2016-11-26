package org.teamresistance.frc.util;

import java.util.function.IntSupplier;

public class FakeIntSupplier extends FakeSupplier<Integer> implements IntSupplier {

  public FakeIntSupplier(int value) {
    super(value);
  }

  @Override
  public int getAsInt() {
    return this.getValue();
  }
}
