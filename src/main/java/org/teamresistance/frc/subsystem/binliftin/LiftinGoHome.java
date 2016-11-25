package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Command;
import org.strongback.components.Switch;

public class LiftinGoHome extends Command {
  private final BinLiftin binLiftin;
  private final Switch atHomeSwitch;

  LiftinGoHome(BinLiftin binLiftin, Switch atHomeSwitch) {
    super(binLiftin);
    this.binLiftin = binLiftin;
    this.atHomeSwitch = atHomeSwitch;
  }

  @Override
  public boolean execute() {
    binLiftin.safeGoHome();
    return atHomeSwitch.isTriggered();
  }

  @Override
  public void interrupted() {
    binLiftin.hold();
  }

  @Override
  public void end() {
    binLiftin.hold();
  }
}
