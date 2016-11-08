package org.teamresistance.frc;

public interface RobotDelegate {

  default void beforeInit() {
    // noop
  }

  default void onInit() {
    // noop
  }

  default void onPeriodic() {
    // noop
  }
}
