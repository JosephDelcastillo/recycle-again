package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Command;

/**
 * Also known as "set down", this command will lower the {@link BinLiftin} exactly one unit.
 * Once done, it will hold the BinLiftin at the new index. If the BinLiftin is at the zero
 * position, the command will abort.
 * <p>
 * If you call this command again before it has finished, Strongback will <b>not</b> queue
 * another indexing; a new instance will take precedence over the current one. To index
 * again, this command must be called at least once <i>after</i> the running indexing
 * has finished. Spam the trigger as desired.
 *
 * @author Rothanak So
 * @see BinLiftin#indexDown()
 * @see BinLiftin#hold()
 */
public class LiftinIndexDown extends Command {
  private final BinLiftin binLiftin;

  public LiftinIndexDown(BinLiftin binLiftin) {
    super(binLiftin);
    this.binLiftin = binLiftin;
  }

  @Override
  public boolean execute() {
    binLiftin.indexDown();
    return binLiftin.isAtZero() || binLiftin.hasIndexed();
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
