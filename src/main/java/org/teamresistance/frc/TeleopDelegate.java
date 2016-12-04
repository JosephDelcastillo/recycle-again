package org.teamresistance.frc;

import org.strongback.SwitchReactor;
import org.strongback.command.Command;
import org.strongback.components.Switch;
import org.teamresistance.frc.device.CoDriverBox;
import org.teamresistance.frc.subsystem.binliftin.BinLiftin;
import org.teamresistance.frc.subsystem.binliftin.LiftinGoHome;
import org.teamresistance.frc.subsystem.binliftin.LiftinIndexDown;
import org.teamresistance.frc.subsystem.binliftin.LiftinIndexUp;
import org.teamresistance.frc.subsystem.binliftin.LiftinUnloadAll;
import org.teamresistance.frc.subsystem.binliftin.LiftinZero;

import java.util.function.Supplier;

import static org.teamresistance.frc.device.CoDriverBox.ButtonType.BINLIFTIN_BACKWARD;
import static org.teamresistance.frc.device.CoDriverBox.ButtonType.BINLIFTIN_HOME;
import static org.teamresistance.frc.device.CoDriverBox.ButtonType.BINLIFTIN_INDEX_DOWN;
import static org.teamresistance.frc.device.CoDriverBox.ButtonType.BINLIFTIN_INDEX_UP;
import static org.teamresistance.frc.device.CoDriverBox.ButtonType.BINLIFTIN_UNLOAD;

class TeleopDelegate implements RobotDelegate {
  private final CoDriverBox coDriverBox;
  private final SwitchReactor reactor;
  private final BinLiftin binLiftin;

  TeleopDelegate(CoDriverBox coDriverBox, SwitchReactor reactor, BinLiftin binLiftin) {
    this.coDriverBox = coDriverBox;
    this.reactor = reactor;
    this.binLiftin = binLiftin;
  }

  @Override
  public void onInit() {
    // Register all the BinLiftin commands
    bindCommand(coDriverBox.getButton(BINLIFTIN_INDEX_UP), () -> new LiftinIndexUp(binLiftin));
    bindCommand(coDriverBox.getButton(BINLIFTIN_INDEX_DOWN), () -> new LiftinIndexDown(binLiftin));
    bindCommand(coDriverBox.getButton(BINLIFTIN_HOME), () -> new LiftinGoHome(binLiftin));
    bindCommand(coDriverBox.getButton(BINLIFTIN_UNLOAD), () -> new LiftinUnloadAll(binLiftin));
    bindCommand(coDriverBox.getButton(BINLIFTIN_BACKWARD), () -> new LiftinZero(binLiftin));
  }

  private void bindCommand(Switch button, Supplier<Command> commandSupplier) {
    reactor.onTriggeredSubmit(button, commandSupplier);
  }
}
