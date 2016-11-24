package org.teamresistance.frc.subsystem.binliftin;

public class FakeLiftinWatcher implements LiftinWatcher {
  private int currentIndex = 0;

  @Override
  public int getCurrentIndex() {
    return currentIndex;
  }

  public void setCurrentIndex(int currentIndex) {
    this.currentIndex = currentIndex;
  }
}
