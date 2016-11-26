package org.teamresistance.frc.subsystem.binliftin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.strongback.mock.Mock;
import org.strongback.mock.MockMotor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is a unit test that tests the ONLY logic for the {@link BinLiftin}. Testing occurs
 * under total isolation; outside dependencies are mocked. For an idea of how the BinLiftin
 * integrates with the command framework, see {@link LiftinIndexUpTest}.
 *
 * @author Rothanak So
 * @see LiftinIndexUpTest
 */
class BinLiftinTest {
  private MockMotor motor = Mock.stoppedMotor();
  private FakeTuskWatcher liftinWatcher = FakeTuskWatcher.atZero();
  private BinLiftin binLiftin = new BinLiftin(motor, liftinWatcher);

  @Test
  void unsafeIndexUp_ShouldMoveMotorForward() {
    binLiftin.unsafeIndexUp();
    assertThat(motor.getSpeed()).isPositive();
  }

  @Test
  void unsafeIndexDown_ShouldMoveMotorBackward() {
    binLiftin.unsafeIndexDown();
    assertThat(motor.getSpeed()).isNegative();
  }

  @Test
  void unsafeGoHome_ShouldMoveMotorBackward() {
    binLiftin.unsafeGoHome();
    assertThat(motor.getSpeed()).isNegative();
  }

  @Nested
  class WhenNotHoldingTotes {

    @BeforeEach
    void doNotHoldTotes() {
      liftinWatcher.empty();
      motor.setSpeed(1.0); // a running motor lets us verify the motor is later stopped
    }

    @Test
    void hold_ShouldKillMotor() {
      binLiftin.hold();
      assertThat(motor.getSpeed()).isZero();
    }
  }

  @Nested
  class WhenHoldingTotes {

    @BeforeEach
    void holdMaxCapacity() {
      liftinWatcher.fillWithTotes();
    }

    @Test
    void hold_ShouldIdleMotor() {
      binLiftin.hold();
      assertThat(motor.getSpeed()).isPositive();
    }
  }
}