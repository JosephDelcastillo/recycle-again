package org.teamresistance.frc.subsystem.binliftin;

import com.sun.istack.internal.NotNull;

import org.strongback.command.Requirable;
import org.strongback.components.Motor;
import org.strongback.components.Switch;
import org.teamresistance.frc.sensor.TuskWatcher;

/**
 * The BinLiftin is a chain-driven mechanism built to lift and carry up to 6(?) totes. It is
 * operated by one motor controlled by a victorSP and has two limit switches: one which marks the
 * end of the "home" position and another which the tusks activate upon sliding by. A pivoting joint
 * connects it to the Shuttle.
 * <p>
 * The current position (or index) of the BinLiftin can be changed by running the commands such as
 * {@link LiftinIndexUp} and {@link LiftinGoHome}. The lifter can be tilted back, but this operation
 * is current unsupported.
 *
 * @author Rothanak So
 */
public class BinLiftin implements Requirable {
  private static final double INDEX_SPEED = 0.75;
  private static final double HOME_SPEED = 0.50;
  private static final double UNLOAD_SPEED = 0.50;
  private static final double[] HOLD_SPEEDS =
      { 0.0, 0.0, 0.0, 0.0, 0.1, 0.1, 0.2, 0.2, 0.2, 0.2 };

  private final Motor binLiftinMotor;
  private final TuskWatcher tuskWatcher;
  private final Switch hasIndexedLimit;

  public BinLiftin(@NotNull Motor binLiftinMotor, @NotNull TuskWatcher tuskWatcher,
                   @NotNull Switch hasIndexedLimit) {
    this.binLiftinMotor = binLiftinMotor;
    this.tuskWatcher = tuskWatcher;
    this.hasIndexedLimit = hasIndexedLimit;
  }

  private void safeSetSpeed(double speed) {
    binLiftinMotor.setSpeed((isAtTop() && speed > 0 || isAtHome() && speed < 0) ? 0 : speed);
  }

  void indexUp() {
    safeSetSpeed(INDEX_SPEED);
  }

  void indexDown() {
    safeSetSpeed(-INDEX_SPEED);
  }

  void goHome() {
    safeSetSpeed(-HOME_SPEED);
  }

  void unload() {
    safeSetSpeed(-UNLOAD_SPEED);
  }

  void zeroFromAhead() {
    safeSetSpeed(-HOME_SPEED);
  }

  void zeroFromBehind() {
    safeSetSpeed(HOME_SPEED);
  }

  void hold() {
    // TODO: safety assurance. Right now, safeSetSpeed prohibits setting a positive while at the
    // very top. Might be a non-issue if my understanding of "top" is wrong and "top" =/= MAX_INDEX
    binLiftinMotor.setSpeed(HOLD_SPEEDS[tuskWatcher.getCurrentToteCount()]);
  }

  boolean isAtTop() {
    return tuskWatcher.isAtTop();
  }

  boolean isAtZero() {
    return tuskWatcher.isAtZero();
  }

  boolean isAtHome() {
    return tuskWatcher.isAtHome();
  }

  boolean hasIndexed() {
    return hasIndexedLimit.isTriggered();
  }

  int getCurrentIndex() {
    return tuskWatcher.getCurrentIndex();
  }
}
