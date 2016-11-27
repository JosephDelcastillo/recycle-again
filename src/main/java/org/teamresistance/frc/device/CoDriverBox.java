package org.teamresistance.frc.device;

import org.strongback.components.Switch;
import org.strongback.components.ui.ContinuousRange;

import edu.wpi.first.wpilibj.Joystick;

/**
 * The co-driver box is a custom joystick-like device built to pilot the recycle bot.
 * It features 11 buttons, 2 sliders, and one rotating knob. Created by Frank McCoy.
 *
 * @author Rothanak So
 */
public final class CoDriverBox {

  public enum ButtonType {
    CLAW_TOP(16),
    CLAW_CENTER(19),
    GRIP_TOGGLE(14),
    SHUTTLE_HIGH(4),
    SHUTTLE_LOW(12),
    BINLIFTIN_FORWARD(15),
    BINLIFTIN_BACKWARD(3),
    BINLIFTIN_INDEX_UP(2),
    BINLIFTIN_INDEX_DOWN(17),
    BINLIFTIN_HOME(18),
    BINLIFTIN_UNLOAD(5);

    private final int number;

    ButtonType(int number) {
      this.number = number;
    }
  }

  private static final int KNOB_AXIS = 2;
  private final Joystick inputDevice;

  public CoDriverBox(int port) {
    this.inputDevice = new Joystick(port);
  }

  public Switch getButton(ButtonType buttonType) {
    return () -> inputDevice.getRawButton(buttonType.number);
  }

  public ContinuousRange getKnob() {
    return () -> inputDevice.getRawAxis(KNOB_AXIS);
  }

  public ContinuousRange getX() {
    return inputDevice::getX;
  }

  public ContinuousRange getY() {
    return inputDevice::getY;
  }
}
