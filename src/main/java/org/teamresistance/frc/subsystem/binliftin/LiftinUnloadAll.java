package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Command;

import java.util.function.BooleanSupplier;

public class LiftinUnloadAll extends Command {
  private static final int UNLOAD_TIMEOUT_SECONDS = 1;

  private final BinLiftin binLiftin;
  private final BooleanSupplier isAtHome;

  LiftinUnloadAll(BinLiftin binLiftin, BooleanSupplier isAtHome) {
    super(UNLOAD_TIMEOUT_SECONDS, binLiftin);
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
