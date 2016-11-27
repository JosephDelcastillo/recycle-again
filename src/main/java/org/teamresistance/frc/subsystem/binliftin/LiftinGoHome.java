package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Command;

import java.util.function.BooleanSupplier;

/**
 * This command will tuck the {@link BinLiftin} back into its home position from wherever
 * it is. Once done, it will idle the motors. If the BinLiftin is already home, the command
 * will abort.
 * <p>
 * This command does <b>not</b> check to see if the {@link BinLiftin} is bearing any totes
 * before wheeling the tusks away.
 *
 * @author Rothanak So
 * @implNote This command might actually be unnecessary if {@link LiftinZero} works.
 * @see BinLiftin#unsafeGoHome()
 * @see BinLiftin#hold()
 */
class LiftinGoHome extends Command {
  private final BinLiftin binLiftin;
  private final BooleanSupplier isAtHome;

  LiftinGoHome(BinLiftin binLiftin, BooleanSupplier isAtHome) {
    super(binLiftin);
    this.binLiftin = binLiftin;
    this.isAtHome = isAtHome;
  }

  @Override
  public boolean execute() {
    binLiftin.unsafeGoHome();
    return isAtHome.getAsBoolean();
  }

  @Override
  public void interrupted() {
    this.end();
  }

  @Override
  public void end() {
    binLiftin.hold();
  }
}
