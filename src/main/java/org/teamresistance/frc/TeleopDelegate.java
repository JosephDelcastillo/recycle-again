package org.teamresistance.frc;

import org.strongback.SwitchReactor;
import org.teamresistance.frc.device.CoDriverBox;
import org.teamresistance.frc.subsystem.binliftin.BinLiftin;

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
    reactor.onTriggeredSubmit(coDriverBox.getButton(BINLIFTIN_INDEX_UP), binLiftin::indexUp);
    reactor.onTriggeredSubmit(coDriverBox.getButton(BINLIFTIN_INDEX_DOWN), binLiftin::indexDown);
    reactor.onTriggeredSubmit(coDriverBox.getButton(BINLIFTIN_HOME), binLiftin::goHome);
    reactor.onTriggeredSubmit(coDriverBox.getButton(BINLIFTIN_UNLOAD), binLiftin::unloadAll);
    reactor.onTriggeredSubmit(coDriverBox.getButton(BINLIFTIN_BACKWARD), binLiftin::goZero); // for now
  }
}
