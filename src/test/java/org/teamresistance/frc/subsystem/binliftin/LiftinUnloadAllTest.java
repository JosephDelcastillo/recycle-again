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
 * This is an integration test that verifies the logic for the {@link LiftinUnloadAll} command
 * with a real {@link BinLiftin} instance. All other dependencies (sensors, motors) are mocked.
 *
 * @author Rothanak So
 * @see BinLiftinTest
 * @see LiftinIndexUpTest
 */
class LiftinUnloadAllTest {
  private final FakeBooleanSupplier isAtHome = new FakeBooleanSupplier(false);
  private final MockMotor motor = Mock.stoppedMotor();

  private final BinLiftin binLiftin = new BinLiftin(motor, FakeTuskWatcher.atZero());
  private final LiftinUnloadAll unloadAllCommand = new LiftinUnloadAll(binLiftin, isAtHome);

  @Test
  void whenMotorRunning_interrupt_ShouldKillMotor() {
    motor.setSpeed(1.0);
    CommandTester runner = new CommandTester(unloadAllCommand);
    CommandUtilities.interrupt(runner);
    assertThat(motor.getSpeed()).isZero();
  }

  @Nested
  class BeforeAtHome {

    @BeforeEach
    void setBeforeAtHome() {
      isAtHome.setValue(false);
      motor.stop();
    }

    @Test
    void execute_ShouldContinue() {
      CommandTester runner = new CommandTester(unloadAllCommand);
      boolean isFinished = runner.step(0);
      assertThat(isFinished).isFalse();
    }

    @Test
    void execute_ShouldMoveMotorBackward() {
      CommandTester runner = new CommandTester(unloadAllCommand);
      runner.step(0);
      assertThat(motor.getSpeed()).isNegative();
    }

    @Nested
    class WhenTimerExpires {
      private static final int REALLY_LONG_TIME = (int) 1e10;

      @Test
      void execute_ShouldFinish() {
        CommandTester runner = new CommandTester(unloadAllCommand);
        runner.step(0); // initialize command
        boolean isFinished = runner.step(REALLY_LONG_TIME);
        assertThat(isFinished).isTrue();
      }

      @Test
      void execute_ShouldKillMotor() {
        CommandTester runner = new CommandTester(unloadAllCommand);
        runner.step(0); // initialize command
        runner.step(REALLY_LONG_TIME);
        assertThat(motor.getSpeed()).isZero();
      }
    }
  }

  @Nested
  class WhenAtHome {

    @BeforeEach
    void setAtHome() {
      isAtHome.setValue(true);
      motor.setSpeed(1.0);
    }

    @Test
    void execute_ShouldFinish() {
      CommandTester runner = new CommandTester(unloadAllCommand);
      boolean isFinished = runner.step(0);
      assertThat(isFinished).isTrue();
    }

    @Test
    void execute_ShouldKillMotor() {
      CommandTester runner = new CommandTester(unloadAllCommand);
      runner.step(0);
      assertThat(motor.getSpeed()).isZero();
    }
  }
}