package org.teamresistance.frc;

import com.kauailabs.navx.frc.AHRS;

import org.strongback.components.Motor;
import org.strongback.components.ui.FlightStick;
import org.strongback.drive.MecanumDrive;
import org.strongback.hardware.Hardware;

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
    TeleopDelegate provideTeleopDelegate(MecanumDrive drive, @Named("Left") FlightStick leftJoystick) {
      return new TeleopDelegate(drive, leftJoystick);
    }

    @Provides @Singleton
    AutoDelegate provideAutoDelegate() {
      return new AutoDelegate();
    }
  }

  @Module
  class SubsystemModule {

    @Provides @Singleton
    MecanumDrive provideDriveSubsystem(AHRS navX) {
      // These are the drive motors; they are only used by the drive class.
      final Motor leftFront = Hardware.Motors.victorSP(0);
      final Motor leftRear = Hardware.Motors.victorSP(1);
      final Motor rightFront = Hardware.Motors.victorSP(2);
      final Motor rightRear = Hardware.Motors.victorSP(3);
      return new MecanumDrive(leftFront, leftRear, rightFront, rightRear, navX::getAngle);
    }
  }

  @Module
  class SensorModule {

    @Provides @Singleton
    AHRS provideNavX() {
      // Provide the navX-MXP sensor. We're using it as the gyro for mecanum drive.
      return new AHRS(SPI.Port.kMXP, (byte) 50);
    }
  }

  @Module
  class HumanDevicesModule {

    @Provides @Singleton @Named("Left")
    FlightStick provideLeftJoystick() {
      // Only a single Joystick is needed for mecanum drive.
      return Hardware.HumanInterfaceDevices.logitechAttack3D(0);
    }
  }
}
