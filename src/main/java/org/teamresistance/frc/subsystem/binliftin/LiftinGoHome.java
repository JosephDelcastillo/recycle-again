package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Command;

import java.util.function.BooleanSupplier;

public class LiftinGoHome extends Command {
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
