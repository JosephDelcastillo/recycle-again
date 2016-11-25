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
 * integrates with the goHomeCommand framework, see {@link LiftinIndexUpTest}.
 *
 * @author Rothanak So
 * @see LiftinIndexUpTest
 */
class BinLiftinTest {
  private MockMotor motor = Mock.stoppedMotor();
  private FakeTuskWatcher liftinWatcher = FakeTuskWatcher.atZero();
  private BinLiftin binLiftin = new BinLiftin(motor, liftinWatcher);

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

  @Nested
  class WhenIdleInMiddle {

    @BeforeEach
    void pretendInMiddle() {
      liftinWatcher.setInMiddle();
      motor.stop();
    }

    @Test
    void safeIndexUp_ShouldMoveMotorForward() {
      binLiftin.safeIndexUp();
      assertThat(motor.getSpeed()).isPositive();
    }

    @Test
    void safeIndexUp_ShouldReturnFalse() {
      boolean isFinished = binLiftin.safeIndexUp();
      assertThat(isFinished).isFalse();
    }

    @Test
    void safeIndexDown_ShouldMoveMotorBackward() {
      binLiftin.safeIndexDown();
      assertThat(motor.getSpeed()).isNegative();
    }

    @Test
    void safeIndexDown_ShouldReturnFalse() {
      boolean isFinished = binLiftin.safeIndexDown();
      assertThat(isFinished).isFalse();
    }
  }

  @Nested
  class WhenAtTop {

    @BeforeEach
    void moveToTop() {
      liftinWatcher.setAtTop();
      motor.setSpeed(1.0);
    }

    @Test
    void safeIndexUp_ShouldKillMotor() {
      binLiftin.safeIndexUp();
      assertThat(motor.getSpeed()).isZero();
    }

    @Test
    void safeIndexUp_ShouldReturnTrue() {
      boolean isFinished = binLiftin.safeIndexUp();
      assertThat(isFinished).isTrue();
    }
  }

  @Nested
  class WhenAtZero {

    @BeforeEach
    void moveToZero() {
      liftinWatcher.setAtZero();
      motor.setSpeed(1.0);
    }

    @Test
    void safeIndexDown_ShouldKillMotor() {
      binLiftin.safeIndexDown();
      assertThat(motor.getSpeed()).isZero();
    }

    @Test
    void safeIndexDown_ShouldReturnTrue() {
      boolean isFinished = binLiftin.safeIndexDown();
      assertThat(isFinished).isTrue();
    }
  }

  @Nested
  class WhenAwayFromHome {

    @BeforeEach
    void setAwayFromHome() {
      liftinWatcher.setAtTop();
      motor.setSpeed(1.0);
    }

    @Test
    void safeGoHome_ShouldMoveMotorBackward() {
      binLiftin.safeGoHome();
      assertThat(motor.getSpeed()).isNegative();
    }
  }

  @Nested
  class WhenAtHome {

    @BeforeEach
    void setAtHome() {
      liftinWatcher.setAtHome();
      motor.setSpeed(1.0);
    }

    @Test
    void safeGoHome_ShouldKillMotor() {
      binLiftin.safeGoHome();
      assertThat(motor.getSpeed()).isZero();
    }
  }
}