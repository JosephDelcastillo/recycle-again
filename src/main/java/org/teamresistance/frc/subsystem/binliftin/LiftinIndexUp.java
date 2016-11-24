package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Command;
import org.strongback.components.Motor;
import org.strongback.components.Switch;

/**
 * Also known as "pickup", this command will raise the BinLifter exactly one unit.
 * <p>
 * Calling this command again before it has finished will <b>not</b> queue another
 * index; instead, the running command will be replaced with a new instance. This
 * behavior is harmless though (because this command is stateless), so spam away
 * as needed.
 * <p>
 * TODO: Add a state machine middleman to handle a reached-top state.
 */
public class LiftinIndexUp extends Command {
  private static final double INDEX_UP_SPEED = -0.75;
  private final Motor binLiftinMotor;
  private final Switch hasIndexedSwitch;

  public LiftinIndexUp(Motor binLiftinMotor, Switch hasIndexedSwitch) {
    super(binLiftinMotor);
    this.binLiftinMotor = binLiftinMotor;
    this.hasIndexedSwitch = hasIndexedSwitch;
  }

  @Override
  public boolean execute() {
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
