package org.teamresistance.frc.subsystem.binliftin;

import com.sun.istack.internal.NotNull;

import org.strongback.command.Command;
import org.strongback.command.Requirable;
import org.strongback.components.Motor;
import org.strongback.components.Switch;

/**
 * The BinLiftin is a chain-driven mechanism built to lift and carry up to 6(?) totes.
 * It is operated by one motor controlled by a victorSP and has two limit switches: one
 * which marks the end of the "home" position and another which the tusks activate upon
 * sliding by. A pivoting joint connects it to the Shuttle.
 *
 * The current position (or index) of the BinLiftin can be changed by running the commands
 * returned from methods such as {@link #indexUp()} and {@link #goHome()}. The lifter can
 * be tilted back, but this operation is current unsupported.
 *
 * @author Rothanak So
 */
public class BinLiftin implements Requirable {
  private static final double INDEX_SPEED = 0.75;
  private static final double HOME_SPEED = 0.50;
  private static final double UNLOAD_SPEED = 0.50;
  private static final double[] HOLD_SPEEDS =
      {0.0, 0.0, 0.0, 0.0, 0.1, 0.1, 0.2, 0.2, 0.2, 0.2};

  private final Motor binLiftinMotor;
  private final TuskWatcher tuskWatcher;
  private final Switch hasIndexedLimit;

  public BinLiftin(@NotNull Motor binLiftinMotor, @NotNull TuskWatcher tuskWatcher,
                   @NotNull Switch hasIndexedLimit) {
    this.binLiftinMotor = binLiftinMotor;
    this.tuskWatcher = tuskWatcher;
    this.hasIndexedLimit = hasIndexedLimit;
  }

  /**
   * Returns a command that can be called to move the BinLifter one unit up.
   *
   * @see LiftinIndexUp
   */
  public Command indexUp() {
    return new LiftinIndexUp(this, this::isAtTop, hasIndexedLimit::isTriggered);
  }

  /**
   * Returns a command that can be called to move the BinLifter one unit down.
   *
   * @see LiftinIndexDown
   */
  public Command indexDown() {
    return new LiftinIndexDown(this, this::isAtZero, hasIndexedLimit::isTriggered);
  }

  /**
   * Returns a command that can be called to move the BinLifter to its home position.
   *
   * @see LiftinGoHome
   */
  public Command goHome() {
    return new LiftinGoHome(this, this::isAtHome);
  }

  /**
   * Returns a command that can be called to unload the BinLifter. Behaves the same as
   * {@link #goHome()} except with an added timeout in case totes get stuck.
   *
   * @see LiftinUnloadAll
   */
  public Command unloadAll() {
    // Since this is basically LiftinGoHome with a 10s timeout, we should look for a way to
    // eliminate the need for a named command, unless having it this way adds substantial
    // semantic value.
    return new LiftinUnloadAll(this, this::isAtHome);
  }

  /**
   * Returns a command that can be called to reset the BinLifter to the zero position.
   *
   * @see LiftinZero
   */
  public Command goZero() {
    return new LiftinZero(this, tuskWatcher);
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

  void unsafeZeroFromAhead() {
    binLiftinMotor.setSpeed(-1 * HOME_SPEED);
  }

  void unsafeZeroFromBehind() {
    binLiftinMotor.setSpeed(HOME_SPEED);
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
