package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Command;

/**
 * Also known as "pickup", this command will raise the {@link BinLiftin} exactly one unit.
 * Once done, it will hold the BinLiftin at the new index. If the BinLiftin is already at
 * the top, the command will abort.
 * <p>
 * If you call this command again before it has finished, Strongback will <b>not</b> queue
 * another indexing; a new instance will take precedence over the current one. To index
 * again, this command must be called at least once <i>after</i> the running indexing
 * has finished. Spam the trigger as desired.
 *
 * @author Rothanak So
 * @see BinLiftin#indexUp()
 * @see BinLiftin#hold()
 */
public class LiftinIndexUp extends Command {
  private final BinLiftin binLiftin;

  public LiftinIndexUp(BinLiftin binLiftin) {
    super(binLiftin);
    this.binLiftin = binLiftin;
  }

  @Override
  public boolean execute() {
    binLiftin.indexUp();
    return binLiftin.isAtTop() || binLiftin.hasIndexed();
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
