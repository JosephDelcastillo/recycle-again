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
 * This is an integration test that verifies the logic for the {@link LiftinGoHome} command with
 * a real {@link BinLiftin} instance. All other dependencies (sensors, motors) are mocked.
 *
 * @author Rothanak So
 * @see BinLiftinTest
 * @see LiftinIndexUpTest
 */
class LiftinGoHomeTest {
  private final FakeBooleanSupplier isAtHome = new FakeBooleanSupplier(false);
  private final MockMotor motor = Mock.stoppedMotor();

  private final BinLiftin binLiftin = new BinLiftin(motor, FakeTuskWatcher.atZero());
  private final LiftinGoHome goHomeCommand = new LiftinGoHome(binLiftin, isAtHome);

  @Test
  void whenMotorRunning_Interrupt_ShouldKillMotor() {
    motor.setSpeed(1.0);
    CommandTester runner = new CommandTester(goHomeCommand);
    CommandUtilities.interrupt(runner);
    assertThat(motor.getSpeed()).isZero();
  }

  @Nested
  class WhenReachedHome {

    @BeforeEach
    void reachHome() {
      isAtHome.setValue(true);
      motor.setSpeed(1.0);
    }

    @Test
    void execute_ShouldFinish() {
      CommandTester runner = new CommandTester(goHomeCommand);
      boolean isFinished = runner.step(0);
      assertThat(isFinished).isTrue();
    }

    @Test
    void execute_ShouldKillMotor() {
      CommandTester runner = new CommandTester(goHomeCommand);
      runner.step(0);
      assertThat(motor.getSpeed()).isZero();
    }
  }

  @Nested
  class WhenAwayFromHome {

    @BeforeEach
    void awayFromHome() {
      isAtHome.setValue(false);
      motor.setSpeed(1.0);
    }

    @Test
    void execute_ShouldContinue() {
      CommandTester runner = new CommandTester(goHomeCommand);
      boolean isFinished = runner.step(0);
      assertThat(isFinished).isFalse();
    }

    @Test
    void execute_ShouldMoveMotorBackward() {
      CommandTester runner = new CommandTester(goHomeCommand);
      runner.step(0);
      assertThat(motor.getSpeed()).isNegative();
    }
  }
}