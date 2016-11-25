package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Requirable;
import org.strongback.components.Motor;

// This class will grow, so I'm gonna hold off on docs for now. --Rothanak
class BinLiftin implements Requirable {
  private static final double INDEX_SPEED = 0.75;
  private static final double HOME_SPEED = 0.50;
  private static final double[] HOLD_SPEEDS = {
      0.0, 0.0, 0.0, 0.0, 0.1, 0.1, 0.2, 0.2, 0.2, 0.2
  };

  private final Motor binLiftinMotor;
  private final TuskWatcher tuskWatcher;

  BinLiftin(Motor binLiftinMotor, TuskWatcher tuskWatcher) {
    this.binLiftinMotor = binLiftinMotor;
    this.tuskWatcher = tuskWatcher;
  }

  boolean safeIndexUp() {
    if (isAtTop()) {
      hold();
      return true;
    }

    binLiftinMotor.setSpeed(INDEX_SPEED);
    return false;
  }

  boolean safeIndexDown() {
    if (isAtZero()) {
      hold();
      return true;
    }

    binLiftinMotor.setSpeed(-1 * INDEX_SPEED);
    return false;
  }

  void safeGoHome() {
    if (isAtHome()) {
      hold();
      return;
    }

    binLiftinMotor.setSpeed(-1 * HOME_SPEED);
  }

  void hold() {
    binLiftinMotor.setSpeed(HOLD_SPEEDS[tuskWatcher.getCurrentToteCount()]);
  }

  private boolean isAtTop() {
    return tuskWatcher.getCurrentIndex() == TuskWatcher.MAX_INDEX;
  }

  private boolean isAtZero() {
    return tuskWatcher.getCurrentIndex() == 0;
  }

  private boolean isAtHome() {
    return tuskWatcher.getCurrentIndex() == -1;
  }
}
