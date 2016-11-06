package org.teamresistance.frc;

import com.kauailabs.navx.frc.AHRS;

import org.strongback.components.Motor;
import org.strongback.components.ui.FlightStick;
import org.strongback.drive.MecanumDrive;
import org.strongback.hardware.Hardware;
import org.teamresistance.frc.buildconfig.BuildConfig;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;

/**
 * Main robot class. Override methods from {@link IterativeRobot} to define behavior.
 */
public class Robot extends IterativeRobot {

  // Only a single Joystick is needed for mecanum drive.
  private final FlightStick leftJoystick = Hardware.HumanInterfaceDevices.logitechAttack3D(0);

  // The navX-MXP sensor. We're using it as the gyro for mecanum drive.
  private final AHRS navX = new AHRS(SPI.Port.kMXP, (byte) 50);

  // These are the drive motors; they are only used by the drive class.
  private final Motor leftFront = Hardware.Motors.victorSP(0);
  private final Motor leftRear = Hardware.Motors.victorSP(1);
  private final Motor rightFront = Hardware.Motors.victorSP(2);
  private final Motor rightRear = Hardware.Motors.victorSP(3);
  private final MecanumDrive drive = new MecanumDrive(leftFront, leftRear, rightFront, rightRear, navX::getAngle);

  @Override
  public void robotInit() {
    // Identifies which laptop deployed the code to keep track of whose code is on the
    // robot. The BuildConfig is automatically generated for each user at compile-time
    // using the "USERNAME" system environment variable. Prints to the DriverStation.
    System.out.println(BuildConfig.AGENT);
  }

  @Override
  public void teleopPeriodic() {
    // The roll (x-axis) and pitch (y-axis) of the joystick determine the speed of travel.
    // We don't need the bot to rotate, but if we did, we would increase the rotation speed
    // to 1 and then later decrease it to 0 after reaching our target angle. A constant
    // rotation speed of 1 means the robot will be spinning at full speed indefinitely.
    drive.cartesian(leftJoystick.getRoll().read(), leftJoystick.getPitch().read(), 0);
  }
}
