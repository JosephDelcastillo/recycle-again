package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.annotation.Experimental;
import org.strongback.command.Command;

/**
 * This command will move the {@link BinLiftin} to the zero position from wherever it is. The
 * motor will spin backward if the lifter is ahead of zero (tusks are out) and forward if it
 * is behind (at home) until it reaches zero according to the {@link TuskWatcher}. Once done,
 * it will idle the motors. If the BinLiftin is already at zero, the command will abort.
 * <p>
 * This command is typically called after calling {@link LiftinGoHome}. It can also be called
 * to reset the index immediately before calling {@link LiftinIndexUp}.
 *
 * @author Rothanak So
 * @implNote Experimental: relies on the the {@link TuskWatcher} accurately tracking position.
 * @see BinLiftin#zeroFromAhead()
 * @see BinLiftin#zeroFromBehind()
 * @see BinLiftin#hold()
 */
@Experimental
public class LiftinZero extends Command {
  private final BinLiftin binLiftin;

  public LiftinZero(BinLiftin binLiftin) {
    super(binLiftin);
    this.binLiftin = binLiftin;
  }

  @Override
  public boolean execute() {
    int currentIndex = binLiftin.getCurrentIndex();

    if (currentIndex == 0) {
      // At zero -> finish
      return true;
    } else if (currentIndex > 0) {
      // Tusks are out, so we're ahead of zero
      binLiftin.zeroFromAhead();
      return false;
    } else {
      // At home, so we're behind zero
      binLiftin.zeroFromBehind();
      return false;
    }
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
