package org.teamresistance.frc.util;

abstract class FakeSupplier<T> {
  private T value;

  FakeSupplier(T value) {
    this.value = value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  T getValue() {
    return value;
  }
}
