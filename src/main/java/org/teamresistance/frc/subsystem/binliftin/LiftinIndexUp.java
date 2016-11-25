package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Command;
import org.strongback.components.Switch;

/**
 * Also known as "pickup", this command will lower the {@link BinLiftin} exactly one unit.
 * Once done, it will hold the BinLiftin at the new index.
 * <p>
 * If the BinLiftin is already at the top according to the {@link TuskWatcher} sensor,
 * the command will abort. The topmost index is defined by {@link TuskWatcher#MAX_INDEX}.
 * <p>
 * If you call this command again before it has finished, Strongback will <b>not</b> queue
 * another indexing; a new instance will take precedence over the current one. To index
 * again, this command must be called at least once <i>after</i> the running indexing
 * has finished. Spam the trigger as desired.
 *
 * @author Rothanak So
 * @see BinLiftin#safeIndexUp()
 * @see BinLiftin#hold()
 */
public class LiftinIndexUp extends Command {
  private final BinLiftin binLiftin;
  private final Switch hasIndexedSwitch;

  LiftinIndexUp(BinLiftin binLiftin, Switch hasIndexedSwitch) {
    super(binLiftin);
    this.binLiftin = binLiftin;
    this.hasIndexedSwitch = hasIndexedSwitch;
  }

  @Override
  public boolean execute() {
    return binLiftin.safeIndexUp() || hasIndexedSwitch.isTriggered();
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
