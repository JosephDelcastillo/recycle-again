package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Requirable;
import org.strongback.components.Motor;

// This class will grow, so I'm gonna hold off on docs for now. --Rothanak
class BinLiftin implements Requirable {
  private static final double INDEX_SPEED = 0.75;
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
    if (isAtBottom()) {
      hold();
      return true;
    }

    binLiftinMotor.setSpeed(-1 * INDEX_SPEED);
    return false;
  }

  void hold() {
    binLiftinMotor.setSpeed(HOLD_SPEEDS[tuskWatcher.getCurrentToteCount()]);
  }

  private boolean isAtTop() {
    return tuskWatcher.getCurrentIndex() == TuskWatcher.MAX_INDEX;
  }

  private boolean isAtBottom() {
    return tuskWatcher.getCurrentIndex() == 0;
  }
}
