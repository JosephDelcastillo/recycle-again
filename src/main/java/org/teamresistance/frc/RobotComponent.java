package org.teamresistance.frc;

import com.kauailabs.navx.frc.AHRS;

import org.strongback.components.AngleSensor;
import org.strongback.components.Motor;
import org.strongback.components.ui.FlightStick;
import org.strongback.drive.MecanumDrive;
import org.strongback.hardware.Hardware;
import org.teamresistance.frc.subsystem.DriveSubsystem;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import edu.wpi.first.wpilibj.SPI;

@Singleton
@Component(modules = {
    RobotComponent.RobotModule.class,
    RobotComponent.SubsystemModule.class,
    RobotComponent.SensorModule.class,
    RobotComponent.HumanDevicesModule.class
})
interface RobotComponent {

  void inject(Robot robot);

  @Module
  class RobotModule {

    @Provides @Singleton
    TeleopDelegate provideTeleopDelegate(DriveSubsystem drive, @Named("Left") FlightStick leftJoystick,
                                         TeleopDelegate.CoDriverBox coDriverBox, AngleSensor gyro) {
      return new TeleopDelegate(drive, leftJoystick, coDriverBox, gyro);
    }

    @Provides @Singleton
    AutoDelegate provideAutoDelegate() {
      return new AutoDelegate();
    }
  }

  @Module
  class SubsystemModule {

    @Provides @Singleton
    DriveSubsystem provideDriveSubsystem(AngleSensor gyro) {
      // These are the drive motors; they are only used by the drive class.
      final Motor leftFront = Hardware.Motors.victorSP(0);
      final Motor leftRear = Hardware.Motors.victorSP(1);
      final Motor rightFront = Hardware.Motors.victorSP(2);
      final Motor rightRear = Hardware.Motors.victorSP(3);
      final MecanumDrive drive = new MecanumDrive(leftFront, leftRear, rightFront, rightRear, gyro);
      return new DriveSubsystem(drive);
    }
  }

  @Module
  class SensorModule {

    @Provides @Singleton
    AngleSensor provideGyro() {
      // Instantiate the navX-MXP sensor. We're using it as the gyro for mecanum drive.
      AHRS navX = new AHRS(SPI.Port.kMXP, (byte) 50);
      return navX::getAngle;
    }
  }

  @Module
  class HumanDevicesModule {
    private static final int JOYSTICK_LEFT = 0;
    private static final int JOYSTICK_RIGHT = 1;

    @Provides @Singleton @Named("Left")
    FlightStick provideLeftJoystick() {
      // Only a single Joystick is needed for mecanum drive.
      return Hardware.HumanInterfaceDevices.logitechAttack3D(JOYSTICK_LEFT);
    }

    @Provides @Singleton
    TeleopDelegate.CoDriverBox provideCoDriverBox() {
      return new TeleopDelegate.CoDriverBox(2);
    }
  }
}
