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
 * This is an integration test that tests the combined logic for both the {@link LiftinIndexDown}
 * command and the {@link BinLiftin} subsystem. Only the hardware-based inputs and ouputs have
 * been replaced with mocks (motors and sensors). The status of the command (e.g. continuing vs
 * finished) and the motor outputs are used as verification.
 *
 * @author Rothanak So
 * @see BinLiftinTest
 * @see LiftinIndexUpTest
 */
class LiftinIndexDownTest {
  private MockMotor motor = Mock.stoppedMotor();
  private MockSwitch hasIndexedSwitch = Mock.notTriggeredSwitch();
  private FakeTuskWatcher liftinWatcher = FakeTuskWatcher.atBottom();

  private BinLiftin binLiftin = new BinLiftin(motor, liftinWatcher);
  private LiftinIndexDown liftinIndexDown = new LiftinIndexDown(binLiftin, hasIndexedSwitch);

  @Test
  void interrupt_ShouldStopMotor() {
    CommandTester runner = new CommandTester(liftinIndexDown);
    CommandUtilities.interrupt(runner);
    assertThat(motor.getSpeed()).isZero();
  }

  @Nested
  class WhenAtBottom {

    @BeforeEach
    void pretendAtBottom() {
      liftinWatcher.setAtBottom();
    }

    @Test
    void execute_ShouldImmediatelyFinish() {
      CommandTester runner = new CommandTester(liftinIndexDown);
      boolean isFinished = runner.step(0);
      assertThat(isFinished).isTrue();
    }

    @Test
    void execute_ShouldStopMotor() {
      CommandTester runner = new CommandTester(liftinIndexDown);
      runner.step(0);
      assertThat(motor.getSpeed()).isZero();
    }
  }

  @Nested
  class WhenNotAtBottom {

    @BeforeEach
    void pretendInMiddle() {
      liftinWatcher.setInMiddle();
    }

    @Test
    void execute_ShouldMoveMotorBackward() {
      CommandTester runner = new CommandTester(liftinIndexDown);
      runner.step(0);
      assertThat(motor.getSpeed()).isNegative();
    }


    @Nested
    class WhenLimitIsNotPressed {

      @BeforeEach
      void setLimitNotPressed() {
        hasIndexedSwitch.setNotTriggered();
      }

      @Test
      void execute_ShouldContinueIndexing() {
        CommandTester runner = new CommandTester(liftinIndexDown);
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
        CommandTester runner = new CommandTester(liftinIndexDown);
        boolean isFinished = runner.step(0);
        assertThat(isFinished).isTrue();
      }

      @Test
      void executing_ShouldStopMotor() {
        CommandTester runner = new CommandTester(liftinIndexDown);
        runner.step(0);
        assertThat(motor.getSpeed()).isZero();
      }
    }
  }
}