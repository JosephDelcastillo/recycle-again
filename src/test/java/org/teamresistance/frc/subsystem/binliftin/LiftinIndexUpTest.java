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
 * command and the {@link BinLiftin} subsystem. Only the hardware-based inputs and ouputs have
 * been replaced with mocks (motors and sensors). The status of the command (e.g. continuing vs
 * finished) and the motor outputs are used as verification.
 *
 * @author Rothanak So
 * @see BinLiftinTest
 * @see LiftinIndexDownTest
 */
class LiftinIndexUpTest {
  private MockMotor motor = Mock.stoppedMotor();
  private MockSwitch hasIndexedSwitch = Mock.notTriggeredSwitch();
  private FakeTuskWatcher liftinWatcher = FakeTuskWatcher.atBottom();

  private BinLiftin binLiftin = new BinLiftin(motor, liftinWatcher);
  private LiftinIndexUp liftinIndexUp = new LiftinIndexUp(binLiftin, hasIndexedSwitch);

  @Test
  void interrupt_ShouldStopMotor() {
    CommandTester runner = new CommandTester(liftinIndexUp);
    CommandUtilities.interrupt(runner);
    assertThat(motor.getSpeed()).isZero();
  }

  // Notice there is no test case for "finish_ShouldStopMotor". This is because is no way for
  // us to manually finish a command. This is actually by design, as a command's final state
  // also depends on whatever happens in #execute, not just #stop. Thus, "finish" varies by
  // scenario too, so we test it within the scenarios wherever relevant.

  @Nested
  class WhenAtTop {

    @BeforeEach
    void pretendAtTop() {
      liftinWatcher.setAtTop();
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
      void executing_ShouldStopMotor() {
        CommandTester runner = new CommandTester(liftinIndexUp);
        runner.step(0);
        assertThat(motor.getSpeed()).isZero();
      }
    }
  }
}