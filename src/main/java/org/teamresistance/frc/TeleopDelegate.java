package org.teamresistance.frc;

import org.strongback.components.ui.FlightStick;
import org.strongback.drive.MecanumDrive;

import javax.inject.Named;

class TeleopDelegate implements RobotDelegate {
  private final MecanumDrive drive;
  private final FlightStick leftJoystick;

  TeleopDelegate(MecanumDrive drive, @Named("Left") FlightStick leftJoystick) {
    this.drive = drive;
    this.leftJoystick = leftJoystick;
  }

  @Override
  public void onPeriodic() {
    // The roll (x-axis) and pitch (y-axis) of the joystick determine the speed of travel.
    // We don't need the bot to rotate, but if we did, we would increase the rotation speed
    // to 1 and then later decrease it to 0 after reaching our target angle. A constant
    // rotation speed of 1 means the robot will be spinning at full speed indefinitely.
    drive.cartesian(leftJoystick.getRoll().read(), leftJoystick.getPitch().read(), 0);
  }
}
