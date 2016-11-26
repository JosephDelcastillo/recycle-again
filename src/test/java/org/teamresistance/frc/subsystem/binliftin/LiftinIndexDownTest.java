package org.teamresistance.frc.subsystem.binliftin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.strongback.command.CommandTester;
import org.strongback.mock.Mock;
import org.strongback.mock.MockMotor;
import org.teamresistance.frc.util.CommandUtilities;
import org.teamresistance.frc.util.FakeBooleanSupplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is an integration test that tests the combined logic for both the {@link LiftinIndexDown}
 * command and the {@link BinLiftin} subsystem. Only the hardware-based inputs and outputs have
 * been replaced with mocks (motors and sensors). The status of the command (e.g. continuing vs
 * finished) and the motor outputs are used as verification.
 *
 * @author Rothanak So
 * @see BinLiftinTest
 * @see LiftinIndexUpTest
 */
class LiftinIndexDownTest {
  private final FakeBooleanSupplier isAtZero = new FakeBooleanSupplier(false);
  private final FakeBooleanSupplier hasIndexed = new FakeBooleanSupplier(false);
  private final MockMotor motor = Mock.stoppedMotor();

  private BinLiftin binLiftin = new BinLiftin(motor, FakeTuskWatcher.atZero());
  private LiftinIndexDown liftinIndexDown = new LiftinIndexDown(binLiftin, isAtZero, hasIndexed);

  @Test
  void whenMotorRunning_interrupt_ShouldKillMotor() {
    motor.setSpeed(1.0);
    CommandTester runner = new CommandTester(liftinIndexDown);
    CommandUtilities.interrupt(runner);
    assertThat(motor.getSpeed()).isZero();
  }

  @Nested
  class WhenAtZero {

    @BeforeEach
    void pretendAtZero() {
      isAtZero.setValue(true);
      motor.setSpeed(1.0);
    }

    @Test
    void execute_ShouldImmediatelyFinish() {
      CommandTester runner = new CommandTester(liftinIndexDown);
      boolean isFinished = runner.step(0);
      assertThat(isFinished).isTrue();
    }

    @Test
    void execute_ShouldKillMotor() {
      CommandTester runner = new CommandTester(liftinIndexDown);
      runner.step(0);
      assertThat(motor.getSpeed()).isZero();
    }
  }

  @Nested
  class WhenNotAtZero {

    @BeforeEach
    void pretendInMiddle() {
      isAtZero.setValue(false);
      motor.setSpeed(1.0);
    }

    @Nested
    class WhenStillIndexing {

      @BeforeEach
      void setStillIndexing() {
        hasIndexed.setValue(false);
      }

      @Test
      void execute_ShouldContinueIndexing() {
        CommandTester runner = new CommandTester(liftinIndexDown);
        boolean isFinished = runner.step(0);
        assertThat(isFinished).isFalse();
      }

      @Test
      void execute_ShouldMoveMotorBackward() {
        CommandTester runner = new CommandTester(liftinIndexDown);
        runner.step(0);
        assertThat(motor.getSpeed()).isNegative();
      }
    }

    @Nested
    class WhenHasIndexed {

      @BeforeEach
      void pressLimit() {
        hasIndexed.setValue(true);
      }

      @Test
      void execute_ShouldFinish() {
        CommandTester runner = new CommandTester(liftinIndexDown);
        boolean isFinished = runner.step(0);
        assertThat(isFinished).isTrue();
      }

      @Test
      void executing_ShouldKillMotor() {
        CommandTester runner = new CommandTester(liftinIndexDown);
        runner.step(0);
        assertThat(motor.getSpeed()).isZero();
      }
    }
  }
}