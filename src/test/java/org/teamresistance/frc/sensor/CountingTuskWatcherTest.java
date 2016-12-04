package org.teamresistance.frc.sensor;

import org.junit.jupiter.api.Test;
import org.strongback.mock.Mock;
import org.strongback.mock.MockMotor;
import org.teamresistance.frc.subsystem.binliftin.BinLiftin;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is a unit test for the {@link CountingTuskWatcher}. It ensures the class properly increments
 * and decrements the current index of the {@link BinLiftin} in accordance with the direction of the
 * motor.
 *
 * @author Rothanak So
 */
class CountingTuskWatcherTest {
  private final MockMotor motor = Mock.stoppedMotor();
  private final CountingTuskWatcher tuskWatcher = new CountingTuskWatcher(motor);

  @Test
  void positiveMotor_onIndexed_ShouldIncreaseCount() {
    motor.setSpeed(1.0);
    tuskWatcher.onIndexed();
    tuskWatcher.onIndexed();
    tuskWatcher.onIndexed();

    assertThat(tuskWatcher.getCurrentIndex()).isEqualTo(3);
  }

  @Test
  void positiveMotor_onIndexed_ShouldNotExceedMax() {
    motor.setSpeed(1.0);
    for (int i = 0; i < TuskWatcher.MAX_INDEX; i++) {
      tuskWatcher.onIndexed();
    }
    tuskWatcher.onIndexed();
    tuskWatcher.onIndexed();

    assertThat(tuskWatcher.getCurrentIndex()).isEqualTo(TuskWatcher.MAX_INDEX);
  }

  @Test
  void negativeMotor_onIndexed_ShouldNotExceedBelowHome() {
    motor.setSpeed(-1.0);
    tuskWatcher.onIndexed();
    tuskWatcher.onIndexed();
    tuskWatcher.onIndexed();

    assertThat(tuskWatcher.getCurrentIndex()).isEqualTo(-1);
  }

  @Test
  void mixedMotor_onIndexed_ShouldCountAccordingly() {
    motor.setSpeed(1.0);
    tuskWatcher.onIndexed();
    tuskWatcher.onIndexed();

    motor.setSpeed(-1.0);
    tuskWatcher.onIndexed();
    tuskWatcher.onIndexed();

    assertThat(tuskWatcher.getCurrentIndex()).isEqualTo(0);
  }
}