package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Command;

import java.util.function.BooleanSupplier;

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
 * @see BinLiftin#unsafeIndexDown()
 * @see BinLiftin#hold()
 */
public class LiftinIndexDown extends Command {
  private final BinLiftin binLiftin;
  private final BooleanSupplier isAtZero;
  private final BooleanSupplier hasIndexed;

  LiftinIndexDown(BinLiftin binLiftin, BooleanSupplier isAtZero, BooleanSupplier hasIndexed) {
    super(binLiftin);
    this.binLiftin = binLiftin;
    this.isAtZero = isAtZero;
    this.hasIndexed = hasIndexed;
  }

  @Override
  public boolean execute() {
    binLiftin.unsafeIndexDown();
    return isAtZero.getAsBoolean() || hasIndexed.getAsBoolean();
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
