package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Command;

public class LiftinZero extends Command {
  private final BinLiftin binLiftin;
  private final TuskWatcher tuskWatcher;

  LiftinZero(BinLiftin binLiftin, TuskWatcher tuskWatcher) {
    super(binLiftin);
    this.binLiftin = binLiftin;
    this.tuskWatcher = tuskWatcher;
  }

  @Override
  public boolean execute() {
    int currentIndex = tuskWatcher.getCurrentIndex();

    if (currentIndex == 0) {
      // At zero -> finish
      return true;
    } else if(currentIndex > 0) {
      // Tusks are out, so we're ahead of zero
      binLiftin.unsafeZeroFromAhead();
    } else if(currentIndex < 0) {
      // At home, so we're behind zero
      binLiftin.unsafeZeroFromBehind();
    }

    return false;
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
