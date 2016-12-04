package org.teamresistance.frc.subsystem.binliftin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.strongback.mock.Mock;
import org.strongback.mock.MockMotor;
import org.strongback.mock.MockSwitch;

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
  private MockSwitch hasIndexedSwitch = Mock.notTriggeredSwitch();
  private FakeTuskWatcher liftinWatcher = FakeTuskWatcher.atZero();
  private BinLiftin binLiftin = new BinLiftin(motor, liftinWatcher, hasIndexedSwitch);

  @Nested
  class WhenAtZero {

    @BeforeEach
    void setAtZero() {
      liftinWatcher.setAtZero();
    }

    @Test
    void indexUp_ShouldMoveMotorForward() {
      binLiftin.indexUp();
      assertThat(motor.getSpeed()).isPositive();
    }

    @Test
    void indexDown_ShouldMoveMotorBackward() {
      binLiftin.indexDown();
      assertThat(motor.getSpeed()).isNegative();
    }

    @Test
    void goHome_ShouldMoveMotorBackward() {
      binLiftin.goHome();
      assertThat(motor.getSpeed()).isNegative();
    }

    @Test
    void unload_ShouldMoveMotorBackward() {
      binLiftin.unload();
      assertThat(motor.getSpeed()).isNegative();
    }

    @Test
    void zeroFromAhead_ShouldMoveMotorBackward() {
      binLiftin.zeroFromAhead();
      assertThat(motor.getSpeed()).isNegative();
    }

    @Test
    void zeroFromBehind_ShouldMoveMotorForward() {
      binLiftin.zeroFromBehind();
      assertThat(motor.getSpeed()).isPositive();
    }
  }

  @Nested
  class WhenAtTop {

    @BeforeEach
    void setAtTop() {
      liftinWatcher.setAtTop();
      motor.setSpeed(1.0);
    }

    @Test
    void indexUp_ShouldKillMotor() {
      binLiftin.indexUp();
      assertThat(motor.getSpeed()).isZero();
    }
  }

  @Nested
  class WhenAtHome {

    @BeforeEach
    void setAtHome() {
      liftinWatcher.setAtHome();
      motor.setSpeed(-1.0);
    }

    @Test
    void indexDown_ShouldKillMotor() {
      binLiftin.indexDown();
      assertThat(motor.getSpeed()).isZero();
    }

    @Test
    void goHome_ShouldKillMotor() {
      binLiftin.goHome();
      assertThat(motor.getSpeed()).isZero();
    }

    @Test
    void unload_ShouldKillMotor() {
      binLiftin.unload();
      assertThat(motor.getSpeed()).isZero();
    }
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