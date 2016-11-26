package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Requirable;
import org.strongback.components.Motor;

// This class will grow, so I'm gonna hold off on docs for now. --Rothanak
class BinLiftin implements Requirable {
  private static final double INDEX_SPEED = 0.75;
  private static final double HOME_SPEED = 0.50;
  private static final double UNLOAD_SPEED = 0.50;
  private static final double[] HOLD_SPEEDS = {
      0.0, 0.0, 0.0, 0.0, 0.1, 0.1, 0.2, 0.2, 0.2, 0.2
  };

  private final Motor binLiftinMotor;
  private final TuskWatcher tuskWatcher;

  BinLiftin(Motor binLiftinMotor, TuskWatcher tuskWatcher) {
    this.binLiftinMotor = binLiftinMotor;
    this.tuskWatcher = tuskWatcher;
  }

  void unsafeIndexUp() {
    binLiftinMotor.setSpeed(INDEX_SPEED);
  }

  void unsafeIndexDown() {
    binLiftinMotor.setSpeed(-1 * INDEX_SPEED);
  }

  void unsafeGoHome() {
    binLiftinMotor.setSpeed(-1 * HOME_SPEED);
  }

  void unsafeUnload() {
    binLiftinMotor.setSpeed(-1 * UNLOAD_SPEED);
  }

  void hold() {
    binLiftinMotor.setSpeed(HOLD_SPEEDS[tuskWatcher.getCurrentToteCount()]);
  }
}
