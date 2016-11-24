package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Command;
import org.strongback.components.Motor;
import org.strongback.components.Switch;

/**
 * Also known as "pickup", this command will raise the BinLiftin exactly one unit.
 * <p>
 * If the BinLiftin is already at the top according to the {@link LiftinWatcher} sensor,
 * the command will abort. The topmost index is defined by {@link LiftinWatcher#MAX}.
 * <p>
 * If you call this command again before it has finished, Strongback will <b>not</b>
 * queue another indexing; a new instance will simply take precedence over the current
 * one. To index again, this command must be called at least once <i>after</i> the
 * running indexing has finished. Spam the trigger as desired.
 */
public class LiftinIndexUp extends Command {
  private static final double INDEX_UP_SPEED = -0.75;
  private final Motor binLiftinMotor;
  private final Switch hasIndexedSwitch;
  private final LiftinWatcher liftinWatcher;

  public LiftinIndexUp(Motor binLiftinMotor, Switch hasIndexedSwitch, LiftinWatcher liftinWatcher) {
    super(binLiftinMotor);
    this.binLiftinMotor = binLiftinMotor;
    this.hasIndexedSwitch = hasIndexedSwitch;
    this.liftinWatcher = liftinWatcher;
  }

  @Override
  public boolean execute() {
    // If the lift is already at the top, do nothing and abort.
    if (liftinWatcher.getCurrentIndex() == LiftinWatcher.MAX) {
      return true;
    }

    binLiftinMotor.setSpeed(INDEX_UP_SPEED);
    return hasIndexedSwitch.isTriggered();
  }

  @Override
  public void interrupted() {
    binLiftinMotor.stop();
  }

  @Override
  public void end() {
    binLiftinMotor.stop();
  }
}
