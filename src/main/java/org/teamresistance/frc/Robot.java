package org.teamresistance.frc;

import org.teamresistance.frc.buildconfig.BuildConfig;

import javax.inject.Inject;

import edu.wpi.first.wpilibj.IterativeRobot;
import timber.log.Timber;

/**
 * Main robot class. Override methods from {@link IterativeRobot} to define behavior.
 */
@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
public class Robot extends IterativeRobot {

  @Inject TeleopDelegate teleop;
  @Inject AutoDelegate auto;

  @Override
  public void robotInit() {
    // Dagger is used to inject and resolve dependencies. Experimental.
    DaggerRobotComponent.create().inject(this);

    // Register a logger (Tree) that prints messages to the console (i.e. DriverStation).
    // Log a message by invoking the static Timber methods, like "Timber.d(..)". Since
    // all logging is through Timber, they can be silenced by not planting any trees.
    Timber.plant(new Timber.Tree() {
      @Override
      protected void log(int priority, String tag, String message, Throwable t) {
        System.out.println(message);
      }
    });

    // Identifies which laptop deployed the code to keep track of whose code is on the
    // robot. The BuildConfig is automatically generated for each user at compile-time
    // using the "USERNAME" system environment variable. Prints to the DriverStation.
    Timber.d(BuildConfig.AGENT);

    // Call into our delegates in case they have initialization code too.
    teleop.beforeInit();
  }

  @Override
  public void teleopPeriodic() {
    // Forward calls to our teleop delegate.
    teleop.onPeriodic();
  }
}
