package org.teamresistance.frc.subsystem.binliftin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.strongback.command.CommandTester;
import org.strongback.mock.Mock;
import org.strongback.mock.MockMotor;
import org.strongback.mock.MockSwitch;
import org.teamresistance.frc.util.CommandUtilities;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is an integration test that tests the combined logic for both the {@link LiftinIndexUp}
 * goHomeCommand and the {@link BinLiftin} subsystem. Only the hardware-based inputs and ouputs have
 * been replaced with mocks (motors and sensors). The status of the goHomeCommand (e.g. continuing vs
 * finished) and the motor outputs are used as verification.
 *
 * @author Rothanak So
 * @see BinLiftinTest
 * @see LiftinIndexDownTest
 */
class LiftinIndexUpTest {
  private MockMotor motor = Mock.stoppedMotor();
  private MockSwitch hasIndexedSwitch = Mock.notTriggeredSwitch();
  private FakeTuskWatcher liftinWatcher = FakeTuskWatcher.atZero();

  private BinLiftin binLiftin = new BinLiftin(motor, liftinWatcher);
  private LiftinIndexUp liftinIndexUp = new LiftinIndexUp(binLiftin, hasIndexedSwitch);

  @Test
  void whenMotorRunning_interrupt_ShouldKillMotor() {
    motor.setSpeed(1.0);
    CommandTester runner = new CommandTester(liftinIndexUp);
    CommandUtilities.interrupt(runner);
    assertThat(motor.getSpeed()).isZero();
  }

  // Notice there is no test case for "finish_ShouldKillMotor". This is because is no way for
  // us to manually finish a goHomeCommand. This is actually by design, as a goHomeCommand's final state
  // also depends on whatever happens in #execute, not just #stop. Thus, "finish" varies by
  // scenario too, so we test it within the scenarios wherever relevant.

  @Nested
  class WhenAtTop {

    @BeforeEach
    void pretendAtTop() {
      liftinWatcher.setAtTop();
      motor.setSpeed(1.0);
    }

    @Test
    void execute_ShouldImmediatelyFinish() {
      CommandTester runner = new CommandTester(liftinIndexUp);
      boolean isFinished = runner.step(0);
      assertThat(isFinished).isTrue();
    }

    @Test
    void execute_ShouldNotMoveMotor() {
      CommandTester runner = new CommandTester(liftinIndexUp);
      runner.step(0);
      assertThat(motor.getSpeed()).isZero();
    }
  }

  @Nested
  class WhenNotAtTop {

    @BeforeEach
    void pretendInMiddle() {
      liftinWatcher.setInMiddle();
      motor.setSpeed(1.0);
    }

    @Test
    void execute_ShouldMoveMotorForward() {
      CommandTester runner = new CommandTester(liftinIndexUp);
      runner.step(0);
      assertThat(motor.getSpeed()).isGreaterThan(0);
    }

    @Nested
    class WhenLimitIsNotPressed {

      @BeforeEach
      void setLimitNotPressed() {
        hasIndexedSwitch.setNotTriggered();
      }

      @Test
      void execute_ShouldContinueIndexing() {
        CommandTester runner = new CommandTester(liftinIndexUp);
        boolean isFinished = runner.step(0);
        assertThat(isFinished).isFalse();
      }
    }

    @Nested
    class WhenLimitIsPressed {

      @BeforeEach
      void pressLimit() {
        hasIndexedSwitch.setTriggered();
      }

      @Test
      void execute_ShouldFinish() {
        CommandTester runner = new CommandTester(liftinIndexUp);
        boolean isFinished = runner.step(0);
        assertThat(isFinished).isTrue();
      }

      @Test
      void executing_ShouldKillMotor() {
        CommandTester runner = new CommandTester(liftinIndexUp);
        runner.step(0);
        assertThat(motor.getSpeed()).isZero();
      }
    }
  }
}