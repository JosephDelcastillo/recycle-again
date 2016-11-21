package org.teamresistance.frc;

import org.strongback.Strongback;
//import org.teamresistance.frc.buildconfig.BuildConfig;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * Main robot class. Override methods from {@link IterativeRobot} to define behavior.
 */

// TODO:
//    buttons should change subsystem state to COMMAND_CONTROL
//    AND
//    should submit command(s) or command group(s) to Strongback
//    IO class shouldn't be referenced anywhere but

public class Robot extends IterativeRobot {

    Drive drive;

    @Override
    public void robotInit() {
        // Identifies which laptop deployed the code to keep track of whose code is on the
        // robot. The BuildConfig is automatically generated for each user at compile-time
        // using the "USERNAME" system environment variable. Prints to the DriverStation.
//        System.out.println(BuildConfig.AGENT);

        Strongback.configure().recordNoEvents().recordNoData().initialize();
        drive = new Drive(IO.robotDrive);

    }

    @Override
    public void autonomousInit() {
        Strongback.start();
    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void teleopPeriodic() {
        drive.update(IO.translateXSpeed, IO.translateYSpeed, IO.gyro);
    }

    @Override
    public void disabledInit() {
        // stop every subsystem
        drive.stop();
        Strongback.disable();
    }





}
