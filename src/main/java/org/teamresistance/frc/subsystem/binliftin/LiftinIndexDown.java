package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Command;
import org.strongback.mock.MockSwitch;

/**
 * Also known as "set down", this command will lower the {@link BinLiftin} exactly one unit.
 * Once done, it will hold the BinLiftin at the new index.
 * <p>
 * If the BinLiftin is already at the bottom according to the {@link TuskWatcher} sensor,
 * the command will abort. The bottommost index is defined as 0.
 * <p>
 * If you call this command again before it has finished, Strongback will <b>not</b> queue
 * another indexing; a new instance will take precedence over the current one. To index
 * again, this command must be called at least once <i>after</i> the running indexing
 * has finished. Spam the trigger as desired.
 *
 * @author Rothanak So
 * @see BinLiftin#safeIndexDown()
 * @see BinLiftin#hold()
 */
public class LiftinIndexDown extends Command {
  private final BinLiftin binLiftin;
  private final MockSwitch hasIndexedSwitch;

  LiftinIndexDown(BinLiftin binLiftin, MockSwitch hasIndexedSwitch) {
    super(binLiftin);
    this.binLiftin = binLiftin;
    this.hasIndexedSwitch = hasIndexedSwitch;
  }

  @Override
  public boolean execute() {
    return binLiftin.safeIndexDown()|| hasIndexedSwitch.isTriggered();
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
