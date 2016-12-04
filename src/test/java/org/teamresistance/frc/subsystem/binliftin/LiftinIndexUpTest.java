package org.teamresistance.frc.subsystem.binliftin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.strongback.command.Command;
import org.strongback.command.CommandTester;
import org.strongback.mock.Mock;
import org.strongback.mock.MockMotor;
import org.strongback.mock.MockSwitch;
import org.teamresistance.frc.util.CommandUtilities;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is an integration test that tests the combined logic for both the {@link LiftinIndexUp}
 * command and the {@link BinLiftin} subsystem. Only the hardware-based inputs and outputs have
 * been replaced with mocks (motors and sensors). The status of the command (e.g. continuing vs
 * finished) and the motor outputs are used as verification.
 *
 * @author Rothanak So
 * @see BinLiftinTest
 * @see LiftinIndexDownTest
 */
class LiftinIndexUpTest {
  private final FakeTuskWatcher tuskWatcher = FakeTuskWatcher.atHome();
  private final MockSwitch hasIndexedSwitch = Mock.notTriggeredSwitch();
  private final MockMotor motor = Mock.stoppedMotor();

  private final BinLiftin binLiftin = new BinLiftin(motor, tuskWatcher, hasIndexedSwitch);
  private final Command liftinIndexUp = new LiftinIndexUp(binLiftin);

  @Test
  void whenMotorRunning_interrupt_ShouldKillMotor() {
    motor.setSpeed(1.0);
    CommandTester runner = new CommandTester(liftinIndexUp);
    CommandUtilities.interrupt(runner);
    assertThat(motor.getSpeed()).isZero();
  }

  // Notice there is no test case for "finish_ShouldKillMotor". This is because is no way for
  // us to manually finish a command. This is actually by design, as a command's final state
  // also depends on whatever happens in #execute, not just #stop. Thus, "finish" varies by
  // scenario too, so we test it within the scenarios wherever relevant.

  @Nested
  class WhenAtTop {

    @BeforeEach
    void pretendAtTop() {
      tuskWatcher.setAtTop();
      motor.setSpeed(1.0);
    }

    @Test
    void execute_ShouldImmediatelyFinish() {
      CommandTester runner = new CommandTester(liftinIndexUp);
      boolean isFinished = runner.step(0);
      assertThat(isFinished).isTrue();
    }

    @Test
    void execute_ShouldKillMotor() {
      CommandTester runner = new CommandTester(liftinIndexUp);
      runner.step(0);
      assertThat(motor.getSpeed()).isZero();
    }
  }

  @Nested
  class WhenNotAtTop {

    @BeforeEach
    void pretendInMiddle() {
      tuskWatcher.setInMiddle();
      motor.setSpeed(1.0);
    }

    @Nested
    class WhenStillIndexing {

      @BeforeEach
      void setStillIndexing() {
        hasIndexedSwitch.setNotTriggered();
      }

      @Test
      void execute_ShouldContinueIndexing() {
        CommandTester runner = new CommandTester(liftinIndexUp);
        boolean isFinished = runner.step(0);
        assertThat(isFinished).isFalse();
      }

      @Test
      void execute_ShouldMoveMotorForward() {
        CommandTester runner = new CommandTester(liftinIndexUp);
        runner.step(0);
        assertThat(motor.getSpeed()).isPositive();
      }
    }

    @Nested
    class WhenHasIndexed {

      @BeforeEach
      void setHasIndexed() {
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